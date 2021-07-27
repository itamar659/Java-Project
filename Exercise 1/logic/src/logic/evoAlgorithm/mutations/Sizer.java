package logic.evoAlgorithm.mutations;

import logic.evoAlgorithm.TimeTableProblem;
import logic.schema.Parameterizable;
import engine.base.Mutation;
import engine.base.Population;
import engine.base.Problem;
import engine.base.Solution;
import logic.timeTable.Lesson;
import logic.timeTable.TimeTable;

import java.util.List;
import java.util.Random;

public class Sizer extends Mutation<TimeTable> implements Parameterizable {

    private static final Random rand = new Random();
    private int totalTupples;

    public int getTotalTupples() {
        return totalTupples;
    }

    public void setTotalTupples(int totalTupples) {
        this.totalTupples = totalTupples;
    }

    @Override
    public void setValue(String parameterName, Object value) {
        if (parameterName.equals("TotalTupples")) {
            setTotalTupples(Integer.parseInt(value.toString()));
        } else {
            throw new IllegalArgumentException("Not found parameter name in " + this.getClass().getSimpleName());
        }
    }

    @Override
    public Object getValue(String parameterName) {
        if (parameterName.equals("TotalTupples")) {
            return getTotalTupples();
        } else {
            throw new IllegalArgumentException("Not found parameter name in " + this.getClass().getSimpleName());
        }
    }

    @Override
    public void mutatePopulation(Population<TimeTable> population, Problem<TimeTable> problem) {
        for (Solution<TimeTable> solution : population.getSolutions()) {
            if (rand.nextDouble() <= probability) {
                mutate(solution, problem);
            }
        }
    }

    private void mutate(Solution<TimeTable> solution, Problem<TimeTable> problem) {
        List<Lesson> lessons = solution.getGens().getLessons();
        TimeTableProblem theProblem = (TimeTableProblem) problem;

        if (totalTupples >= 0) {
            addLessons(lessons, theProblem);
        } else {
            removeLessons(lessons, theProblem);
        }
    }

    private void addLessons(List<Lesson>lessons, TimeTableProblem theProblem) {
        for (int i = 0; i < totalTupples; i++) {
            if (lessons.size() >= theProblem.getDays() * theProblem.getHours()) {
                break;
            }

            lessons.add(theProblem.randomizeLesson());
        }
    }

    private void removeLessons(List<Lesson>lessons, TimeTableProblem theProblem) {
        int removes = rand.nextInt( (-1) * totalTupples);

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
                "probability=" + probability +
                ", totalTupples=" + totalTupples +
                '}';
    }
}
