package logic.timeTable.rules;

import logic.algorithm.TimeTableSolution;
import logic.timeTable.rules.base.Rule;
import logic.algorithm.genericEvolutionAlgorithm.Solution;
import logic.timeTable.Lesson;
import logic.validation.ValidationResult;

import java.util.List;

public class Knowledgeable extends Rule {

    public Knowledgeable() {
        this.setRuleName("Knowledgeable");
    }

    @Override
    public float calcFitness(Solution solution) {
        List<Lesson> lessons = ((TimeTableSolution) solution).getLessons();

        int penalty = 0;
        for (Lesson lesson : lessons) {
            if (lesson.getTeacher().getTeachesCoursesIDs().contains(lesson.getCourse().getCourseID())) {
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
