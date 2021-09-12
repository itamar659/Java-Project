package webEngine.utils;

import webEngine.helpers.Constants;
import webEngine.users.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SessionUtils {

    // Don't allow to create an instance of this class
    private SessionUtils() { }

    public static User getUser(HttpServletRequest request) {
        return (User) request.getSession(false).getAttribute(Constants.USER_ATTRIBUTE);
    }

    public static synchronized void startSession(HttpServletRequest request, User user) {
        request.getSession(true).setAttribute(Constants.USER_ATTRIBUTE, user);
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
