package logic.timeTable.rules;

import logic.algorithm.TimeTableSolution;
import logic.timeTable.rules.base.Rule;
import logic.algorithm.genericEvolutionAlgorithm.Solution;
import logic.timeTable.Lesson;
import logic.timeTable.Teacher;
import logic.validation.ValidationResult;

import java.util.*;

public class DayOffTeacher extends Rule {

    public DayOffTeacher() {
        this.setRuleName("DayOffTeacher");
    }

    @Override
    public float calcFitness(Solution solution) {
        List<Lesson> lessons = ((TimeTableSolution) solution).getLessons();
        final int DAYS_IN_WEEK = 7;

        Map<Teacher, boolean[]> teacher2daysOfWork = new TreeMap<>(Comparator.comparing(Teacher::getTeacherID));
        int penalty = 0;

        // calculate for every teacher in which days he's working.
        for (Lesson lesson : lessons) {
            // potential bug -> 3 days in a week and work in the weekend. Will solve it later.
            if (!teacher2daysOfWork.containsKey(lesson.getTeacher())) {
                boolean[] workingDays = new boolean[DAYS_IN_WEEK];
                Arrays.fill(workingDays, false);
                teacher2daysOfWork.put(lesson.getTeacher(), workingDays);
            }

            teacher2daysOfWork.get(lesson.getTeacher())[lesson.getDay()] = true;
        }

        // Check if every teacher has at least one day off
        for (boolean[] workingDays : teacher2daysOfWork.values()) {
            int freeDays = 0;
            for (boolean currentDay : workingDays) {
                if (!currentDay) {
                    freeDays++;
                }
            }

            if (freeDays == 0) {
                penalty++;
            }
        }

        return 1f / (1 + penalty);
    }

    @Override
    public ValidationResult checkValidation() {
        return new ValidationResult(true);
    }
}
