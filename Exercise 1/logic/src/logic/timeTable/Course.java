package logic.timeTable;

import java.util.Objects;

public class Course implements Cloneable {

    private String courseID;
    private String name;

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseID, course.courseID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseID);
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID='" + courseID + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
