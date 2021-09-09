package webEngine;

import logic.evoAlgorithm.TimeTableProblem;

import java.util.HashMap;
import java.util.Map;

public class ProblemManager {

    private final ProblemStatisticsBuilder problemStatisticsBuilder = new ProblemStatisticsBuilder();

    private final Map<Integer, ProblemPair> id2problem = new HashMap<>();
    private int currentID = 1;

    public synchronized void addProblem(String uploader, TimeTableProblem problem) {
        problemStatisticsBuilder.setProblemID(currentID);
        problemStatisticsBuilder.setProblem(problem);
        problemStatisticsBuilder.setUploader(uploader);

        ProblemPair p = new ProblemPair(problem, problemStatisticsBuilder.create());

        id2problem.put(currentID, p);

        currentID++;
    }

    public synchronized void removeProblem(int problemID) {
        id2problem.remove(problemID);
    }

    public synchronized Map<Integer, ProblemStatistics> getProblemsStatistics() {
        Map<Integer, ProblemStatistics> statisticsMap = new HashMap<>();
        id2problem.forEach((key, value) -> statisticsMap.put(key, value.getProblemStatistics()));
        return statisticsMap;
    }

    public ProblemStatistics getProblemStatistics(int problemID) {
        return id2problem.get(problemID).problemStatistics;
    }

    public TimeTableProblem getProblem(int problemID) {
        return id2problem.get(problemID).realProblem;
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


//    private static class EngineStatistics {
//
//        private Engine engine;
//        private final Set<User> activeUsers = new HashSet<>();
//
//        public EngineStatistics() {
//        }
//
//        public void addUser(User user) {
//            activeUsers.add(user);
//        }
//
//        public void removeUser(User user) {
//            activeUsers.remove(user);
//        }
//
//        public User findUserByName(String name) {
//            return activeUsers.stream()
//                    .filter(user -> user.getName().equals(name))
//                    .findFirst()
//                    .orElse(null);
//        }
//
//        public int size() {
//            return activeUsers.size();
//        }
//
//        public Set<User> getActiveUsers() {
//            return activeUsers;
//        }
//    }
}
