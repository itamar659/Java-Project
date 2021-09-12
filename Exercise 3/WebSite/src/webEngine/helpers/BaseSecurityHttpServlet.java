package webEngine.helpers;

import webEngine.utils.ServletLogger;
import webEngine.utils.SessionUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseSecurityHttpServlet extends HttpServlet {

    protected boolean hasSession(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        if (!SessionUtils.hasSession(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().println(Constants.PAGE_LOGIN);
            ServletLogger.getLogger().warning("The user is not authorized to get to this page without a known session id");
            return false;
        }
        return true;
    }
}
