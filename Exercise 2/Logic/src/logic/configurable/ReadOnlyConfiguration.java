package logic.configurable;

import java.util.HashMap;
import java.util.Map;

// A class to hide the setter for parameters value.
// Only instances of Configurable would be able to set the parameters.
public class ReadOnlyConfiguration {

    private final Configuration configuration;

    public ReadOnlyConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Map<String, String> getConfiguration() { return configuration.getConfiguration(); }

    public int countParameters() {
        return configuration.countParameters();
    }

    public String getParameter(String paramName) {
        return configuration.getParameter(paramName);
    }
}
