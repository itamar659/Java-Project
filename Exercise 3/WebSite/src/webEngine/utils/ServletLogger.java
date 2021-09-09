package webEngine.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class ServletLogger {

    private ServletLogger() { }

    private static final Object loggerCreationLock = new Object();

    private static Logger logger = null;

    public static Logger getLogger() {
        if (logger == null) {
            synchronized (loggerCreationLock) {
                if (logger == null) {
                    logger = Logger.getLogger(ServletLogger.class.getName());

                    ConsoleHandler handler = new ConsoleHandler();
                    handler.setFormatter(new SimpleFormatter());
                    System.out.println(logger.getHandlers().length);

                    logger.addHandler(handler);
                    logger.setLevel(Level.FINE);
                }
            }
        }
        return logger;
    }
}
