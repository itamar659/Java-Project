package logic;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class StudentsClass {

    private String classID;
    private String name;
    private final Map<String, Integer> courseID2Hours;

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
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

    public void addCourseToLearn(String courseID, int hours) {
        this.courseID2Hours.put(courseID, hours);
    }

    public StudentsClass() {
        this.courseID2Hours = new TreeMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentsClass that = (StudentsClass) o;
        return Objects.equals(classID, that.classID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classID);
    }
}
