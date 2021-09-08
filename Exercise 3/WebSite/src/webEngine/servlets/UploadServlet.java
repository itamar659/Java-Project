package webEngine.servlets;

import com.google.gson.Gson;
import logic.Engine;
import logic.schema.exceptions.XMLExtractException;
import webEngine.utils.ServletUtils;

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/json");

        String errorMessage = null;
        boolean isFileCorrupted = false;

        Part theEngineXML = request.getPart("file");
        String xmlFileAsString = readFromInputStream(theEngineXML.getInputStream());

        try {
            Engine engine = new Engine();
            engine.loadTTEEngineFromString(xmlFileAsString);
            ServletUtils.getEngineManager(getServletContext()).addEngine(engine);
        } catch (JAXBException | XMLExtractException e) {
            errorMessage = e.getMessage();
            isFileCorrupted = true;
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
