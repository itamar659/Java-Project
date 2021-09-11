package logic.evoAlgorithm.factory;

public final class Factories {

    private Factories() { }

    private static final CrossoverFactory crossoverFactory = new CrossoverFactory();
    private static final MutationFactory mutationFactory = new MutationFactory();
    private static final RuleFactory ruleFactory = new RuleFactory();
    private static final SelectionFactory selectionFactory = new SelectionFactory();

    public static CrossoverFactory getCrossoverFactory() {
        return crossoverFactory;
    }

    public static MutationFactory getMutationFactory() {
        return mutationFactory;
    }

    public static RuleFactory getRuleFactory() {
        return ruleFactory;
    }

    public static SelectionFactory getSelectionFactory() {
        return selectionFactory;
    }

}
