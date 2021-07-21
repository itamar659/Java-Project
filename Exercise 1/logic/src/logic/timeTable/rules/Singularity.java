package logic.timeTable.rules;

import logic.algorithm.TimeTableSolution;
import logic.timeTable.rules.base.Rule;
import logic.algorithm.genericEvolutionAlgorithm.Solution;
import logic.timeTable.Lesson;
import logic.validation.ValidationResult;

import java.util.List;

public class Singularity extends Rule {

    public Singularity() {
        this.setRuleName("Singularity");
    }

    @Override
    public float calcFitness(Solution solution) {
        List<Lesson> lessons = ((TimeTableSolution) solution).getLessons();

        int penalty = 0;
        for (int i = 0; i < lessons.size() - 1; i++) {
            for (int j = i + 1; j < lessons.size(); j++) {
                if (lessons.get(i).getDay() == lessons.get(j).getDay()) { // same day
                    if (lessons.get(i).getHour() == lessons.get(j).getHour()) { // same hour
                        if (lessons.get(i).getTeacher().equals(lessons.get(j).getTeacher())) { // same teacher
                            if (!lessons.get(i).getaClass().equals(lessons.get(j).getaClass())) { // different class
                                penalty++;
                            }
                        }
                    }
                }
            }
        }

        return 1f / (1 + penalty);
    }

    @Override
    public ValidationResult checkValidation() {
        return new ValidationResult(true);
    }
}
