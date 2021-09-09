package webEngine.servlets;

import com.google.gson.Gson;
import logic.evoAlgorithm.TimeTableProblem;
import logic.schema.TTEvoEngineCreator;
import logic.schema.exceptions.XMLExtractException;
import webEngine.ProblemStatisticsBuilder;
import webEngine.helpers.Constants;
import webEngine.ProblemStatistics;
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
public class UploadServlet extends HttpServlet {

    ProblemStatisticsBuilder problemStatisticsBuilder = new ProblemStatisticsBuilder();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.sendRedirect(getServletContext().getContextPath() + Constants.PAGE_LOGIN);
            return;
        }

        response.setContentType("text/json");
        String errorMessage = null;
        boolean isFileCorrupted = false;

        try {
            Part theEngineXML = request.getPart("file");
            String xmlFileAsString = readFromInputStream(theEngineXML.getInputStream());
            TimeTableProblem problem = TTEvoEngineCreator.createProblemFromXMLString(xmlFileAsString);

            problemStatisticsBuilder.setProblem(problem);
            problemStatisticsBuilder.setUploader(username);
            ServletUtils.getProblemManager(getServletContext()).addProblem(problemStatisticsBuilder.create());
        } catch (JAXBException | XMLExtractException e) {
            errorMessage = e.getMessage();
            isFileCorrupted = true;
        } catch (ServletException ignored) {
            response.sendRedirect(getServletContext().getContextPath() + Constants.PAGE_LOGIN);
            return;
        }

        Gson gson = new Gson();
        response.getOutputStream().println(gson.toJson(new JsonObjectToReturn(errorMessage, isFileCorrupted)));
    }

    private static class JsonObjectToReturn {
        public String errorMessage;
        public boolean isFileCorrupted;

        public JsonObjectToReturn(String errorMessage, boolean isFileCorrupted) {
            this.errorMessage = errorMessage;
            this.isFileCorrupted = isFileCorrupted;
        }
    }

    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
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
