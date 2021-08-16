package logic.timeTable.rules;

import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.Class;
import logic.timeTable.HasId;
import logic.timeTable.TimeTable;
import logic.schema.Parameterizable;
import logic.timeTable.rules.base.Rule;
import engine.base.Solution;
import logic.timeTable.Course;
import logic.timeTable.Lesson;

import java.util.*;
import java.util.stream.Collectors;

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
    public void setValue(String parameterName, Object value) {
        if (parameterName.equals("TotalHours")) {
            setTotalHours(Integer.parseInt(value.toString()));
        }else {
            throw new IllegalArgumentException("Not found parameter name in" + this.getClass().getSimpleName());
        }
    }

    @Override
    public Object getValue(String parameterName) {
        if (parameterName.equals("TotalHours")) {
            return getTotalHours();
        }else {
            throw new IllegalArgumentException("Not found parameter name in" + this.getClass().getSimpleName());
        }
    }

    @Override
    public float calcFitness(Solution<TimeTable> solution) {
        List<Lesson> lessons = new ArrayList<>(solution.getGens().getLessons());
        TimeTableProblem problem = solution.getGens().getProblem();


        final int[] penalty = {0};
        int max = problem.getDays() * problem.getClasses().size() * problem.getCourses().size();

        problem.getClasses().forEach(curClass -> {

            // Init new mapping from course to schedule for the current class
            Map<Course, boolean[][]> course2schedule = new HashMap<>();
            problem.getCourses().forEach(course -> {
                course2schedule.put(course, new boolean[problem.getDays()][problem.getHours()]);
            });

            // Fill the schedule for the current class
            lessons.stream()
                    .filter(l -> l.getaClass().getId().equals(curClass.getId()))
                    .forEach(lesson -> {
                        course2schedule.get(lesson.getCourse())[lesson.getDay()][lesson.getHour()] = true;
                    });

            // Calculate the sequentially for each course in this class
            course2schedule.forEach((course, schedule) -> {
                for (boolean[] currentDay : schedule) {
                    int hoursInARow = 0;
                    for (boolean currentHour : currentDay) {
                        if (currentHour) {
                            hoursInARow++;
                        } else {
                            hoursInARow = 0;
                        }

                        if (hoursInARow >= this.getTotalHours()) {
                            penalty[0]++;
                        }
                    }
                }
            });
        });

        return ((max - penalty[0]) / (float)max) * MAX_PERCENTAGE;
    }
}
