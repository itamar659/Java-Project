package logic.Algorithm;

import logic.Algorithm.genericEvolutionAlgorithm.Rule;
import logic.Algorithm.genericEvolutionAlgorithm.Rules;
import logic.Algorithm.genericEvolutionAlgorithm.Solution;
import logic.timeTable.Lesson;

import java.util.List;

public class TestRule extends Rule {

    public TestRule() {
        this.setType(Rules.RULE_TYPE.HARD);
    }

    @Override
    public int calcPenalty(Solution solution) {
        List<Lesson> lessons = ((TimeTableSolution) solution).getLessons();

        int penalty = 0;
        if (lessons.size() < 2) {
            penalty += 2 - lessons.size();
        }
        if (lessons.size() > 5) {
            penalty += lessons.size() - 5;
        }

        for (Lesson lesson : lessons) {
            if (lesson.getDay() == 1) {
                if (lesson.getHour() < 4) {
                    continue;
                }
            }

            penalty++;
        }

        return penalty;
    }
}
