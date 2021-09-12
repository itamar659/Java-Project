package webEngine.servlets;

import com.google.gson.*;
import engine.base.*;
import logic.Engine;
import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.rules.base.Rule;
import webEngine.gsonHelpers.gsonSerializers.*;
import webEngine.gsonHelpers.gsonStrategy.EngineStrategy;
import webEngine.gsonHelpers.gsonStrategy.EvolutionEngineExclusionStrategy;
import webEngine.gsonHelpers.gsonStrategy.SolutionExclusionStrategy;
import webEngine.helpers.Constants;
import webEngine.users.User;
import webEngine.utils.ServletUtils;
import webEngine.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/evolutionengine"})
public class SolveProblemServlet extends HttpServlet {

    private final Gson gson;

    public SolveProblemServlet() {
        this.gson = createGsonByGsonBuilder();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        // Check if a valid user request this page
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().println(getServletContext().getContextPath() + Constants.PAGE_LOGIN);
            return;
        }

        User thisUser = ServletUtils.getUserManager(getServletContext()).getUserByName(username);
        Integer userProblemId = thisUser.getSolvingProblemID();

        Engine engine = getTheEngine(request, userProblemId);

        if (userProblemId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().println(getServletContext().getContextPath() + Constants.PAGE_HOME);
            return;
        }



        response.getOutputStream().println(
                gson.toJson(
                        engine
                )
        );

        System.out.println(
                gson.toJson(
                        engine
                )
        );


        // TODO: Allow user to solve many problems. Not just one.

    }

    private Engine getTheEngine(HttpServletRequest request, int userProblemId) {
        TimeTableProblem problem =
                ServletUtils.getProblemManager(getServletContext()).getProblem(userProblemId);

        Engine engine = (Engine) SessionUtils.getAttribute(request, Constants.ENGINE_PARAMETER);

        if (engine == null) {
            engine = new Engine();
            engine.loadTTEEngineByProblem(problem);

            SessionUtils.setAttribute(request, Constants.ENGINE_PARAMETER, engine);
        }

        return engine;
    }

    private Gson createGsonByGsonBuilder() {
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
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Regular doGet and doPost">

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    // </editor-fold>
}