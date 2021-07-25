package logic.timeTable.rules;

import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import logic.evoAlgorithm.base.Solution;
import logic.timeTable.Lesson;

import java.util.List;

public class Singularity extends Rule {

    @Override
    public String getId() {
        return "Singularity";
    }

    @Override
    public float calcFitness(Solution solution) {
        List<Lesson> lessons = ((TimeTable) solution).getLessons();

        int penalty = 0;
        int max = lessons.size();

        lessons.sort(Lesson::compareByDHCTS);
        for (int i = 0; i < max - 1; i++) {
            for (int j = i + 1; j < max; j++) {
                if (lessons.get(i).getDay() == lessons.get(j).getDay()) { // same day
                    if (lessons.get(i).getHour() == lessons.get(j).getHour()) { // same hour
                        if (lessons.get(i).getaClass().equals(lessons.get(j).getaClass())) { // same class
                            penalty++;
                        }
                    } else if (lessons.get(i).getHour() < lessons.get(j).getHour()) {
                        break;
                    }
                } else if (lessons.get(i).getDay() < lessons.get(j).getDay()) {
                    break;
                }
            }
        }

        return (float)Math.pow((max - penalty) / ((float) max), 2);
    }
}
