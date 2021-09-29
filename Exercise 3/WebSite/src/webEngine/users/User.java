package webEngine.users;

import logic.Engine;

import java.util.*;

public class User {
    private final String username;
    private final Set<Integer> solvingProblemID;
    private final Date registerTime;
    private Integer activeProblem;
    private Map<Integer, Engine> problemID2Engine;

    public final Set<Integer> getSolvingProblemID() {
        return solvingProblemID;
    }

    public void addSolvingProblemID(Integer solvingProblemID) {
        this.solvingProblemID.add(solvingProblemID);
    }

    public void removeSolvingProblemID(Integer solvingProblemID) {
        this.solvingProblemID.remove(solvingProblemID);
    }

    public boolean isSolvingProblemID(Integer solvingProblemID) {
        return this.solvingProblemID.contains(solvingProblemID);
    }

    public void addEngine(Integer problemID, Engine engine) {
        problemID2Engine.put(problemID, engine);
    }

    public void removeEngine(Integer problemID) {
        problemID2Engine.remove(problemID);
    }

    public Engine getEngine(Integer problemID) {
        return problemID2Engine.get(problemID);
    }

    public Integer getActiveProblemID() {
        return activeProblem;
    }

    public void setActiveProblem(Integer activeProblem) {
        this.activeProblem = activeProblem;
    }

    public String getUsername() {
        return username;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public User(String username) {
        this.username = username;
        this.registerTime = new Date();
        this.solvingProblemID = new HashSet<>();
        this.problemID2Engine = new HashMap<>();
        this.activeProblem = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(registerTime, user.registerTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, registerTime);
    }
}
