package logic.timeTable.rules.base;

import engine.base.Solution;
import logic.timeTable.HasId;

import java.io.Serializable;
import java.util.Objects;

public abstract class Rule<T> implements HasId, Serializable {

    protected static final float MAX_PERCENTAGE = 100;

    private Rules.RULE_TYPE type;

    public Rules.RULE_TYPE getType() {
        return type;
    }

    public void setType(Rules.RULE_TYPE type) {
        this.type = type;
    }

    public abstract float calcFitness(Solution<T> solution);

    @Override
    public String toString() {
        return "Rule{" +
                "type=" + type +
                ", ruleID='" + getId() + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rule<?> rule = (Rule<?>) o;
        return getId().equals(rule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
