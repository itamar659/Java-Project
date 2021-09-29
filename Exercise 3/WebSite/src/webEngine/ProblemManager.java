package webEngine;

import logic.evoAlgorithm.TimeTableProblem;

import java.util.HashMap;
import java.util.Map;

public class ProblemManager {

    private final Map<Integer, ProblemPair> id2problem = new HashMap<>();
    private int currentID = 1;

    public void addProblem(String uploader, TimeTableProblem problem) {
        ProblemStatisticsBuilder problemStatisticsBuilder = new ProblemStatisticsBuilder();
        problemStatisticsBuilder.setProblemID(currentID);
        problemStatisticsBuilder.setProblem(problem);
        problemStatisticsBuilder.setUploader(uploader);

        synchronized (this) {
            ProblemPair p = new ProblemPair(problem, problemStatisticsBuilder.create());
            id2problem.put(currentID, p);
            currentID++;
        }
    }

    public synchronized void removeProblem(int problemID) {
        id2problem.remove(problemID);
    }

    public synchronized Map<Integer, ProblemStatistics> getProblemsStatistics() {
        Map<Integer, ProblemStatistics> statisticsMap = new HashMap<>();
        id2problem.forEach((key, value) -> statisticsMap.put(key, value.getProblemStatistics()));
        return statisticsMap;
    }

    public synchronized ProblemStatistics getProblemStatistics(int problemID) {
        return id2problem.get(problemID).problemStatistics;
    }

    public synchronized TimeTableProblem getProblem(int problemID) {
        return id2problem.get(problemID).realProblem;
    }

    public boolean contains(int problemId) {
        return id2problem.containsKey(problemId);
    }

    private static class ProblemPair {

        private final TimeTableProblem realProblem;
        private final ProblemStatistics problemStatistics;

        public TimeTableProblem getRealProblem() {
            return realProblem;
        }

        public ProblemStatistics getProblemStatistics() {
            return problemStatistics;
        }

        public ProblemPair(TimeTableProblem realProblem, ProblemStatistics problem) {
            this.realProblem = realProblem;
            this.problemStatistics = problem;
        }
    }
}
