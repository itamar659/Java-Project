package logic.evoAlgorithm.crossovers;

import logic.timeTable.Lesson;
import logic.timeTable.TimeTable;
import engine.base.Crossover;
import engine.base.Population;
import engine.base.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DayTimeOriented implements Crossover<TimeTable> {

    private static final Random rand = new Random();

    private int cuttingPoints;

    @Override
    public int getCuttingPoints() {
        return cuttingPoints;
    }

    @Override
    public void setCuttingPoints(int cuttingPoints) {
        // Can be bigger from the population. it'll cut every single block
        this.cuttingPoints = cuttingPoints;
    }

    // Very generic method (but may not answer every crossover)
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
            } catch (CloneNotSupportedException e) { // Won't come here. Change to ignored
                e.printStackTrace();
                throw new NullPointerException("children (crossover return value) cannot be null after crossover activated.");
            }

            for (int j = 0; j < children.size() && i < size; j++, i++) {
                newPopulation.setSolutionByIndex(i, children.get(j));
            }
        }

        return newPopulation;
    }

    private static class Parents {

        public final List<Lesson> father;
        public final List<Lesson> mother;

        public Parents(List<Lesson> father, List<Lesson> mother) {
            this.father = father;
            this.mother = mother;
        }
    }

    // Very specific method for this situation
    private List<Solution<TimeTable>> crossoverParents(Solution<TimeTable> father, Solution<TimeTable> mother) throws CloneNotSupportedException {
        Parents parents = parentsLessonsOrdered(father.getGens().getLessons(), mother.getGens().getLessons());

        // Now we can put the one each other and do the split.
        TimeTable child1 = (TimeTable) mother.createChild();
        TimeTable child2 = (TimeTable) mother.createChild();

        int swapAfter = parents.father.size() / this.cuttingPoints;
        if (swapAfter <= 0) {
            swapAfter = 1;
        }

        for (int i = 0; i < parents.father.size(); i++) {
            if (i % swapAfter == 0) {
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

    private Parents parentsLessonsOrdered(List<Lesson> fatherLessons, List<Lesson> motherLessons)
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

    @Override
    public String toString() {
        return "DayTimeOriented{" +
                "cutting-points=" + cuttingPoints +
                '}';
    }
}
