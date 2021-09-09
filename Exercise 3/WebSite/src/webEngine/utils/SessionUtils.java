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

    public static synchronized void startSession(HttpServletRequest request, String username) {
        request.getSession(true).setAttribute(Constants.USERNAME_PARAMETER, username);
    }

    public static synchronized boolean hasSession(HttpServletRequest request) {
        return request.getSession(false) != null;
    }

    public static synchronized void endSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }

    public static void setAttribute(HttpServletRequest request, String s, Object o) {
        request.getSession(false).setAttribute(s, o);
    }

    public static Object getAttribute(HttpServletRequest request, String s) {
        return request.getSession(false).getAttribute(s);
    }
}
