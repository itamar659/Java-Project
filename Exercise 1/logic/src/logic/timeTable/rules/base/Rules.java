package logic.timeTable.rules.base;

import logic.timeTable.rules.base.Rule;

import java.util.ArrayList;
import java.util.List;

public class Rules {

    public enum RULE_TYPE {
        SOFT, HARD;
    }

    private List<Rule> rules;
    private int hardRuleWeight;

    public List<Rule> getListOfRules() {
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

    public void addRule(Rule rule) {
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
