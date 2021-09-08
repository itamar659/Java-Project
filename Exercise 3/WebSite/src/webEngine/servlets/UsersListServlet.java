package webEngine.servlets;

import com.google.gson.Gson;
import webEngine.users.UserManager;
import webEngine.utils.ServletUtils;
import webEngine.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet(name = "UsersListServlet", urlPatterns = {"/userlist"})
public class UsersListServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        if (!SessionUtils.hasSession(request)) {
            // Don't allow guests to view the users list - TODO: Maybe allow it and display it in the login/main(index) page.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                UserManager userManager = ServletUtils.getUserManager(getServletContext());
                Collection<String> usersList = userManager.getNameList();
                String json = gson.toJson(usersList);
                out.println(json);
                out.flush();
            }
        }
    }

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
}
