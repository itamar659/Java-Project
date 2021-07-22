package logic.timeTable.rules;

import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import logic.evoAlgorithm.base.Solution;
import logic.timeTable.Lesson;

import java.util.List;

public class TeacherIsHuman extends Rule {

    public TeacherIsHuman() {
        this.setId("TeacherIsHuman");
    }

    @Override
    public float calcFitness(Solution solution) {
        List<Lesson> lessons = ((TimeTable) solution).getLessons();

        int penalty = 0;
        for (int i = 0; i < lessons.size() - 1; i++) {
            for (int j = i + 1; j < lessons.size(); j++) {
                if (lessons.get(i).getDay() == lessons.get(j).getDay()) { // same day
                    if (lessons.get(i).getHour() == lessons.get(j).getHour()) { // same hour
                        if (lessons.get(i).getaClass().equals(lessons.get(j).getaClass())) { // same class
                            if (!lessons.get(i).getTeacher().equals(lessons.get(j).getTeacher())) { // different teacher
                                penalty++;
                            }
                        }
                    }
                }
            }
        }

        return 1f / (1 + penalty);
    }
}



















