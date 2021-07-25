package logic.evoAlgorithm.timeTableEvolution.mutations;

import logic.evoAlgorithm.base.Parameterizable;
import logic.evoAlgorithm.base.Mutation;
import logic.evoAlgorithm.base.Population;
import logic.evoAlgorithm.base.Problem;
import logic.evoAlgorithm.base.Solution;
import logic.evoAlgorithm.timeTableEvolution.TimeTableProblem;
import logic.timeTable.Lesson;
import logic.timeTable.TimeTable;

import java.util.List;
import java.util.Random;

public class Sizer extends Mutation implements Parameterizable {

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
}
