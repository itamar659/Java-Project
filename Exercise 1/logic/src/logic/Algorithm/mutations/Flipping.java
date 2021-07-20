package logic.Algorithm.mutations;

import logic.timeTable.Lesson;
import logic.Algorithm.TimeTableProblem;
import logic.Algorithm.TimeTableSolution;
import logic.Algorithm.genericEvolutionAlgorithm.Mutation;
import logic.Algorithm.genericEvolutionAlgorithm.Population;
import logic.Algorithm.genericEvolutionAlgorithm.Problem;
import logic.Algorithm.genericEvolutionAlgorithm.Solution;
import logic.validation.ValidationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flipping implements Mutation {

    private enum Component {
        S, T, C, H, D;
    }

    private static final Random rand = new Random();

    private int maxTupples;
    private Component component;

    public int getMaxTupples() {
        return maxTupples;
    }

    public void setMaxTupples(int maxTupples) {
        this.maxTupples = maxTupples;
    }

    public String getComponent() {
        return component.name();
    }

    public void setComponent(String component) {
        this.component = Component.valueOf(component);
    }

    @Override
    public void setValue(String parameterName, Object value) {
        if (parameterName.equals("MaxTupples")) {
            setMaxTupples(Integer.parseInt(value.toString()));
        } else if (parameterName.equals("Component")) {
            setComponent((String) value);
        } else {
            throw new IllegalArgumentException("Not found parameter name in " + this.getClass().getSimpleName());
        }
    }

    @Override
    public Object getValue(String parameterName) {
        if (parameterName.equals("MaxTupples")) {
            return getMaxTupples();
        } else if (parameterName.equals("Component")) {
            return getComponent();
        } else {
            throw new IllegalArgumentException("Not found parameter name in " + this.getClass().getSimpleName());
        }
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
        int numOfMutates = rand.nextInt(this.maxTupples) + 1;
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

    @Override
    public String toString() {
        return "Flipping{" +
                "maxTuples=" + maxTupples +
                ", component=" + component +
                '}';
    }

    @Override
    public ValidationResult checkValidation() {
        if (maxTupples < 0) {
            return new ValidationResult(false, "'MaxTuples' has to be positive value");
        }

        return new ValidationResult(true);
    }
}
