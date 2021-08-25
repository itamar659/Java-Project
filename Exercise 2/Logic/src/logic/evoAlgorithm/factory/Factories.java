package logic.evoAlgorithm.factory;

import engine.base.Selection;

public class Factories {
    private final CrossoverFactory crossoverFactory = new CrossoverFactory();
    private final MutationFactory mutationFactory = new MutationFactory();
    private final RuleFactory ruleFactory = new RuleFactory();
    private final SelectionFactory selectionFactory = new SelectionFactory();

    public CrossoverFactory getCrossoverFactory() {
        return crossoverFactory;
    }

    public MutationFactory getMutationFactory() {
        return mutationFactory;
    }

    public RuleFactory getRuleFactory() {
        return ruleFactory;
    }

    public SelectionFactory getSelectionFactory() {
        return selectionFactory;
    }

}
