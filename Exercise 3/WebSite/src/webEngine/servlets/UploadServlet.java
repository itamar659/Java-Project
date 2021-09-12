package webEngine.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import logic.evoAlgorithm.TimeTableProblem;
import logic.schema.TTEvoEngineCreator;
import logic.schema.exceptions.XMLExtractException;
import webEngine.helpers.BaseSecurityHttpServlet;
import webEngine.helpers.Constants;
import webEngine.users.User;
import webEngine.utils.ServletLogger;
import webEngine.utils.ServletUtils;
import webEngine.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

@WebServlet(urlPatterns = {"/upload"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadServlet extends BaseSecurityHttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!hasSession(request, response)) {
            return;
        }

        response.setContentType("application/json");
        String errorMessage = null;
        boolean isFileCorrupted = false;

        try {
            Part theEngineXML = request.getPart("file");
            String xmlFileAsString = readFromInputStream(theEngineXML.getInputStream());
            TimeTableProblem problem = TTEvoEngineCreator.createProblemFromXMLString(xmlFileAsString);
            String username = SessionUtils.getUser(request).getUsername();

            ServletUtils.getProblemManager(getServletContext()).addProblem(username, problem);

            ServletLogger.getLogger().info(username + " uploaded a new problem to the site.");
        } catch (JAXBException | XMLExtractException e) {
            errorMessage = e.getMessage();
            isFileCorrupted = true;
        } catch (ServletException e) {
            response.sendRedirect(Constants.PAGE_LOGIN);
            ServletLogger.getLogger().severe(request.toString());
            ServletLogger.getLogger().severe("error message: " + e.getMessage());
            return;
        }

        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("isFileCorrupted", isFileCorrupted);
        jsonObject.addProperty("errorMessage", errorMessage);
        response.getOutputStream().println(gson.toJson(jsonObject));
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
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
