package logic.Algorithm;

import logic.Algorithm.genericEvolutionAlgorithm.Rule;
import logic.Algorithm.genericEvolutionAlgorithm.Rules;
import logic.Algorithm.genericEvolutionAlgorithm.Solution;
import logic.timeTable.Lesson;

import java.util.ArrayList;
import java.util.List;

public class TimeTableSolution implements Solution {

    // All the lessons (<D,H,C,T,S>) this time table have
    private final List<Lesson> lessons;
    // The set of rules for this time table
    private Rules rules;

    public List<Lesson> getLessons() {
        return lessons;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public TimeTableSolution() {
        this.lessons = new ArrayList<>();
    }

    public TimeTableSolution(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    @Override
    public float getFitness() {
        if (this.rules == null || this.rules.getListOfRules() == null) {
            return 1;
        }

        float penalty = 0;

        for (Rule rule : this.rules.getListOfRules()) {
            float rulePenalty = rule.calcPenalty(this);
            if (rule.getType() == Rules.RULE_TYPE.HARD) {
                rulePenalty *= this.rules.getHardRuleWeight() / 100f;
            } else {
                rulePenalty *= (100f - this.rules.getHardRuleWeight()) / 100f;
            }

            penalty += rulePenalty;
        }

        return 1.0f / (1 + penalty);
    }

    @Override
    public String toString() {
        return "TimeTableSolution{" +
                "lessons=" + lessons +
                ", rules=" + rules +
                '}';
    }
}
