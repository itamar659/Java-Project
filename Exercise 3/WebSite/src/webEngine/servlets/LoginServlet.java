package webEngine.servlets;


import webEngine.users.User;
import webEngine.users.UserManager;
import webEngine.utils.ServletLogger;
import webEngine.utils.ServletUtils;
import webEngine.helpers.Constants;
import webEngine.utils.SessionUtils;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (!SessionUtils.hasSession(request)) {
            // If it's the first time this user login
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String username = request.getParameter(Constants.USERNAME_PARAMETER);

            if (username != null && !username.trim().equals("")) {
                // Valid username, add or send duplication error
                username = username.trim();

                synchronized (this) {
                    if (!userManager.isUsernameExists(username)) {
                        // Add new username and create a session
                        User user = new User(username);
                        userManager.addUser(user);
                        SessionUtils.startSession(request, user);
                        response.getOutputStream().println(Constants.PAGE_HOME);
                        ServletLogger.getLogger().info(String.format("'%s' logged in.", username));
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

                ServletLogger.getLogger().info(String.format("'%s' tried to login with an invalid name.", username));
            }
        } else {
            // User already logged in
            response.getOutputStream().println(Constants.PAGE_HOME);
        }

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
