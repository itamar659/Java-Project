package webEngine.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import webEngine.helpers.BaseSecurityHttpServlet;

import webEngine.helpers.Constants;
import webEngine.users.User;
import webEngine.utils.ServletLogger;
import webEngine.utils.ServletUtils;
import webEngine.utils.SessionUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/problems"})
public class ProblemServlet extends BaseSecurityHttpServlet {

    public static Integer getProblemID(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
        int requestedProblemId;
        try {
            requestedProblemId = Integer.parseInt(request.getParameter(Constants.PROBLEM_ID_PARAMETER));

            if (!ServletUtils.getProblemManager(servletContext).contains(requestedProblemId)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return null;
            }
        } catch (NumberFormatException ignored) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return null;
        }

        return requestedProblemId;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasSession(request, response)) {
            return;
        }

        response.setContentType("application/json");

        String action = request.getParameter(Constants.ACTION_PARAMETER);
        action = action == null ? "getproblemlist" : action;

        switch (action) {
            case "getproblemlist":
                response.getOutputStream().println(
                        new Gson().toJson(
                                ServletUtils.getProblemManager(getServletContext()).getProblemsStatistics()
                        )
                );
                break;
            case "getproblem":
                getProblem(request, response);
                break;
            case "add":
                addUserToProblem(request, response);
                break;
            case "remove":
                removeUserFromProblem(request, response);
                break;
            default:
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                break;
        }
    }

    private void getProblem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson = new Gson();
        try {
            int problemID = Integer.parseInt(request.getParameter(Constants.PROBLEM_ID_PARAMETER));
            response.getOutputStream().println(
                    gson.toJson(
                            ServletUtils.getProblemManager(getServletContext()).getProblemStatistics(problemID)
                    )
            );
        } catch (NumberFormatException ignored) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getOutputStream().println("The value has to be an integer. Sent: " +
                    request.getParameter(Constants.PROBLEM_ID_PARAMETER));
        }
    }

    private void removeUserFromProblem(HttpServletRequest request, HttpServletResponse response) {
        Integer requestedProblemId = getProblemID(request, response, getServletContext());
        if (requestedProblemId == null) {
            return;
        }

        User thisUser = SessionUtils.getUser(request);
        tryToRemoveUser(thisUser, requestedProblemId);
    }

    private void addUserToProblem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Integer requestedProblemId = getProblemID(request, response, getServletContext());
        if (requestedProblemId == null) {
            return;
        }

        User thisUser = SessionUtils.getUser(request);
        tryToAddUser(thisUser, requestedProblemId);

        thisUser.setActiveProblem(requestedProblemId);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("url", Constants.PAGE_SOLVE_PROBLEM);

        response.getOutputStream().println(
                new Gson().toJson(
                        jsonObject
                )
        );
    }

    private void tryToRemoveUser(User user, int problemId) {
        if (!user.isSolvingProblemID(problemId)) {
            ServletUtils.getProblemManager(getServletContext())
                    .getProblemStatistics(problemId)
                    .removeUser(user);

            ServletLogger.getLogger().info(String.format("user %s stop solving problem id: %d", user.getUsername(), problemId));
        }
    }

    private void tryToAddUser(User user, int problemId) {
        if (!user.isSolvingProblemID(problemId)) {
            ServletUtils.getProblemManager(getServletContext())
                    .getProblemStatistics(problemId)
                    .addUser(user);

            ServletLogger.getLogger().info(String.format("%s start trying to solve problem id: %d", user.getUsername(), problemId));
        }
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
