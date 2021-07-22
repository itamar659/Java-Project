package logic.timeTable.rules.base;

import logic.evoAlgorithm.base.Solution;
import logic.timeTable.HasId;

import java.util.Objects;

public abstract class Rule implements HasId {

    private Rules.RULE_TYPE type;
    private String id;

    @Override
    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
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
                ", ruleID='" + id + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule rule = (Rule) o;
        return Objects.equals(id, rule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
