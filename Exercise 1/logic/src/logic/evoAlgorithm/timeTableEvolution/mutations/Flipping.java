package logic.evoAlgorithm.timeTableEvolution.mutations;

import logic.evoAlgorithm.base.Parameterizable;
import logic.evoAlgorithm.base.*;
import logic.timeTable.Lesson;
import logic.evoAlgorithm.timeTableEvolution.TimeTableProblem;
import logic.timeTable.TimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flipping extends Mutation implements Parameterizable {

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
    public void mutatePopulation(Population population, Problem problem) {
        for (Solution solution : population.getSolutions()) {
            if (rand.nextDouble() <= probability) {
                mutate(solution, problem);
            }
        }
    }

    private void mutate(Solution solution, Problem problem) {
        List<Lesson> lessons = ((TimeTable) solution).getLessons();
        TimeTableProblem theProblem = (TimeTableProblem) problem;

        // Get the lessons to mutate
        int numOfMutates = rand.nextInt(this.maxTupples) + 1;
        List<Lesson> lessonsChosen = new ArrayList<>(numOfMutates);

        if (lessons.size() <= 0) {
            return;
        }

        for (int i = 0; i < numOfMutates; i++) {
            lessonsChosen.add(lessons.get(rand.nextInt(lessons.size())));
        }

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
                "probability=" + probability +
                ", maxTupples=" + maxTupples +
                ", component=" + component +
                '}';
    }
}