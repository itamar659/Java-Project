package logic.timeTable.rules.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Rules<T> implements Serializable {

    public enum RULE_TYPE {
        SOFT, HARD;
    }

    private final List<Rule<T>> rules;
    private int hardRuleWeight;

    public final List<Rule<T>> getListOfRules() {
        return rules;
    }

    public int getHardRuleWeight() {
        return hardRuleWeight;
    }

    public void setHardRuleWeight(int hardRuleWeight) {
        this.hardRuleWeight = hardRuleWeight;
    }

    public Rules() {
        this.rules = new ArrayList<>();
    }

    public void addRule(Rule<T> rule) {
        this.rules.add(rule);
    }

    @Override
    public String toString() {
        return "Rules{" +
                "rules=" + rules +
                ", hardRuleWeight=" + hardRuleWeight +
                '}';
    }
}
