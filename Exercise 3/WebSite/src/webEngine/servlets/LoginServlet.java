package webEngine.servlets;


import webEngine.users.UserManager;
import webEngine.utils.ServletUtils;
import webEngine.helpers.Constants;
import webEngine.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/Login"})
public class LoginServlet extends HttpServlet {

    private static final String HOME_PAGE = "/home.html";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        String usernameFromSession = SessionUtils.getUsername(request);

        if (usernameFromSession == null) {
            // If it's the first time this user login
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String username = request.getParameter(Constants.USERNAME_PARAMETER);

            if (username != null && !username.trim().equals("")) {
                // Valid username, add or send duplication error
                username = username.trim();

                synchronized (this) {
                    if (!userManager.isUserExists(username)) {
                        // Add new username and create a session
                        userManager.addUser(username);
                        SessionUtils.createNewSession(request, username);
                        response.getOutputStream().println(HOME_PAGE);
                    } else {
                        // Username already exists
                        response.getWriter().println("Username already exists. Please try a different one.");
                        response.setStatus(HttpServletResponse.SC_CONFLICT);
                    }
                }
            } else {
                // Username syntax is not valid
                response.getWriter().println("Please insert a valid username.");
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            }
        } else {
            // User already logged in
            response.getOutputStream().println(HOME_PAGE);
        }
    }
}
