package logic.timeTable.rules;

import logic.evoAlgorithm.timeTableEvolution.TimeTableProblem;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import logic.evoAlgorithm.base.Solution;
import logic.timeTable.Lesson;
import logic.timeTable.Teacher;

import java.util.*;

public class DayOffTeacher extends Rule {

    @Override
    public String getId() {
        return "DayOffTeacher";
    }

    @Override
    public float calcFitness(Solution solution) {
        List<Lesson> lessons = ((TimeTable) solution).getLessons();
        TimeTableProblem problem = ((TimeTable) solution).getProblem();

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

        return (max - penalty) / ((float) max);
    }
}
