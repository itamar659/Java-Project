package logic.timeTable;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Class implements Cloneable {

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

    public Class() {
        this.courseID2Hours = new TreeMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Class that = (Class) o;
        return Objects.equals(classID, that.classID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classID);
    }

    @Override
    public String toString() {
        return "Class{" +
                "classID='" + classID + '\'' +
                ", name='" + name + '\'' +
                ", courseID2Hours=" + courseID2Hours +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
