package logic.evoAlgorithm.mutations;

import engine.base.*;
import engine.base.configurable.Configurable;
import engine.base.configurable.Configuration;
import engine.base.configurable.ReadOnlyConfiguration;
import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.Lesson;
import logic.timeTable.TimeTable;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sizer implements Mutation<TimeTable> {

    public static final String PARAMETER_TOTAL_TUPPLES = "TotalTupples";
    public static final String PARAMETER_PROBABILITY = "Probability";

    private static final Random rand = new Random();
    private final Configuration configuration;

    @Override
    public String getName() {
        return "Sizer";
    }

    @Override
    public ReadOnlyConfiguration getConfiguration() {
        return configuration.getProxy();
    }

    @Override
    public void setParameter(String parameterName, String value) {
        // TODO: check if it's valid value in the correct range (Maybe not here but outside of this method?)
        if (parameterName.equals(PARAMETER_TOTAL_TUPPLES)) {
            int i = Integer.parseInt(value);
            if(i <= 0)
                throw new IllegalArgumentException("invalid parameter");
        } else if (parameterName.equals(PARAMETER_PROBABILITY)) {
            double d = Double.parseDouble(value);
            if(d <= 0 || d > 1.0)
                throw new IllegalArgumentException("invalid parameter");

        } else {
            throw new IllegalArgumentException("Not found parameter name in " + this.getClass().getSimpleName());
        }

        configuration.setParameter(parameterName, value);
    }

    public Sizer() {
        configuration = new Configuration(
                new AbstractMap.SimpleEntry<>(PARAMETER_PROBABILITY, "0"),
                new AbstractMap.SimpleEntry<>(PARAMETER_TOTAL_TUPPLES, "0")
        );
    }

    public int getTotalTupples() {
        return Integer.parseInt(configuration.getParameter(PARAMETER_TOTAL_TUPPLES));
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
        List<Lesson> lessons = new ArrayList<>(solution.getGens().getLessons());
        TimeTableProblem theProblem = (TimeTableProblem) problem;

        if (getTotalTupples() >= 0) {
            addLessons(lessons, theProblem);
        } else {
            removeLessons(lessons, theProblem);
        }
    }

    private void addLessons(List<Lesson>lessons, TimeTableProblem theProblem) {
        for (int i = 0; i < getTotalTupples(); i++) {
            if (lessons.size() >= theProblem.getDays() * theProblem.getHours()) {
                break;
            }

            lessons.add(theProblem.randomizeLesson());
        }
    }

    private void removeLessons(List<Lesson>lessons, TimeTableProblem theProblem) {
        int removes = rand.nextInt( (-1) * getTotalTupples());

        for (int i = 0; i < removes; i++) {
            if (lessons.size() <= theProblem.getDays()) {
                break;
            }

            lessons.remove(rand.nextInt(lessons.size()));
        }
    }

    @Override
    public String toString() {
        return "Sizer{" +
                "probability=" + getProbability() +
                ", totalTupples=" + getTotalTupples() +
                '}';
    }

}
