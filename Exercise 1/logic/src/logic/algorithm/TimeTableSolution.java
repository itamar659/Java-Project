package logic.algorithm;

import logic.timeTable.Class;
import logic.timeTable.rules.base.Rule;
import logic.timeTable.rules.base.Rules;
import logic.algorithm.genericEvolutionAlgorithm.Solution;
import logic.timeTable.Lesson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeTableSolution implements Solution {

    // All the lessons (<D,H,C,T,S>) this time table have
    private final List<Lesson> lessons;
    // The set of rules for this time table
    private Rules rules;

    private final TimeTableProblem problem;

    public List<Lesson> getLessons() {
        return lessons;
    }

    public TimeTableProblem getProblem() {
        return problem;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public TimeTableSolution(TimeTableProblem problem) {
        this.problem = problem;
        this.lessons = new ArrayList<>();
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    @Override
    public float getFitness() {
        if (!checkRequirements()) {
            return 0;
        }

        if (this.rules == null || this.rules.getListOfRules() == null) {
            return 1;
        }

        float softFitness = 0;
        float hardFitness = 0;
        int softRules = 0;
        int hardRules = 0;
        float hardRatio = rules.getHardRuleWeight() / 100f;
        float softRatio = 1 - hardRatio;

        for (Rule rule : this.rules.getListOfRules()) {
            if (rule.getType() == Rules.RULE_TYPE.HARD) {
                hardFitness += rule.calcFitness(this);
                hardRules++;
            } else {
                softFitness += rule.calcFitness(this);
                softRules++;
            }
        }

        float fitnessSum = (softFitness * softRatio) + (hardFitness * hardRatio);

        return fitnessSum / ((softRules * softRatio) + (hardRules * hardRatio));
    }

    @Override
    public String toString() {
        return "TimeTableSolution{" +
                "lessons=" + lessons +
                ", rules=" + rules +
                '}';
    }

    private boolean checkRequirements() {
        Map<Class, Map<String, Integer>> class2course2hours = new HashMap<>();

        for (Lesson lesson : lessons) {

            // Check if this class study this course at all
            if (!lesson.getaClass().getCourseID2Hours().containsKey(lesson.getCourse().getCourseID())) {
                return false;
            }

            if (!class2course2hours.containsKey(lesson.getaClass())) {
                Map<String, Integer> course2hours = new HashMap<>();
                class2course2hours.put(lesson.getaClass(), course2hours);
            }

            Map<String, Integer> course2hours = class2course2hours.get(lesson.getaClass());
            if (!course2hours.containsKey(lesson.getCourse().getCourseID())) {
                course2hours.put(lesson.getCourse().getCourseID(), 0);
            }

            course2hours.put(lesson.getCourse().getCourseID(), course2hours.get(lesson.getCourse().getCourseID()) + 1);
        }

        for (Class aclass : problem.getClasses()) {
            if (!class2course2hours.containsKey(aclass)) {
                return false;
            }

            Map<String, Integer> courseID2hours = class2course2hours.get(aclass);
            for (Map.Entry<String, Integer> currentKey : aclass.getCourseID2Hours().entrySet()) {
                if (!courseID2hours.containsKey(aclass.getClassID())) {
                    return false;
                }

                if (courseID2hours.get(aclass.getClassID()).compareTo(currentKey.getValue()) == 0) {
                    return false;
                }
            }
        }

        return true;
    }
}
