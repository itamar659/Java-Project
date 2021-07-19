package logic.Algorithm.mutations;

import logic.Algorithm.genericEvolutionAlgorithm.Mutation;

public class MutationFactory {

    private MutationFactory() {
    }

    public static Mutation getMutationOperator(String mutationName) {
        if (mutationName.equals(Flipping.class.getSimpleName())) {
            return new Flipping();
        }

        return null;
    }
}
