package webEngine.utils;

import logic.Engine;

import java.util.HashSet;
import java.util.Set;

public class EngineManager {

    private final Set<Engine> engineList = new HashSet<>();

    public synchronized void addEngine(Engine engine) {
        engineList.add(engine);
    }

    public synchronized void removeEngine(Engine engine) {
        engineList.remove(engine);
    }

    public synchronized Set<Engine> getEngines() {
        return new HashSet<>(engineList);
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
