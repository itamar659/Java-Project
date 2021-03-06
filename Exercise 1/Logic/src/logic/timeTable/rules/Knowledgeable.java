package logic.timeTable.rules;

import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import engine.base.Solution;
import logic.timeTable.Lesson;

import java.util.ArrayList;
import java.util.List;

public class Knowledgeable extends Rule<TimeTable> {

    @Override
    public String getId() {
        return "Knowledgeable";
    }

    @Override
    public float calcFitness(Solution<TimeTable> solution) {
        List<Lesson> lessons = new ArrayList<>(solution.getGens().getLessons());

        int penalty = 0;
        int max = lessons.size();
        for (Lesson lesson : lessons) {
            if (!lesson.getTeacher().getTeachesCoursesIDs().contains(lesson.getCourse().getId())) {
                penalty++;
            }
        }

        if (max == 0) {
            return 0;
        }

        return (float) Math.pow((max - penalty) / ((double) max), 3) * MAX_PERCENTAGE;
    }
}
