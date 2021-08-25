package logic.evoAlgorithm.crossovers;

import engine.base.configurable.Configuration;
import engine.base.configurable.ReadOnlyConfiguration;
import logic.evoAlgorithm.crossovers.base.BaseCrossover;
import logic.timeTable.TimeTable;
import engine.base.Solution;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class DayTimeOriented extends BaseCrossover {

    private static final Random rand = new Random();

    private static final String PARAMETER_CUTTING_POINTS = "CuttingPoints";

    @Override
    public String getName() {
        return "Day Time Oriented";
    }

    @Override
    public int getCuttingPoints() {
        return Integer.parseInt(configuration.getParameter(PARAMETER_CUTTING_POINTS)); // TODO: Remove this copy paste configuration shit code
    }

    private final Configuration configuration;

    @Override
    public ReadOnlyConfiguration getConfiguration() {
        return configuration.getProxy();
    }

    @Override
    public void setParameter(String parameterName, String value) {
        // TODO: check if it's valid value in the correct range (Maybe not here but outside of this method?)
        if (parameterName.equals(PARAMETER_CUTTING_POINTS)) {
            Integer.parseInt(value);
        } else {
            throw new IllegalArgumentException("Parameter name not found in " + this.getClass().getSimpleName());
        }

        configuration.setParameter(parameterName, value);
    }

    @Override
    public void setCuttingPoints(int cuttingPoints) {
        // TODO: Can be bigger from the population. it'll cut every single block
        setParameter(PARAMETER_CUTTING_POINTS, Integer.toString(cuttingPoints));
    }

    public DayTimeOriented() {
        this.configuration = new Configuration(
                new AbstractMap.SimpleEntry<>(PARAMETER_CUTTING_POINTS, "0")
        );
    }

    @Override
    protected <T>List<Solution<TimeTable>> crossoverParents(Solution<TimeTable> father, Solution<TimeTable> mother) throws CloneNotSupportedException {
        Parents parents = parentsLessonsOrdered(father.getGens().getLessons(), mother.getGens().getLessons());

        // Now we can put the one each other and do the split.
        TimeTable child1 = (TimeTable) mother.createChild();
        TimeTable child2 = (TimeTable) mother.createChild();

        int numOfCuts = getCuttingPoints();
        if (getCuttingPoints() > parents.father.size()) {
            numOfCuts = parents.father.size();
        }

        int[] cuts = IntStream.generate(() -> rand.nextInt(parents.father.size() + 1))
                .distinct()
                .limit(numOfCuts)
                .sorted().toArray();

        int cut_idx = 1;

        for (int i = 0; i < parents.father.size(); i++) {
            if (cut_idx <= getCuttingPoints() && cuts[cut_idx - 1] == i) {
                cut_idx++;
                TimeTable temp = child1;
                child1 = child2;
                child2 = temp;
            }

            if (parents.father.get(i) != null) {
                child1.addLesson(parents.father.get(i).clone());
            }

            if (parents.mother.get(i) != null) {
                child2.addLesson(parents.mother.get(i).clone());
            }
        }

        List<Solution<TimeTable>> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        return children;
    }

    @Override
    public String toString() {
        return "DayTimeOriented{" +
                "cutting-points=" + getCuttingPoints() +
                '}';
    }
}
