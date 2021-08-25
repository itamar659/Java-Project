package logic.evoAlgorithm.mutations;

import engine.base.configurable.Configurable;
import engine.base.configurable.Configuration;
import engine.base.configurable.ReadOnlyConfiguration;
import logic.evoAlgorithm.TimeTableProblem;
import engine.base.*;
import logic.timeTable.Lesson;
import logic.timeTable.TimeTable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Flipping implements Mutation<TimeTable> {

    @Override
    public String getName() {
        return "Flipping";
    }

    private enum Component {
        S, T, C, H, D;
    }

    private static final String PARAMETER_COMPONENT = "Component";
    private static final String PARAMETER_MAX_TUPPLES = "MaxTupples";
    private static final String PARAMETER_PROBABILITY = "Probability"; // TODO: Remove this copy paste configuration shit code
    private static final Random rand = new Random();

    private final Configuration configuration;

    @Override
    public ReadOnlyConfiguration getConfiguration() {
        return configuration.getProxy();
    }

    @Override
    public void setParameter(String parameterName, String value) {
        // TODO: check if it's valid value in the correct range (Maybe not here but outside of this method?)
        if (parameterName.equals(PARAMETER_MAX_TUPPLES)) {
            Integer.parseInt(value);
        } else if (parameterName.equals(PARAMETER_COMPONENT)) {
            Component.valueOf(value);
        } else if (parameterName.equals(PARAMETER_PROBABILITY)) {
            Double.parseDouble(value);
        } else {
            throw new IllegalArgumentException("Parameter name not found in " + this.getClass().getSimpleName());
        }

        configuration.setParameter(parameterName, value);
    }

    public Flipping() {
        configuration = new Configuration(
                new AbstractMap.SimpleEntry<>(PARAMETER_PROBABILITY, "0"),
                new AbstractMap.SimpleEntry<>(PARAMETER_MAX_TUPPLES, "0"),
                new AbstractMap.SimpleEntry<>(PARAMETER_COMPONENT, Component.C.name())
        );
    }

    public int getMaxTupples() {
        return Integer.parseInt(configuration.getParameter(PARAMETER_MAX_TUPPLES));
    }

    public Component getComponent() {
        return Component.valueOf(configuration.getParameter(PARAMETER_COMPONENT));
    }

    @Override
    public double getProbability() {
        return Double.parseDouble(configuration.getParameter(PARAMETER_PROBABILITY));
    }

    @Override
    public void setProbability(double probability) {
        setParameter(PARAMETER_PROBABILITY, Double.toString(probability));
    }

    @Override
    public void mutatePopulation(Population<TimeTable> population, Problem<TimeTable> problem) {
        for (Solution<TimeTable> solution : population.getSolutions()) {
            if (rand.nextDouble() <= getProbability()) {
                mutate(solution, problem);
            }
        }
    }

    private void mutate(Solution<TimeTable> solution, Problem<TimeTable> problem) {
        List<Lesson> lessons = solution.getGens().getLessons();
        TimeTableProblem theProblem = (TimeTableProblem) problem;

        // Get the lessons to mutate
        int numOfMutates = rand.nextInt(getMaxTupples()) + 1;
        List<Lesson> lessonsChosen = new ArrayList<>(numOfMutates);

        if (lessons.size() <= 0) {
            return;
        }

        for (int i = 0; i < numOfMutates; i++) {
            lessonsChosen.add(lessons.get(rand.nextInt(lessons.size())));
        }

        // mutate the chosen lessons
        for (Lesson lesson : lessonsChosen) {
            switch (getComponent()) {
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
                "probability=" + getProbability() +
                ", maxTupples=" + getMaxTupples() +
                ", component=" + getComponent() +
                '}';
    }
}
