package logic.Algorithm.factory;

import logic.Algorithm.mutations.Flipping;

public class MutationFactory {

    private MutationFactory() {
    }

    // configuration is of the following form: first dimension gets a parameter and his value.
    //  second dimension have 2 values, [0] parameter name, [1] parameter value
    public static FactoryResult createMutation(String mutationName, double probability, String[][] configuration) {
        FactoryResult returnObject = new FactoryResult(true, String.format("Couldn't find the mutation named: %s", mutationName));
        if (mutationName.equals(Flipping.class.getSimpleName())) {
            returnObject = Factory.setParameterizableParameters(new Flipping(), configuration);
        }

        return returnObject;
    }
}
