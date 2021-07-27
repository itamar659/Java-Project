package logic.timeTable.rules;

import logic.evoAlgorithm.base.Solution;
import logic.evoAlgorithm.timeTableEvolution.TimeTableProblem;
import logic.timeTable.Class;
import logic.timeTable.Lesson;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Satisfactory extends Rule<TimeTable> {

    @Override
    public String getId() {
        return "Satisfactory";
    }

    @Override
    public float calcFitness(Solution<TimeTable> solution) {
        List<Lesson> lessons = solution.getGens().getLessons();
        TimeTableProblem problem = solution.getGens().getProblem();
        Map<Class, Map<String, Integer>> class2course2hours = new HashMap<>();

        int penalty = 0;
        int max = problem.getClasses().stream().mapToInt(Class::getTotalHours).sum();
        for (Lesson lesson : lessons) {
            if (!class2course2hours.containsKey(lesson.getaClass())) {
                class2course2hours.put(lesson.getaClass(), new HashMap<>());
            }

            Map<String, Integer> course2hours = class2course2hours.get(lesson.getaClass());
            if (!course2hours.containsKey(lesson.getCourse().getId())) {
                course2hours.put(lesson.getCourse().getId(), 0);
            }

            course2hours.put(lesson.getCourse().getId(), course2hours.get(lesson.getCourse().getId()) + 1);
        }

        for (Class aclass : problem.getClasses()) {
            if (!class2course2hours.containsKey(aclass)) {
                for (Map.Entry<String, Integer> cClass : aclass.getCourseID2Hours().entrySet()) {
                    penalty += cClass.getValue();
                }

                continue;
            }

            Map<String, Integer> courseID2hours = class2course2hours.get(aclass);
            for (Map.Entry<String, Integer> requiredCourse2hours : aclass.getCourseID2Hours().entrySet()) {
                if (!courseID2hours.containsKey(requiredCourse2hours.getKey())) {
                    penalty += requiredCourse2hours.getValue();
                } else if (requiredCourse2hours.getValue() > courseID2hours.get(requiredCourse2hours.getKey())) {
                    penalty += requiredCourse2hours.getValue() - courseID2hours.get(requiredCourse2hours.getKey());
                } else {
                    penalty += (courseID2hours.get(requiredCourse2hours.getKey()) - requiredCourse2hours.getValue());
                }
            }

            for (Map.Entry<String, Integer> courses : courseID2hours.entrySet()) {
                if (!aclass.getCourseID2Hours().containsKey(courses.getKey())) {
                    penalty += courses.getValue();
                }
            }
        }

        if (penalty > max) {
            return 0;
        }

        return (float) Math.pow((max - penalty) / ((double) max), 4);
    }
}
