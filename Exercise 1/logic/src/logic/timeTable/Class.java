package logic.timeTable;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Class implements HasId, Cloneable {

    private String id;
    private String name;
    private final Map<String, Integer> courseID2Hours;
    private int totalHours;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getCourseID2Hours() {
        return courseID2Hours;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void addCourseToLearn(String courseID, int hours) {
        totalHours += hours;
        this.courseID2Hours.put(courseID, hours);
    }

    public Class() {
        this.courseID2Hours = new TreeMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Class that = (Class) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Class{" +
                "classID='" + id + '\'' +
                ", name='" + name + '\'' +
                ", courseID2Hours=" + courseID2Hours +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
