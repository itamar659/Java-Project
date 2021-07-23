package logic.timeTable.rules;

import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import logic.evoAlgorithm.base.Solution;
import logic.timeTable.Lesson;

import java.util.List;

public class Knowledgeable extends Rule {

    @Override
    public String getId() {
        return "Knowledgeable";
    }

    @Override
    public float calcFitness(Solution solution) {
        List<Lesson> lessons = ((TimeTable) solution).getLessons();

        int penalty = 0;
        for (Lesson lesson : lessons) {
            if (!lesson.getTeacher().getTeachesCoursesIDs().contains(lesson.getCourse().getId())) {
                penalty++;
            }
        }

        return 1f / (1 + penalty);
    }
}
