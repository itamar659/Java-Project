package logic.Algorithm.genericEvolutionAlgorithm;

public abstract class Rule {

    private Rules.RULE_TYPE type;
    private String ruleID;

    public String getRuleID() {
        return ruleID;
    }

    public void setRuleID(String ruleID) {
        this.ruleID = ruleID;
    }

    public Rules.RULE_TYPE getType() {
        return type;
    }

    public void setType(Rules.RULE_TYPE type) {
        this.type = type;
    }

    public abstract int calcPenalty(Solution solution);

    @Override
    public String toString() {
        return "Rule{" +
                "type=" + type +
                ", ruleID='" + ruleID + '\'' +
                '}';
    }
}
