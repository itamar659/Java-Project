package logic.timeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher implements HasId, Cloneable {

    private String id;
    private String name;
    private final List<String> teachesCourses;

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

    public List<String> getTeachesCoursesIDs() {
        return teachesCourses;
    }

    public void addCourseToTeach(String courseID) {
        this.teachesCourses.add(courseID);
    }

    public Teacher() {
        teachesCourses = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "TeacherID='" + id + '\'' +
                ", name='" + name + '\'' +
                ", teachesCourses=" + teachesCourses +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
