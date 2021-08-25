package logic.evoAlgorithm.configurable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private final Map<String, String> parameters = new HashMap<>();
    private final ReadOnlyConfiguration readOnlyConfiguration;

    public Map<String,String> getParameters() { return this.parameters; }

    @SafeVarargs
    public Configuration(Map.Entry<String, String>... params) {
        Arrays.stream(params).forEach(pair -> {
            parameters.put(pair.getKey(), pair.getValue());
        });

        readOnlyConfiguration = new ReadOnlyConfiguration(this);
    }

    public String getParameter(String paramName) {
        if (isValidParamName(paramName)) {
            return parameters.get(paramName);
        }
        return null;
    }

    public void setParameter(String paramName, String paramVal) {
        if (isValidParamName(paramName)) {
            parameters.put(paramName, paramVal);
        }
    }

    public int countParameters() {
        return parameters.size();
    }

    private boolean isValidParamName(String paramName) {
        if (!parameters.containsKey(paramName)) {
            throw new IllegalArgumentException(String.format("Invalid parameter name - %s.%n", paramName));
            //System.out.printf("Invalid parameter name - %s.%n", paramName);
            //return false;
        }

        return true;
    }

    public ReadOnlyConfiguration getProxy() {
        return readOnlyConfiguration;
    }
}
