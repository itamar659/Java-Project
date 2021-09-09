package webEngine.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import logic.evoAlgorithm.TimeTableProblem;
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

@WebServlet(urlPatterns = {"/evolutionAlgorithm"})
public class SolveProblemServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Check if a valid user request this page
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(getServletContext().getContextPath() + Constants.PAGE_LOGIN);
            return;
        }

        // TODO - 'action' if the user want to enroll a problem or unroll from it.
        //  in that case we dont need to check the problem id parameter the client sent.

        response.setContentType("application/json");

        // Get the required parameters to work with
        User user = ServletUtils.getUserManager(getServletContext()).getUserByName(username);
        Integer userProblemId = user.getSolvingProblemID();
        boolean canRunAlgorithm = userProblemId == null;
        int requestedProblemId;
        try {
            requestedProblemId = Integer.parseInt(request.getParameter(Constants.PROBLEM_ID_PARAMETER));
        } catch (NumberFormatException ignored) {
            // if the client sent an invalid problem id, callback bad request
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!canRunAlgorithm) {
            // can't solve another problem
            // TODO - user CAN'T solve another problem.

        } else {
            // TODO - create a new evolution algorithm engine and set it

            // get the problem
            TimeTableProblem problem =
                    ServletUtils.getProblemManager(getServletContext()).getProblem(requestedProblemId);
            if (problem == null) {
                // invalid problem id
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // set the user to solve this problem and only this.
            user.setSolvingProblemID(requestedProblemId);
        }



        // return json
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("canRunAlgorithm", canRunAlgorithm);
        jsonObject.addProperty("problemId", userProblemId);
        jsonObject.addProperty("url", canRunAlgorithm ? Constants.PAGE_SOLVE_PROBLEM : "");
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