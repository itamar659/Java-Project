package logic.timeTable;

import logic.evoAlgorithm.timeTableEvolution.TimeTableProblem;
import logic.timeTable.rules.base.Rule;
import logic.timeTable.rules.base.Rules;
import logic.evoAlgorithm.base.Solution;

import java.io.Serializable;
import java.util.*;

public class TimeTable implements Solution<TimeTable>, Serializable {

    private final TimeTableProblem problem;
    private final List<Lesson> lessons;
    private Rules<TimeTable> rules;

    public List<Lesson> getLessons() {
        return lessons;
    }

    public TimeTableProblem getProblem() {
        return problem;
    }

    public Rules<TimeTable> getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
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
        Map<Teacher, List<Lesson>> teacher2lessonsList = new TreeMap<>(Comparator.comparing(Teacher::getId));
        for (Lesson lesson : lessons) {
            if (!teacher2lessonsList.containsKey(lesson.getTeacher())) {
                teacher2lessonsList.put(lesson.getTeacher(), new ArrayList<>());
            }

            teacher2lessonsList.get(lesson.getTeacher()).add(lesson);
        }

        return teacher2lessonsList;
    }

    public Map<Class, List<Lesson>> getClassesTimeTable() {
        Map<Class, List<Lesson>> teacher2lessonsList = new TreeMap<>(Comparator.comparing(Class::getId));
        for (Lesson lesson : lessons) {
            if (!teacher2lessonsList.containsKey(lesson.getaClass())) {
                teacher2lessonsList.put(lesson.getaClass(), new ArrayList<>());
            }

            teacher2lessonsList.get(lesson.getaClass()).add(lesson);
        }

        return teacher2lessonsList;
    }

    @Override
    public String toString() {
        return "TimeTableSolution{" +
                "lessons=" + lessons +
                ", rules=" + rules +
                '}';
    }
}
