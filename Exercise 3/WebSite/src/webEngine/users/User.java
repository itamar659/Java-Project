package webEngine.users;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
    private final String username;
    private final Set<Integer> solvingProblemID;
    private final Date registerTime;
    private Integer activeProblem;

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
