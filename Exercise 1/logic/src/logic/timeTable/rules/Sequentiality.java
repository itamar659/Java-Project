package logic.timeTable.rules;

import logic.evoAlgorithm.timeTableEvolution.TimeTableProblem;
import logic.timeTable.TimeTable;
import logic.schema.Parameterizable;
import logic.timeTable.rules.base.Rule;
import logic.evoAlgorithm.base.Solution;
import logic.timeTable.Course;
import logic.timeTable.Lesson;

import java.util.*;

public class Sequentiality extends Rule<TimeTable> implements Parameterizable {

    @Override
    public String getId() {
        return "Sequentiality";
    }

    private int totalHours;

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    @Override
    public float calcFitness(Solution<TimeTable> solution) {
        List<Lesson> lessons = solution.getGens().getLessons();
        TimeTableProblem problem = solution.getGens().getProblem();

        Map<Course, boolean[][]> course2schedule = course2teachTimeTable(problem.getDays(), problem.getHours(), lessons);
        int penalty = 0;

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

    private Map<Course, boolean[][]> course2teachTimeTable(int days, int hours, List<Lesson> lessons) {
        Map<Course, boolean[][]> course2schedule = new TreeMap<>(Comparator.comparing(Course::getId));

        for (Lesson lesson : lessons) {
            if (!course2schedule.containsKey(lesson.getCourse())) {
                boolean[][] courseSchedule = new boolean[days][hours];
                for (boolean[] daySchedule : courseSchedule) {
                    Arrays.fill(daySchedule, false);
                }
                course2schedule.put(lesson.getCourse(), courseSchedule);
            }
            course2schedule.get(lesson.getCourse())[lesson.getDay()][lesson.getHour()] = true;
        }

        return course2schedule;
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
