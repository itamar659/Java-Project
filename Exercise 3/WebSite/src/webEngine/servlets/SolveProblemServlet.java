package webEngine.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import logic.Engine;
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

        response.setContentType("application/json");

        User thisUser = ServletUtils.getUserManager(getServletContext()).getUserByName(username);
        Integer userProblemId = thisUser.getSolvingProblemID();

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