package logic.timeTable.rules;

import logic.algorithm.TimeTableSolution;
import logic.algorithm.genericEvolutionAlgorithm.Parameterizable;
import logic.timeTable.rules.base.Rule;
import logic.algorithm.genericEvolutionAlgorithm.Solution;
import logic.timeTable.Course;
import logic.timeTable.Lesson;
import logic.validation.ValidationResult;

import java.util.*;

public class Sequentiality extends Rule implements Parameterizable {

    private int totalHours;

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public Sequentiality() {
        this.setRuleName("Sequentiality");
    }

    @Override
    public float calcFitness(Solution solution) {
        final int DAYS = 7;
        final int HOURS = 24;
        List<Lesson> lessons = ((TimeTableSolution) solution).getLessons();

        Map<Course, boolean[][]> course2schedule = new TreeMap<>(Comparator.comparing(Course::getCourseID));
        int penalty = 0;

        // create the schedule for a specific course
        for (Lesson lesson : lessons) {
            if (!course2schedule.containsKey(lesson.getCourse())) {
                boolean[][] courseSchedule = new boolean[DAYS][HOURS];
                for (boolean[] daySchedule : courseSchedule) {
                    Arrays.fill(daySchedule, false);
                }
                course2schedule.put(lesson.getCourse(), courseSchedule);
            }

            course2schedule.get(lesson.getCourse())[lesson.getDay()][lesson.getHour()] = true;
        }

        // Check if every teacher has at least one free day
        for (boolean[][] courseSchedule : course2schedule.values()) {
            for (boolean[] currentDay : courseSchedule) {
                int hoursInARow = 0;
                for (boolean currentHour : currentDay) {
                    if (currentHour) { // currentHour == true means the course is taught at this hour
                        hoursInARow++;
                    }

                    if (hoursInARow > totalHours) {
                        penalty++;
                    }
                }
            }
        }

        return 1f / (1 + penalty);
    }

    @Override
    public ValidationResult checkValidation() {
        if (totalHours < 1) {
            return new ValidationResult(false, "total hours has to be a positive value");
        }

        return new ValidationResult(true);
    }

    @Override
    public void setValue(String parameterName, Object value) {
        if (parameterName.equals("totalHours")) {
            setTotalHours((int) value);
        }else {
            throw new IllegalArgumentException("Not found parameter name in" + this.getClass().getSimpleName());
        }
    }

    @Override
    public Object getValue(String parameterName) {
        if (parameterName.equals("totalHours")) {
            return getTotalHours();
        }else {
            throw new IllegalArgumentException("Not found parameter name in" + this.getClass().getSimpleName());
        }
    }
}
