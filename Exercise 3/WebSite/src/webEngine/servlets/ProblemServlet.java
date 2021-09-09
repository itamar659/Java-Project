package webEngine.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

@WebServlet(urlPatterns = {"/problems"})
public class ProblemServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(getServletContext().getContextPath() + Constants.PAGE_LOGIN);
            return;
        }

        response.setContentType("application/json");

        String action = request.getParameter(Constants.ACTION_PARAMETER);
        action = action == null ? "basic" : action;

        switch (action) {
            case "basic":
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
                removeUserFromProblem(request);
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

    private void removeUserFromProblem(HttpServletRequest request) {
        String username = SessionUtils.getUsername(request);
        User thisUser = ServletUtils.getUserManager(getServletContext()).getUserByName(username);
        Integer userProblemId = thisUser.getSolvingProblemID();

        if (userProblemId != null) {
            ServletUtils.getProblemManager(getServletContext())
                    .getProblemStatistics(userProblemId).removeUser(thisUser);
            thisUser.setSolvingProblemID(null);
        }
    }

    private void addUserToProblem(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Check the problem id the client sent
        int requestedProblemId;
        try {
            requestedProblemId = Integer.parseInt(request.getParameter(Constants.PROBLEM_ID_PARAMETER));
        } catch (NumberFormatException ignored) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (!ServletUtils.getProblemManager(getServletContext()).contains(requestedProblemId)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Get the required parameters to work with
        String username = SessionUtils.getUsername(request);
        User thisUser = ServletUtils.getUserManager(getServletContext()).getUserByName(username);
        Integer userProblemId = thisUser.getSolvingProblemID();
        boolean canRunAlgorithm = userProblemId == null;

        if (canRunAlgorithm) {
            // set the user to solve this problem and only this.
            ServletUtils.getProblemManager(getServletContext())
                    .getProblemStatistics(requestedProblemId).addUser(thisUser);
            thisUser.setSolvingProblemID(requestedProblemId);
            
            userProblemId = requestedProblemId;
        } else if (userProblemId == requestedProblemId){
            canRunAlgorithm = true;
        }

        // return json
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("canRunAlgorithm", canRunAlgorithm);
        jsonObject.addProperty("problemId", userProblemId);
        jsonObject.addProperty("url", canRunAlgorithm ? getServletContext().getContextPath() + Constants.PAGE_SOLVE_PROBLEM : "");
        response.getOutputStream().println(
                gson.toJson(
                        jsonObject
                )
        );
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
