package logic.timeTable;

import logic.Algorithm.TimeTableSolution;
import logic.timeTable.rules.base.Rules;

import java.util.ArrayList;
import java.util.List;

public class TimeTable {

    // TODO: Will be a wrapper to TimeTableSolution class. To represent a single solution. (Not the problem parameters)

    // Need to have it for the wrapper, and for printing the results (print for each rule the solution score)
    // TODO: NEED TO REMOVE THIS. TimeTable Package SHOULDN'T KNOW ABOUT Algorithm Package!!!!!!!!!!!!!!!!!!!!!!!!!!
    //  But the Algorithm knows about TimeTable.
    private TimeTableSolution thisTimeTable;

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

    public TimeTable() {
        this.lessons = new ArrayList<>();
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }

    @Override
    public String toString() {
        return "TimeTableSolution{" +
                "lessons=" + lessons +
                ", rules=" + rules +
                '}';
    }
}
