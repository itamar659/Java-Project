package webEngine.utils;

import webEngine.ProblemStatistics;

import java.util.HashMap;
import java.util.Map;

public class ProblemManager {

    private final Map<Integer, ProblemStatistics> id2problem = new HashMap<>();
    private int currentID = 1;

    public synchronized void addProblem(ProblemStatistics problem) {
        id2problem.put(currentID++, problem);
    }

    public synchronized void removeProblem(int problemID) {
        id2problem.remove(problemID);
    }

    public synchronized Map<Integer, ProblemStatistics> getProblems() {
        return new HashMap<>(id2problem);
    }

    public ProblemStatistics getProblem(int problemID) {
        return id2problem.get(problemID);
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
