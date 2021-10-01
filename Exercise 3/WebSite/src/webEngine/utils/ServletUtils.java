package webEngine.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.base.*;
import logic.timeTable.rules.base.Rule;
import webEngine.ProblemManager;
import webEngine.gsonHelpers.gsonSerializers.*;
import webEngine.gsonHelpers.gsonStrategy.EngineStrategy;
import webEngine.gsonHelpers.gsonStrategy.EvolutionEngineExclusionStrategy;
import webEngine.gsonHelpers.gsonStrategy.SolutionExclusionStrategy;
import webEngine.gsonHelpers.gsonStrategy.StopConditionStrategy;
import webEngine.users.UserManager;

import javax.servlet.ServletContext;

public final class ServletUtils {

    // Don't allow to create an instance of this class
    private ServletUtils() { }

    // ServletContext attributes identifiers;
    private static final String USER_MANAGER_ATTRIBUTE = "userManager";
    private static final String PROBLEM_MANAGER_ATTRIBUTE = "problemManager";

    // Locks for synchronization
    private static final Object userManagerLock = new Object();
    private static final Object problemManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {
        // Double check locking - if user manager exists
        if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE) == null) {
            synchronized (userManagerLock) {
                if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE) == null) {
                    servletContext.setAttribute(USER_MANAGER_ATTRIBUTE, new UserManager());
                }
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE);
    }

    public static ProblemManager getProblemManager(ServletContext servletContext) {
        // Double check locking - if problem manager exists
        if (servletContext.getAttribute(PROBLEM_MANAGER_ATTRIBUTE) == null) {
            synchronized (problemManagerLock) {
                if (servletContext.getAttribute(PROBLEM_MANAGER_ATTRIBUTE) == null) {
                    servletContext.setAttribute(PROBLEM_MANAGER_ATTRIBUTE, new ProblemManager());
                }
            }
        }
        return (ProblemManager) servletContext.getAttribute(PROBLEM_MANAGER_ATTRIBUTE);
    }

    public static Gson createFullCustomGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Selection.class, new SelectionSerializer<>())
                .registerTypeAdapter(Crossover.class, new CrossoverSerializer<>())
                .registerTypeAdapter(Mutation.class, new MutationSerializer<>())
                .registerTypeAdapter(Rule.class, new RuleSerializer<>())
                .registerTypeAdapter(Problem.class, new ProblemSerializer<>())
                .registerTypeAdapter(Solution.class, new TimeTableSerializer())
                .setExclusionStrategies(new SolutionExclusionStrategy())
                .setExclusionStrategies(new EvolutionEngineExclusionStrategy())
                .setExclusionStrategies(new EngineStrategy())
                .setExclusionStrategies(new StopConditionStrategy())
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }
}
