package logic.timeTable.rules;

import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import engine.base.Solution;
import logic.timeTable.Lesson;
import logic.timeTable.Teacher;

import java.util.*;

public class DayOffTeacher extends Rule<TimeTable> {

    @Override
    public String getId() {
        return "DayOffTeacher";
    }

    @Override
    public float calcFitness(Solution<TimeTable> solution) {
        List<Lesson> lessons = new ArrayList<>(solution.getGens().getLessons());
        TimeTableProblem problem = solution.getGens().getProblem();

        int penalty = 0;
        int max = 0;
        for (Teacher teacher : problem.getTeachers()) {
            int workingDays = (int) lessons.stream()
                    .filter(t -> t.getTeacher().equals(teacher))
                    .mapToInt(Lesson::getDay)
                    .distinct()
                    .count();

            if (workingDays > 0) {
                penalty += problem.getDays() - workingDays;
                max += problem.getDays();
            }
        }

        if (max == 0) {
            return 0;
        }

        return (max - penalty) / ((float) max);
    }
}
