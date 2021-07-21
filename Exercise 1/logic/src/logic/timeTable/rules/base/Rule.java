package logic.timeTable.rules.base;

import logic.algorithm.genericEvolutionAlgorithm.Solution;
import logic.validation.Validateable;

public abstract class Rule implements Validateable {

    private Rules.RULE_TYPE type;
    private String ruleName;

    public String getRuleName() {
        return ruleName;
    }

    protected void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Rules.RULE_TYPE getType() {
        return type;
    }

    public void setType(Rules.RULE_TYPE type) {
        this.type = type;
    }

    public abstract float calcFitness(Solution solution);

    @Override
    public String toString() {
        return "Rule{" +
                "type=" + type +
                ", ruleID='" + ruleName + '\'' +
                '}';
    }
}
