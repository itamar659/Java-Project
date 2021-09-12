package webEngine.servlets;

import com.google.gson.Gson;
import webEngine.helpers.BaseSecurityHttpServlet;
import webEngine.helpers.Constants;
import webEngine.users.User;
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

@WebServlet(name = "UsersListServlet", urlPatterns = {"/userlist"})
public class UsersListServlet extends BaseSecurityHttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO - Add action count to send back only the number of users instead of the user list
//        if (!SessionUtils.hasSession(request)) {
//            // Don't allow guests to view the users list
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        } else {

        String action = request.getParameter(Constants.ACTION_PARAMETER);
        switch (action) {
            case "userList":
                responseUserList(response);
                break;
            case "username":
                responseUsername(request, response);
                break;
        }
//        }
    }

    private void responseUserList(HttpServletResponse response) throws IOException {
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            response.setContentType("application/json");
            UserManager userManager = ServletUtils.getUserManager(getServletContext());
            String json = gson.toJson(userManager.getNameList());
            out.println(json);
            out.flush();
        }
    }

    private void responseUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (hasSession(request,response)) {
            Gson gson = new Gson();
            response.setContentType("application/json");
            try (PrintWriter out = response.getWriter()) {
                String json = gson.toJson(SessionUtils.getUser(request).getUsername());
                out.println(json);
                out.flush();
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
