package logic.timeTable.rules;

import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import engine.base.Solution;
import logic.timeTable.Lesson;

import java.util.ArrayList;
import java.util.List;

public class TeacherIsHuman extends Rule<TimeTable> {

    @Override
    public String getId() {
        return "TeacherIsHuman";
    }

    @Override
    public float calcFitness(Solution<TimeTable> solution) {
        List<Lesson> lessons = new ArrayList<>(solution.getGens().getLessons());

        int penalty = 0;
        int max = lessons.size();

        lessons.sort(Lesson::compareByDHCTS);
        for (int i = 0; i < max - 1; i++) {
            for (int j = i + 1; j < max; j++) {
                if (lessons.get(i).getDay() == lessons.get(j).getDay()) { // same day
                    if (lessons.get(i).getHour() == lessons.get(j).getHour()) { // same hour
                        if (lessons.get(i).getTeacher().equals(lessons.get(j).getTeacher())) { // same teacher
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

        if (max == 0) {
            return 0;
        }

        return (float)Math.pow((max - penalty) / ((float) max), 2) * MAX_PERCENTAGE;
    }
}