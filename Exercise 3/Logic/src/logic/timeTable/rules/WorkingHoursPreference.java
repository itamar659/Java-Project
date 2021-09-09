package logic.timeTable.rules;

import logic.timeTable.Lesson;
import logic.timeTable.Teacher;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import engine.base.Solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkingHoursPreference extends Rule<TimeTable> {

    @Override
    public String getId() {
        return "WorkingHoursPreference";
    }

    @Override
    public float calcFitness(Solution<TimeTable> solution) {
        List<Lesson> lessons = new ArrayList<>(solution.getGens().getLessons());

        int max = 0;
        int penalty;

        // build a map from teacher id to live working hours
        Map<String, Integer> teacherID2workingHours = new HashMap<>();
        for (Teacher teacher1 : solution.getGens().getProblem().getTeachers()) {
            teacherID2workingHours.put(teacher1.getId(), 0);
            max += teacher1.getWorkingHours();
        }

        lessons.forEach(lesson -> {
            int workingHours = teacherID2workingHours.get(lesson.getTeacher().getId());
            teacherID2workingHours.put(lesson.getTeacher().getId(), workingHours + 1);
        });

        // compare each teacher to his id by the map. see for match.
        penalty = solution.getGens().getProblem().getTeachers().stream()
                .mapToInt(teacher -> Math.abs(teacher.getWorkingHours() - teacherID2workingHours.get(teacher.getId())))
                .sum();

        if (penalty > max || max == 0) {
            return 0;
        }

        return ((float) (max - penalty) / max) * MAX_PERCENTAGE;
    }
}
