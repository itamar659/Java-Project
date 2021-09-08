package webEngine.utils;

import webEngine.users.UserManager;

import javax.servlet.ServletContext;

public final class ServletUtils {

    // Don't allow to create an instance of this class
    private ServletUtils() { }

    // ServletContext attributes identifiers;
    private static final String USER_MANAGER_ATTRIBUTE = "userManager";
    private static final String ENGINE_MANAGER_ATTRIBUTE = "engineManager";

    // Locks for synchronization
    private static final Object userManagerLock = new Object();
    private static final Object engineManagerLock = new Object();

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

    public static EngineManager getEngineManager(ServletContext servletContext) {
        // Double check locking - if engine manager exists
        if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE) == null) {
            synchronized (engineManagerLock) {
                if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE) == null) {
                    servletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE, new EngineManager());
                }
            }
        }
        return (EngineManager) servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE);
    }
}
