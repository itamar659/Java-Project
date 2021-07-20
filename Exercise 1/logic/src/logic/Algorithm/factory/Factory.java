package logic.Algorithm.factory;

import logic.Algorithm.genericEvolutionAlgorithm.Parameterizable;

// TODO: Wtf is this class? create a whole class a single method? Try something else...
//  Works for now.
public class Factory {

    private Factory() {
    }

    public static FactoryResult setParameterizableParameters(Parameterizable mutation, String[][] configuration) {
        if (configuration != null) {
            for (String[] parameterNameValue : configuration) {
                String paramName = parameterNameValue[0];
                String paramValue = parameterNameValue[1];

                try {
                    mutation.setValue(paramName, paramValue);

                } catch (IllegalArgumentException e) {
                    return new FactoryResult(true,
                            String.format("parameter value: %s error: %s", paramName, e.getMessage()));
                } catch (ClassCastException e) {
                    return new FactoryResult(true,
                            String.format("parameter value: %s, doesn't except value types like: %s", paramName, paramValue));
                }
            }
        }

        return new FactoryResult(mutation);
    }
}
