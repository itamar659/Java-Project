package webEngine.utils;

import webEngine.helpers.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionUtils {

    // Don't allow to create an instance of this class
    private SessionUtils() { }

    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME_PARAMETER) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void createNewSession(HttpServletRequest request, String username) {
        request.getSession(true).setAttribute(Constants.USERNAME_PARAMETER, username);
    }

    public static boolean hasSession(HttpServletRequest request) {
        return request.getSession(false) != null;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
