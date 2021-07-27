package logic.timeTable.rules;

import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import engine.base.Solution;
import logic.timeTable.Lesson;
import logic.timeTable.Class;

import java.util.*;

public class DayOffClass extends Rule<TimeTable> {

    @Override
    public String getId() {
        return "DayOffClass";
    }

    @Override
    public float calcFitness(Solution<TimeTable> solution) {
        List<Lesson> lessons = solution.getGens().getLessons();
        final int DAYS_IN_WEEK = 7;

        Map<Class, boolean[]> class2daysOfStudy = new TreeMap<>(Comparator.comparing(Class::getId));
        int penalty = 0;

        // calculate for every class in which days they study.
        for (Lesson lesson : lessons) {
            // potential bug -> 3 days in a week and work in the weekend. Will solve it later.
            if (!class2daysOfStudy.containsKey(lesson.getaClass())) {
                boolean[] studyingDays = new boolean[DAYS_IN_WEEK];
                Arrays.fill(studyingDays, false);
                class2daysOfStudy.put(lesson.getaClass(), studyingDays);
            }

            class2daysOfStudy.get(lesson.getaClass())[lesson.getDay()] = true;
        }

        // Check if every class has at least one day off
        for (boolean[] studyingDays : class2daysOfStudy.values()) {
            int freeDays = 0;
            for (boolean currentDay : studyingDays) {
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
}
