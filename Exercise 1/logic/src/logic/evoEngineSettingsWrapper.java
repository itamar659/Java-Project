package logic;

import logic.evoAlgorithm.timeTableEvolution.TimeTableEvolutionEngine;
import logic.evoAlgorithm.base.Crossover;
import logic.evoAlgorithm.base.Mutation;
import logic.timeTable.rules.base.Rules;
import logic.evoAlgorithm.base.Selection;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.Teacher;

import java.util.List;
import java.util.Set;

public class evoEngineSettingsWrapper {

    private final TimeTableEvolutionEngine TTEAlgorithm;

    public Selection getSelection() {
        return TTEAlgorithm.getSelection();
    }

    public Crossover getCrossover() {
        return TTEAlgorithm.getCrossover();
    }

    public Set<Mutation> getMutations() {
        return TTEAlgorithm.getMutations();
    }

    public int getPopulationSize() {
        return TTEAlgorithm.getPopulationSize();
    }

    public int getDays() {
        return TTEAlgorithm.getProblem().getDays();
    }

    public int getHours() {
        return TTEAlgorithm.getProblem().getHours();
    }

    public List<Class> getClasses() {
        return TTEAlgorithm.getProblem().getClasses();
    }

    public List<Teacher> getTeachers() {
        return TTEAlgorithm.getProblem().getTeachers();
    }

    public List<Course> getCourses() {
        return TTEAlgorithm.getProblem().getCourses();
    }

    public Rules getRules() {
        return TTEAlgorithm.getProblem().getRules();
    }

    public evoEngineSettingsWrapper(TimeTableEvolutionEngine TTEAlgorithm) {
        this.TTEAlgorithm = TTEAlgorithm;
    }

    public Course findCourse(String courseID) {
        for (Course currentCourse : TTEAlgorithm.getProblem().getCourses()) {
            if (currentCourse.getId().equals(courseID)) {
                return currentCourse;
            }
        }

        return null;
    }
}
