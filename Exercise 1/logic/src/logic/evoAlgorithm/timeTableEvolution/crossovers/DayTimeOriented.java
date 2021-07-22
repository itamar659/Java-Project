package logic.evoAlgorithm.timeTableEvolution.crossovers;

import logic.timeTable.Lesson;
import logic.timeTable.TimeTable;
import logic.evoAlgorithm.base.Crossover;
import logic.evoAlgorithm.base.Population;
import logic.evoAlgorithm.base.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DayTimeOriented implements Crossover {

    private static final Random rand = new Random();

    private int cuttingPoints;

    public int getCuttingPoints() {
        return cuttingPoints;
    }

    public void setCuttingPoints(int cuttingPoints) {
        // Can be bigger from the population. it'll cut every single block
        this.cuttingPoints = cuttingPoints;
    }

    // Very generic method (but may not answer every crossover)
    @Override
    public Population repopulateWithCrossover(Population population, int reachSize) {
        Population newPopulation = population.copySmallerPopulation(reachSize);

        Solution father, mother;

        // change i in the inner for-loop
        for (int i = population.getSize(); i < reachSize;) {
            father = population.getSolutionByIndex(rand.nextInt(population.getSize()));
            mother = population.getSolutionByIndex(rand.nextInt(population.getSize()));

            List<Solution> children = null;
            try {
                children = crossover(father, mother);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                throw new NullPointerException("children (crossover return value) cannot be null after crossover activated.");
            }

            for (int j = 0; j < children.size() && i < reachSize; j++, i++) {
                newPopulation.setSolutionByIndex(i, children.get(j));
            }
        }

        return newPopulation;
    }

    // Very specific method for this situation
    private List<Solution> crossover(Solution father, Solution mother) throws CloneNotSupportedException {
        // TODO: MAY NOT WORK. CHECK WITH PRINTS

        // sort the lessons
        List<Lesson> fatherLessons = ((TimeTable) father).getLessons();
        List<Lesson> motherLessons = ((TimeTable) mother).getLessons();
        fatherLessons.sort(DayTimeOriented::compareLessons);
        motherLessons.sort(DayTimeOriented::compareLessons);

        // Create Lists with null objects.
        List<Lesson> parent1 = new ArrayList<>();
        List<Lesson> parent2 = new ArrayList<>();
        int fatherIdx = 0;
        int motherIdx = 0;
        while (fatherIdx < fatherLessons.size() && motherIdx < motherLessons.size()) {
            Lesson fLesson = fatherLessons.get(fatherIdx);
            Lesson mLesson = motherLessons.get(motherIdx);
            int cmpResult = DayTimeOriented.compareLessons(fLesson, mLesson);

            if (cmpResult < 0) { // TODO: Check if changed the compare function
                parent1.add((Lesson) fLesson.clone());
                parent2.add(null);
                fatherIdx++;
            } else if (cmpResult > 1) {
                parent1.add(null);
                parent2.add((Lesson) mLesson.clone());
                motherIdx++;
            } else {
                parent1.add((Lesson) fLesson.clone());
                parent2.add((Lesson) mLesson.clone());
                motherIdx++;
                fatherIdx++;
            }
        }

        for (;fatherIdx < fatherLessons.size(); fatherIdx++) {
            Lesson fLesson = fatherLessons.get(fatherIdx);
            parent1.add((Lesson) fLesson.clone());
            parent2.add(null);
        }

        for (;motherIdx < motherLessons.size(); motherIdx++) {
            Lesson mLesson = motherLessons.get(motherIdx);
            parent1.add(null);
            parent2.add((Lesson) mLesson.clone());
        }
        // End creating the lists with null object.........


        // Now we can put the one each other and do the split.
        TimeTable child1 = new TimeTable(((TimeTable) father).getProblem());
        TimeTable child2 = new TimeTable(((TimeTable) father).getProblem());
        child1.setRules(((TimeTable) father).getRules());
        child2.setRules(((TimeTable) father).getRules());

        int swapAfter = parent1.size() / this.cuttingPoints;
        if (swapAfter == 0) {
            swapAfter = 1;
        }

        for (int i = 0; i < parent1.size(); i++) {
            if (i % swapAfter == 0) {
                TimeTable temp = child1;
                child1 = child2;
                child2 = temp;
            }

            if (parent1.get(i) != null) {
                child1.addLesson((Lesson) parent1.get(i).clone());
            }

            if (parent2.get(i) != null) {
                child2.addLesson((Lesson) parent2.get(i).clone());
            }
        }

        List<Solution> children = new ArrayList<>();
        if (child1.getLessons().size() > 0) children.add(child1);
        if (child2.getLessons().size() > 0) children.add(child2);

        return children;
    }

    @Override
    public String toString() {
        return "DayTimeOriented{" +
                "cutting-points=" + cuttingPoints +
                '}';
    }

    private static int compareLessons(Lesson o1, Lesson o2) {
        if (o1.getDay() < o2.getDay()) {
            return -1;
        }
        if (o1.getDay() > o2.getDay()) {
            return 1;
        }

        if (o1.getHour() < o2.getHour()) {
            return  -1;
        }
        if (o1.getHour() > o2.getHour()) {
            return  1;
        }

        if (o1.getaClass().getId().compareTo(o2.getaClass().getId()) < 0) {
            return  -1;
        }
        if (o1.getaClass().getId().compareTo(o2.getaClass().getId()) > 0) {
            return 1;
        }

        return o1.getTeacher().getId().compareTo(o2.getTeacher().getId());
    }
}
