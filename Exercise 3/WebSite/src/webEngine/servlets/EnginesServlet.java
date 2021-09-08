package webEngine.servlets;

import com.google.gson.Gson;
import webEngine.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/engines"})
public class EnginesServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");

        // TODO - return something else..............
        //  too much of information to transfer
        //  AND don't need everything
        //  ANDDDD need to add more information about this engine/problem/problengine/wtf is should be

        Gson gson = new Gson();
        response.getOutputStream().println(
                gson.toJson(
                        ServletUtils.getEngineManager(getServletContext()).getEngines()
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
