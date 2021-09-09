package webEngine.utils;

import webEngine.users.UserManager;

import javax.servlet.ServletContext;

public final class ServletUtils {

    // Don't allow to create an instance of this class
    private ServletUtils() { }

    // ServletContext attributes identifiers;
    private static final String USER_MANAGER_ATTRIBUTE = "userManager";
    private static final String PROBLEM_MANAGER_ATTRIBUTE = "problemManager";

    // Locks for synchronization
    private static final Object userManagerLock = new Object();
    private static final Object problemManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {
        // Double check locking - if user manager exists
        if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE) == null) {
            synchronized (userManagerLock) {
                if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE) == null) {
                    servletContext.setAttribute(USER_MANAGER_ATTRIBUTE, new UserManager());
                }
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE);
    }

    public static ProblemManager getProblemManager(ServletContext servletContext) {
        // Double check locking - if problem manager exists
        if (servletContext.getAttribute(PROBLEM_MANAGER_ATTRIBUTE) == null) {
            synchronized (problemManagerLock) {
                if (servletContext.getAttribute(PROBLEM_MANAGER_ATTRIBUTE) == null) {
                    servletContext.setAttribute(PROBLEM_MANAGER_ATTRIBUTE, new ProblemManager());
                }
            }
        }
        return (ProblemManager) servletContext.getAttribute(PROBLEM_MANAGER_ATTRIBUTE);
    }
}
