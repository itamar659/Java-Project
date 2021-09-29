package webEngine.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import engine.base.*;
import logic.Engine;
import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.rules.base.Rule;
import webEngine.gsonHelpers.gsonSerializers.*;
import webEngine.gsonHelpers.gsonStrategy.EngineStrategy;
import webEngine.gsonHelpers.gsonStrategy.EvolutionEngineExclusionStrategy;
import webEngine.gsonHelpers.gsonStrategy.SolutionExclusionStrategy;
import webEngine.helpers.BaseSecurityHttpServlet;
import webEngine.helpers.Constants;
import webEngine.users.User;
import webEngine.utils.ServletLogger;
import webEngine.utils.ServletUtils;
import webEngine.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/evolutionengine"})
public class SolveProblemServlet extends BaseSecurityHttpServlet {

    private final Gson gson;

    public SolveProblemServlet() {
        this.gson = createGsonByGsonBuilder();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasSession(request, response)) {
            return;
        }

        response.setContentType("application/json");
        User user = SessionUtils.getUser(request);
        Integer problemId = user.getActiveProblemID();
        if (problemId == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getOutputStream().println(Constants.PAGE_HOME);
            ServletLogger.getLogger().warning(String.format(
                    "%s tried to view his active problem, but there is no active problem.", user.getUsername()));
            return;
        }

        response.setContentType("application/json");
        Engine engine = getTheEngine(request, problemId);
        String action = request.getParameter(Constants.ACTION_PARAMETER);
        action = action == null ? "getEngine" : action;

        switch (action) {
            case "getEngine":
                response.getOutputStream().println(
                        createGsonByGsonBuilder().toJson(
                                getTheEngine(user, problemId)
                        )
                );
                break;
            case "update":
                updateEngine(request, getTheEngine(user, problemId));
                break;
            case "start":
                startResumeEngine(response, user, getTheEngine(user, problemId), false);
                break;
            case "stop":
                getTheEngine(user, problemId).stopAlgorithm();
                break;
            case "resume":
                startResumeEngine(response, user, getTheEngine(user, problemId), true);
                break;
            case "pause":
                getTheEngine(user, problemId).pauseAlgorithm();
                break;
            case "getUserInfo":
                getUserInformation(response, user, problemId);
                break;
            case "getUsersListInformation":
                getUsersListInformation(response, problemId);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                break;
        }
    }

    private void getUserInformation(HttpServletResponse response, User user, Integer problemId) throws IOException {
        JsonObject userJson = new JsonObject();
        userJson.addProperty("username", user.getUsername());
        boolean hasFitness = user.getEngine(problemId).getBestResult() != null;
        userJson.addProperty("bestFitness",
                hasFitness ? user.getEngine(problemId).getBestResult().getFitness() : 0);
        response.getOutputStream().println(
                gson.toJson(
                        engine
                new Gson().toJson(
                        userJson
                )
        );
    }

    

    private Engine getTheEngine(HttpServletRequest request, int userProblemId) {
        TimeTableProblem problem =
                ServletUtils.getProblemManager(getServletContext()).getProblem(userProblemId);
    private void getUsersListInformation(HttpServletResponse response, Integer problemId) throws IOException {
        JsonObject usersJson = new JsonObject();

        // TODO: Can convert to users id list instead.
        ServletUtils.getProblemManager(getServletContext())
                .getProblemStatistics(problemId)
                .getUsers()
                .forEach(currentUser -> {
                    try {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("username", currentUser.getUsername());

                        boolean hasFitnesss = currentUser.getEngine(problemId).getBestResult() != null;
                        jsonObject.addProperty("bestFitness",
                                hasFitnesss ? currentUser.getEngine(problemId).getBestResult().getFitness() : 0);
                        jsonObject.addProperty("currentGeneration", currentUser.getEngine(problemId).getCurrentGeneration());

                        usersJson.add(currentUser.getUsername(), jsonObject);
                    } catch (Exception e) {
                        System.out.printf("Couldn't add '%s' to users list with the problem id %d.%n", currentUser.getUsername(), problemId);
                    }
                });

        response.getOutputStream().println(
                new Gson().toJson(
                        usersJson
                )
        );
    }

    private void startResumeEngine(HttpServletResponse response, User user, Engine engine, boolean resume) {
        if (engine.getState() == Engine.State.RUNNING) {
            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            return;
        }

        Engine engine = (Engine) SessionUtils.getAttribute(request, Constants.ENGINE_PARAMETER);
        // TODO - Check configuration
        if (engine.getEvoEngineSettings().getPopulationSize() <= 0) {

        }
        if (engine.getEvoEngineSettings().getElitism() < 0) {

        }
        if (engine.getEvoEngineSettings().getCrossover() == null) {

        }
        if (engine.getEvoEngineSettings().getSelection() == null) {

        }

        // Start the algorithm
        Thread resumeThread = new Thread(resume ? engine::resumeAlgorithm : engine::startAlgorithm);
        resumeThread.setName(user.getUsername() + " Thread");
        resumeThread.start();
    }

    private void updateEngine(HttpServletRequest request, Engine engine) {
        // TODO:
//        gson.fromJson()
    }

    private Engine getTheEngine(User user, int userProblemId) {
        Engine engine = user.getEngine(userProblemId);
        if (engine == null) {
            TimeTableProblem problem =
                    ServletUtils.getProblemManager(getServletContext()).getProblem(userProblemId);

            engine = new Engine();
            engine.loadTTEEngineByProblem(problem);

            SessionUtils.setAttribute(request, Constants.ENGINE_PARAMETER, engine);
            user.addEngine(userProblemId, engine);
        }

        return engine;
    }

    private Gson createGsonByGsonBuilder() {
//        TODO:
//         add isPaused to Engine.
//         - With the engine json object, return the engine state (idle, running, paused, completed)

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