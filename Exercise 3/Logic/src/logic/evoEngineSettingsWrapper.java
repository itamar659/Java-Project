package logic;

import engine.base.Problem;
import logic.evoAlgorithm.TimeTableEvolutionEngine;
import engine.base.Crossover;
import engine.base.Mutation;
import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rules;
import engine.base.Selection;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.Teacher;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class evoEngineSettingsWrapper implements Serializable {

    private final TimeTableEvolutionEngine TTEAlgorithm;

    public final TimeTableProblem getProblem() {
        return TTEAlgorithm.getProblem();
    }

    public final Selection<TimeTable> getSelection() {
        return TTEAlgorithm.getSelection();
    }

    public final Crossover<TimeTable> getCrossover() {
        return TTEAlgorithm.getCrossover();
    }

    public int getPopulationSize() {
        return TTEAlgorithm.getPopulationSize();
    }

    public int getElitism() {return TTEAlgorithm.getElitism(); }

    public int getDays() {
        return TTEAlgorithm.getProblem().getDays();
    }

    public int getHours() {
        return TTEAlgorithm.getProblem().getHours();
    }

    public final List<Class> getClasses() {
        return TTEAlgorithm.getProblem().getClasses();
    }

    public final List<Teacher> getTeachers() {
        return TTEAlgorithm.getProblem().getTeachers();
    }

    public final List<Course> getCourses() {
        return TTEAlgorithm.getProblem().getCourses();
    }

    public final Rules<TimeTable> getRules() {
        return TTEAlgorithm.getProblem().getRules();
    }

    public evoEngineSettingsWrapper(TimeTableEvolutionEngine TTEAlgorithm) {
        this.TTEAlgorithm = TTEAlgorithm;
    }

    public final Course findCourse(String courseID) {
        for (Course currentCourse : TTEAlgorithm.getProblem().getCourses()) {
            if (currentCourse.getId().equals(courseID)) {
                return currentCourse;
            }
        }

        return null;
    }
}
