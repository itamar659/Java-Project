package webEngine.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import engine.base.*;
import logic.Engine;
import logic.evoAlgorithm.TimeTableProblem;
import logic.evoAlgorithm.crossovers.AspectOriented;
import logic.evoAlgorithm.factory.Factories;
import logic.evoAlgorithm.mutations.Flipping;
import logic.evoAlgorithm.selections.Tournament;
import logic.evoAlgorithm.selections.Truncation;
import logic.timeTable.TimeTable;
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

    /* Servlet actions:
     * ================
     * "getEngine": get the information about the problem and it's engine
     * "update": update the engine configurations (crossover, selection, pop size...)
     * "start": start the engine
     * "stop": stop the engine
     * "pause": pause the engine
     * "resume": resume the engine
     * "getUserInfo": returns the username and best fitness
     * "getSolution": return the lessons of the best solution
     * "getUsersListInformation": returns a list of all the users solving this problem
     *                            Each user will have a username, best fitness, and current generation
     * "getUserEngine": expect parameter "username" to know which engine to return
    */

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

        String action = request.getParameter(Constants.ACTION_PARAMETER);
        action = action == null ? "getEngine" : action;

        switch (action) {
            case "getEngine":
                response.getOutputStream().println(
                        ServletUtils.createFullCustomGson().toJson(
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
            case "getSolution":
                response.getOutputStream().println(
                        new Gson().toJson(
                                getTheEngine(user, problemId).getBestResult().getLessons()
                        )
                );
                break;
            case "getUsersListInformation":
                getUsersListInformation(response, problemId);
                break;
            case "getUserEngine":
                try {
                String reqUsername = request.getParameter(Constants.USERNAME_PARAMETER);
                User reqUser = ServletUtils.getUserManager(getServletContext()).getUserByName(reqUsername);
                response.getOutputStream().println(
                        ServletUtils.createFullCustomGson().toJson(
                                getTheEngine(reqUser, problemId)
                        )
                );
                } catch (Exception ignored) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
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
        userJson.addProperty("engineStatus", user.getEngine(problemId).getState().toString());
        response.getOutputStream().println(
                new Gson().toJson(
                        userJson
                )
        );
    }

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
//        if (engine.getState() == Engine.State.RUNNING) {
//            response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
//            return;
//        }

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
        final String CROSSOVER_NAME_PARAM = "crossoverName";
        final String CROSSOVER_CUTTING_POINTS_PARAM = "crossoverCuttingPoints";
        final String CROSSOVER_ASPECT_ORIENTATION_PARAM = "crossoverInputAspect";

        final String SELECTION_NAME_PARAM = "selectionName";
        final String SELECTION_INPUT_PARAM = "selectionInput";

        final String MUTATION_NAME_PARAM = "mutations";

        final String POPULATION_SIZE_PARAM = "populationSize";
        final String ELITISM_PARAM = "elitism";

        final String INTERVAL_PARAM = "interval";

        final String MAX_GEN_PARAM = "maxGeneration";
        final String MAX_FITNESS_PARAM = "maxFitness";
        final String MAX_TIME_PARAM = "maxTime";


        // Set engine configurations
        try {
            engine.setPopulationSize(Integer.parseInt(request.getParameter(POPULATION_SIZE_PARAM)));
            engine.setElitism(Integer.parseInt(request.getParameter(ELITISM_PARAM)));
            engine.setUpdateGenerationInterval(Integer.parseInt(request.getParameter(INTERVAL_PARAM)));

            try {
                engine.setMaxGenerationsCondition(Integer.parseInt(request.getParameter(MAX_GEN_PARAM)));
                engine.addStopCondition(Engine.StopCondition.MAX_GENERATIONS);
            } catch (Exception ignored) {
                engine.removeStopCondition(Engine.StopCondition.MAX_GENERATIONS);
            }
            try {
                engine.setMaxFitnessCondition(Float.parseFloat(request.getParameter(MAX_FITNESS_PARAM)));
                engine.addStopCondition(Engine.StopCondition.REQUESTED_FITNESS);
            } catch (Exception ignored) {
                engine.removeStopCondition(Engine.StopCondition.REQUESTED_FITNESS);
            }
            try {
                engine.setTimeStopCondition(Long.parseLong(request.getParameter(MAX_TIME_PARAM)));
                engine.addStopCondition(Engine.StopCondition.BY_TIME);
            } catch (Exception ignored) {
                engine.removeStopCondition(Engine.StopCondition.BY_TIME);
            }

            engine.changeCrossover(request.getParameter(CROSSOVER_NAME_PARAM));
            engine.changeSelection(request.getParameter(SELECTION_NAME_PARAM));
            engine.getMutations().clear();

            // Get Crossover
            Crossover<TimeTable> crossover = engine.getEvoEngineSettings().getCrossover();
            crossover.setCuttingPoints(Integer.parseInt(request.getParameter(CROSSOVER_CUTTING_POINTS_PARAM)));
            if (crossover instanceof AspectOriented) {
                crossover.setParameter(AspectOriented.PARAMETER_ORIENTATION, request.getParameter(CROSSOVER_ASPECT_ORIENTATION_PARAM));
            }

            // Get Selection
            Selection<TimeTable> selection = engine.getEvoEngineSettings().getSelection();
            if (selection instanceof Tournament) {
                ((Tournament) selection).setParameter(Tournament.PARAMETER_PTE, request.getParameter(SELECTION_INPUT_PARAM));
            } else if (selection instanceof Truncation) {
                ((Truncation) selection).setParameter(Truncation.PARAMETER_TOP_PERCENT, request.getParameter(SELECTION_INPUT_PARAM));
            }

            // Get Mutations
            int currentMutation = 0;
            final String mutationStringFormat = "mutations[%d][%s]";
            while (request.getParameter(String.format(mutationStringFormat, currentMutation, "name")) != null) {
                String name = request.getParameter(String.format(mutationStringFormat, currentMutation, "name"));
                String probability = request.getParameter(String.format(mutationStringFormat, currentMutation, "probability"));
                String maxTupples = request.getParameter(String.format(mutationStringFormat, currentMutation, "maxTupples"));

                Mutation<TimeTable> mutation = Factories.getMutationFactory().create(name);
                mutation.setParameter(Flipping.PARAMETER_PROBABILITY, probability);
                mutation.setParameter(Flipping.PARAMETER_MAX_TUPPLES, maxTupples);

                if (mutation instanceof Flipping) {
                    String component = request.getParameter(String.format(mutationStringFormat, currentMutation, "component"));
                    mutation.setParameter(Flipping.PARAMETER_COMPONENT, component);
                }

                engine.getMutations().add(mutation);
                currentMutation++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Engine getTheEngine(User user, int userProblemId) {
        if (user == null) {
            return null;
        }
        Engine engine = user.getEngine(userProblemId);
        if (engine == null) {
            TimeTableProblem problem =
                    ServletUtils.getProblemManager(getServletContext()).getProblem(userProblemId);

            engine = new Engine();
            engine.loadTTEEngineByProblem(problem);

            user.addEngine(userProblemId, engine);
        }

        return engine;
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