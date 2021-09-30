package logic.evoAlgorithm.crossovers.base;

import engine.base.Crossover;
import engine.base.Population;
import engine.base.Solution;
import engine.base.configurable.Configuration;
import logic.timeTable.Lesson;
import logic.timeTable.TimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class BaseCrossover implements Crossover<TimeTable> {

    protected static final Random rand = new Random();

    public static final String PARAMETER_CUTTING_POINTS = "CuttingPoints";

    protected final Configuration configuration;

    protected BaseCrossover(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public int getCuttingPoints() {
        return Integer.parseInt(configuration.getParameter(PARAMETER_CUTTING_POINTS));
    }

    @Override
    public void setCuttingPoints(int cuttingPoints) {
        setParameter(PARAMETER_CUTTING_POINTS, Integer.toString(cuttingPoints));
    }

    @Override
    public Population<TimeTable> crossover(Population<TimeTable> population, int size) {
        Population<TimeTable> newPopulation = population.initializeSubPopulation(size);

        Solution<TimeTable> father, mother;

        // change i in the inner for-loop
        for (int i = 0; i < size;) {
            father = population.getSolutionByIndex(rand.nextInt(population.getSize()));
            mother = population.getSolutionByIndex(rand.nextInt(population.getSize()));

            List<Solution<TimeTable>> children = null;
            try {
                children = crossoverParents(father, mother);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                throw new NullPointerException("children (crossover return value) cannot be null after crossover activated.");
            }

            for (int j = 0; j < children.size() && i < size; j++, i++) {
                newPopulation.setSolutionByIndex(i, children.get(j));
            }
        }

        return newPopulation;
    }

    protected Parents parentsLessonsOrdered(List<Lesson> fatherLessons, List<Lesson> motherLessons)
            throws CloneNotSupportedException {
        // sort the lessons
        fatherLessons.sort(Lesson::compareByDHCTS);
        motherLessons.sort(Lesson::compareByDHCTS);

        // Create Lists with null objects.
        List<Lesson> parent1 = new ArrayList<>(fatherLessons.size() + motherLessons.size());
        List<Lesson> parent2 = new ArrayList<>(fatherLessons.size() + motherLessons.size());
        int fatherIdx = 0;
        int motherIdx = 0;
        int cmpResult = 0;
        while (fatherIdx < fatherLessons.size() && motherIdx < motherLessons.size()) {
            Lesson currentFatherLesson = fatherLessons.get(fatherIdx);
            Lesson currentMotherLesson = motherLessons.get(motherIdx);
            cmpResult = currentFatherLesson.compareByDHCTS(currentMotherLesson);

            if (cmpResult < 0) {
                parent1.add(currentFatherLesson.clone());
                parent2.add(null);
                fatherIdx++;
            } else if (cmpResult > 0) {
                parent1.add(null);
                parent2.add(currentMotherLesson.clone());
                motherIdx++;
            } else {
                parent1.add(currentFatherLesson.clone());
                parent2.add(currentMotherLesson.clone());
                motherIdx++;
                fatherIdx++;
            }
        }

        for (;fatherIdx < fatherLessons.size(); fatherIdx++) {
            Lesson currentFatherLesson = fatherLessons.get(fatherIdx);
            parent1.add(currentFatherLesson.clone());
            parent2.add(null);
        }

        for (;motherIdx < motherLessons.size(); motherIdx++) {
            Lesson currentMotherLesson = motherLessons.get(motherIdx);
            parent1.add(null);
            parent2.add(currentMotherLesson.clone());
        }

        return new Parents(parent1, parent2);
    }

    protected abstract <T>List<Solution<TimeTable>> crossoverParents(Solution<TimeTable> father, Solution<TimeTable> mother)
            throws CloneNotSupportedException;

    protected static class Parents {

        public final List<Lesson> father;
        public final List<Lesson> mother;

        public Parents(List<Lesson> father, List<Lesson> mother) {
            this.father = father;
            this.mother = mother;
        }
    }
}
