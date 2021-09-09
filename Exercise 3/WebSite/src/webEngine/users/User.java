package webEngine.users;

import java.util.Date;
import java.util.Objects;

public class User {

    private String name;
    private Integer solvingProblemID;
    private final Date registerTime;

    public Integer getSolvingProblemID() {
        return solvingProblemID;
    }

    public void setSolvingProblemID(Integer solvingProblemID) {
        this.solvingProblemID = solvingProblemID;
    }

    public String getName() {
        return name;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public User(String name) {
        this.name = name;
        this.registerTime = new Date();
        this.solvingProblemID = null;
    }

    private void changeName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(registerTime, user.registerTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, registerTime);
    }
}
