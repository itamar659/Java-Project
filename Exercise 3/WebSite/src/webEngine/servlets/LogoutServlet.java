package webEngine.servlets;

import webEngine.helpers.Constants;
import webEngine.users.User;
import webEngine.utils.ServletLogger;
import webEngine.utils.ServletUtils;
import webEngine.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Remove the user from the user list
        String username = SessionUtils.getUsername(request);
        User thisUser = ServletUtils.getUserManager(getServletContext()).getUserByName(username);
        ServletUtils.getUserManager(getServletContext()).removeUser(username);

        // Remove any work this user do
        // TODO - remove the engine thread if exists
        if (thisUser.getSolvingProblemID() != null) {
            ServletUtils.getProblemManager(getServletContext())
                    .getProblemStatistics(thisUser.getSolvingProblemID())
                            .removeUser(thisUser);
        }

        // Remove session
        SessionUtils.endSession(request);
        response.getOutputStream().println(getServletContext().getContextPath() + Constants.PAGE_LOGIN);

        ServletLogger.getLogger().info(String.format("%s logged out.", username));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }
}
