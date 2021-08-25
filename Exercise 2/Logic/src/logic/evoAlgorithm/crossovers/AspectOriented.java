package logic.evoAlgorithm.crossovers;

import engine.base.*;
import logic.evoAlgorithm.configurable.Configurable;
import logic.evoAlgorithm.configurable.Configuration;
import logic.evoAlgorithm.configurable.ReadOnlyConfiguration;
import logic.timeTable.Lesson;
import logic.timeTable.TimeTable;

import java.util.*;
import java.util.stream.IntStream;

public class AspectOriented implements Crossover<TimeTable>, Configurable {

    @Override
    public String getName() {
        return "Aspect Oriented";
    }

    public enum Orientation {
        CLASS, TEACHER;
    }

    private static final String PARAMETER_ORIENTATION = "Orientation";
    private static final Random rand = new Random();

    private final Configuration configuration;
    private int cuttingPoints;

    @Override
    public ReadOnlyConfiguration getConfiguration() {
        return configuration.getProxy();
    }

    @Override
    public void setParameter(String parameterName, String value) {
        if (parameterName.equals(PARAMETER_ORIENTATION)) {
            Orientation.valueOf(value);
        } else {
            throw new IllegalArgumentException("Parameter name not found in " + this.getClass().getSimpleName());
        }

        configuration.setParameter(parameterName, value);
    }

    @Override
    public int getCuttingPoints() {
        return cuttingPoints;
    }

    @Override
    public void setCuttingPoints(int cuttingPoints) {
        this.cuttingPoints = cuttingPoints;
    }

    public AspectOriented() {
        this.configuration = new Configuration(
                new AbstractMap.SimpleEntry<>(PARAMETER_ORIENTATION, Orientation.CLASS.name())
        );
    }

    public String getOrientation() {
        return configuration.getParameter(PARAMETER_ORIENTATION);
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

    @Override
    public String toString() {
        return "AspectOriented{" +
                "cuttingPoints=" + cuttingPoints +
                ", orientation=" + getOrientation() +
                '}';
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
    private <T>List<Solution<TimeTable>> crossoverParents(Solution<TimeTable> father, Solution<TimeTable> mother)
            throws CloneNotSupportedException {
        Map<T, List<Lesson>> motherMap = getMap(mother);
        Map<T, List<Lesson>> fatherMap = getMap(father);
        List<T> availableTeachers = getWhatAvailable(father);

        updateMapsAndOrderOneOverAnother(fatherMap, motherMap, availableTeachers);

        // Now we can put the one each other and do the split.
        TimeTable child1 = (TimeTable) mother.createChild();
        TimeTable child2 = (TimeTable) mother.createChild();

        crossoverParentsAndUpdateChildren(fatherMap,
                motherMap, child1, child2);

        List<Solution<TimeTable>> children = new ArrayList<>();
        children.add(child1);
        children.add(child2);

        return children;
    }

    private <T> List<T> getWhatAvailable(Solution<TimeTable> father) {
        if (getOrientation().equals(Orientation.CLASS.name())) {
            return (List<T>) father.getGens().getProblem().getClasses();
        } else {
            return (List<T>) father.getGens().getProblem().getTeachers();
        }
    }

    private <T>Map<T, List<Lesson>> getMap(Solution<TimeTable> tt) {
        if (getOrientation().equals(Orientation.CLASS.name())) {
            return (Map<T, List<Lesson>>) tt.getGens().getClassesTimeTable();
        } else {
            return (Map<T, List<Lesson>>) tt.getGens().getTeachersTimeTable();
        }
    }

    // compare the maps and make them equal in size and shape
    private <T>void updateMapsAndOrderOneOverAnother(Map<T, List<Lesson>> father, Map<T, List<Lesson>> mother,
                                                     List<T> available) throws CloneNotSupportedException {
        for (T t : available) {
            if (!father.containsKey(t) && !mother.containsKey(t)) {
                continue;
            }

            if (!father.containsKey(t)) {
                father.put(t, new ArrayList<>());
            }
            if (!mother.containsKey(t)) {
                mother.put(t, new ArrayList<>());
            }

            Parents parents = parentsLessonsOrdered(father.get(t), mother.get(t));
            father.put(t, parents.father);
            mother.put(t, parents.mother);
        }
    }

    // This method should fill the empty lessons between the teachers
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

    // update children list of lessons by crossover of their parents
    private <T>void crossoverParentsAndUpdateChildren(Map<T, List<Lesson>> father, Map<T, List<Lesson>> mother,
                                                      Solution<TimeTable> child1, Solution<TimeTable> child2) {
        // the maps has to be the same size
        for (Map.Entry<T, List<Lesson>> valueFromFather : father.entrySet()) {
            List<Lesson> currentFatherValue = valueFromFather.getValue();
            List<Lesson> currentMotherValue = mother.get(valueFromFather.getKey());

            try {
                crossoverLessons(currentFatherValue, currentMotherValue, child1, child2);
            } catch (CloneNotSupportedException e) { // Won't come here. Change to ignored
                e.printStackTrace();
                throw new NullPointerException("teacher list (crossover return value) cannot be null after crossover activated.");
            }
        }
    }

    private void crossoverLessons(List<Lesson> p1, List<Lesson> p2,
                                  Solution<TimeTable> child1, Solution<TimeTable> child2) throws CloneNotSupportedException {
        Solution<TimeTable> c1 = child1;
        Solution<TimeTable> c2 = child2;

        int numOfCuts = this.cuttingPoints;
        if (this.cuttingPoints > p1.size()) {
            numOfCuts = p1.size();
        }

        int[] cuts = IntStream.generate(() -> rand.nextInt(p1.size() + 1))
                .distinct()
                .limit(numOfCuts)
                .sorted().toArray();

        int cut_idx = 1;

        for (int i = 0; i < p1.size(); i++) {
            if (cut_idx <= this.cuttingPoints && cuts[cut_idx - 1] == i) {
                cut_idx++;
                Solution<TimeTable> temp = c1;
                c1 = c2;
                c2 = temp;
            }

            if (p1.get(i) != null) {
                c1.getGens().addLesson(p1.get(i).clone());
            }

            if (p2.get(i) != null) {
                c2.getGens().addLesson(p2.get(i).clone());
            }
        }
    }
}
