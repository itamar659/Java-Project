package logic.Algorithm.mutations;

import logic.timeTable.Lesson;
import logic.Algorithm.TimeTableProblem;
import logic.Algorithm.TimeTableSolution;
import logic.Algorithm.genericEvolutionAlgorithm.Mutation;
import logic.Algorithm.genericEvolutionAlgorithm.Population;
import logic.Algorithm.genericEvolutionAlgorithm.Problem;
import logic.Algorithm.genericEvolutionAlgorithm.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flipping implements Mutation {

    private enum Component {
        S, T, C, H, D;
    }

    private static final Random rand = new Random();

    private int maxTuples;
    private Component component;

    public int getMaxTuples() {
        return maxTuples;
    }

    public void setMaxTuples(int maxTuples) {
        this.maxTuples = maxTuples;
    }

    public String getComponent() {
        return component.name();
    }

    public void setComponent(String component) {
        this.component = Component.valueOf(component);
    }

    @Override
    public void mutatePopulation(Population population, float mutateChance, Problem problem) {
        for (Solution solution : population.getSolutions()) {
            if (rand.nextFloat() <= mutateChance) {
                mutate(solution, problem);
            }
        }
    }

    private void mutate(Solution solution, Problem problem) {
        List<Lesson> lessons = ((TimeTableSolution) solution).getLessons();
        TimeTableProblem theProblem = (TimeTableProblem) problem;

        // Get the lessons to mutate
        int numOfMutates = rand.nextInt(this.maxTuples) + 1;
        List<Lesson> lessonsChosen = new ArrayList<>(numOfMutates);

        for (int i = 0; i < numOfMutates; i++) {
            lessonsChosen.add(lessons.get(rand.nextInt(lessons.size())));
        }

        // Without multiply lessons
//        for (int i = 0; i < numOfMutates; i++) {
//            int lChosen = rand.nextInt(lessons.size());
//            if (!lessonsChosen.contains(lessons.get(lChosen))) {
//                lessonsChosen.add(lessons.get(lChosen));
//            } else {
//                i--;
//            }
//        }

        // mutate the chosen lessons
        for (Lesson lesson : lessonsChosen) {
            switch (component) {
                case S:
                    lesson.setCourse(theProblem.randomizeCourse());
                    break;
                case T:
                    lesson.setTeacher(theProblem.randomizeTeacher());
                    break;
                case C:
                    lesson.setaClass(theProblem.randomizeClass());
                    break;
                case H:
                    lesson.setHour(theProblem.randomizeHour());
                    break;
                case D:
                    lesson.setDay(theProblem.randomizeDay());
                    break;
            }
        }
    }
}
