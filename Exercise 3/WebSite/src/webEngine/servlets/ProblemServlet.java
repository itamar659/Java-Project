package webEngine.servlets;

import com.google.gson.Gson;
import webEngine.helpers.Constants;
import webEngine.utils.ServletUtils;

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
        response.setContentType("application/json");

        String action = request.getParameter(Constants.ACTION_PARAMETER);

        Gson gson = new Gson();
        if (action == null) {
            response.getOutputStream().println(
                    gson.toJson(
                            ServletUtils.getProblemManager(getServletContext()).getProblemsStatistics()
                    )
            );
        } else if (action.equals("getproblem")) {
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
