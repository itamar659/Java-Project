package logic.timeTable;

import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.rules.base.Rule;
import logic.timeTable.rules.base.Rules;
import engine.base.Solution;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class TimeTable implements Solution<TimeTable>, Serializable {

    private final TimeTableProblem problem;
    private final List<Lesson> lessons;
    private Rules<TimeTable> rules;

    public final List<Lesson> getLessons() {
        return lessons;
    }

    public final TimeTableProblem getProblem() {
        return problem;
    }

    public final Rules<TimeTable> getRules() {
        return rules;
    }

    public void setRules(Rules<TimeTable> rules) {
        this.rules = rules;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    public TimeTable(TimeTableProblem problem) {
        this.problem = problem;
        this.lessons = new ArrayList<>();
    }

    @Override
    public float getFitness() {
        if (this.rules == null || this.rules.getListOfRules() == null) {
            return 1;
        }

        float softFitness = 0;
        float hardFitness = 0;
        int softRules = 0;
        int hardRules = 0;
        float hardRatio = rules.getHardRuleWeight() / 100f;
        float softRatio = 1 - hardRatio;

        for (Rule<TimeTable> rule : this.rules.getListOfRules()) {
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
    public TimeTable getGens() {
        return this;
    }

    @Override
    public Solution<TimeTable> createChild() {
        TimeTable child = new TimeTable(this.problem);
        child.setRules(this.rules);
        return child;
    }

    public float getAvgFitness(Rules.RULE_TYPE type) {
        float total = 0;
        int counted = 0;
        for (Rule<TimeTable> rule : this.rules.getListOfRules()) {
            if (rule.getType() == type) {
                counted++;
                total += rule.calcFitness(this);
            }
        }

        return counted > 0 ?total / counted : 1;
    }

    public Map<Teacher, List<Lesson>> getTeachersTimeTable() {
        return  getSpecificTimeTable(Lesson::getTeacher);
    }

    public Map<Class, List<Lesson>> getClassesTimeTable() {
        return  getSpecificTimeTable(Lesson::getaClass);
    }

    private <T extends HasId>Map<T, List<Lesson>> getSpecificTimeTable(Function<Lesson, T> TMethod) {
        List<Lesson> lessons = new ArrayList<>(this.lessons);
        lessons.sort(Lesson::compareByDHCTS);
        Map<T, List<Lesson>> identifier2lessonsList = new TreeMap<>(T::compareByID);
        T funcRtnObj;
        for (Lesson lesson : lessons) {
            funcRtnObj = TMethod.apply(lesson);
            if (!identifier2lessonsList.containsKey(funcRtnObj)) {
                identifier2lessonsList.put(funcRtnObj, new ArrayList<>());
            }

            identifier2lessonsList.get(funcRtnObj).add(lesson);
        }

        return identifier2lessonsList;
    }

    @Override
    public String toString() {
        return "TimeTableSolution{" +
                "lessons=" + lessons +
                ", rules=" + rules +
                '}';
    }
}
