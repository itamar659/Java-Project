package logic.timeTable.rules;

import engine.configurable.Configurable;
import engine.configurable.Configuration;
import engine.configurable.ReadOnlyConfiguration;
import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import engine.base.Solution;
import logic.timeTable.Course;
import logic.timeTable.Lesson;

import java.util.*;

public class Sequentiality extends Rule<TimeTable> implements Configurable {

    private static final String PARAMETER_TOTAL_HOURS = "TotalHours";

    private final Configuration configuration;

    @Override
    public ReadOnlyConfiguration getConfiguration() {
        return configuration.getProxy();
    }

    @Override
    public void setParameter(String parameterName, String value) {
        if (parameterName.equals(PARAMETER_TOTAL_HOURS)) {
            Integer.parseInt(value);
        }else {
            throw new IllegalArgumentException("Not found parameter name in" + this.getClass().getSimpleName());
        }

        configuration.setParameter(parameterName, value);
    }

    @Override
    public String getConfigurableName() {
        return "Sequentiality";
    }

    @Override
    public String getId() {
        return "Sequentiality";
    }

    public int getTotalHours() {
        return Integer.parseInt(configuration.getParameter(PARAMETER_TOTAL_HOURS));
    }

    public Sequentiality() {
        configuration = new Configuration(
                new AbstractMap.SimpleEntry<>(PARAMETER_TOTAL_HOURS, "0")
        );
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
