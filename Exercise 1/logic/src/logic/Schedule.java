package logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Schedule {

    private final List<StudentsClass> studentsClasses;
    private final List<Course> courses;
    private final List<Teacher> teachers;

    public List<StudentsClass> getClasses() {
        return studentsClasses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public Schedule() {
        this.studentsClasses = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.teachers = new ArrayList<>();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addClass(StudentsClass clazz) {
        studentsClasses.add(clazz);
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public void sortListsByID() {
        courses.sort(Comparator.comparing(Course::getCourseID));
        teachers.sort(Comparator.comparing(Teacher::getTeacherID));
        studentsClasses.sort(Comparator.comparing(StudentsClass::getClassID));
    }

    public Course findCourse(String courseID) {
        // TODO: Implement binarysearch if possible?
        for (Course currentCourse : courses) {
            if (currentCourse.getCourseID().equals(courseID)) {
                return currentCourse;
            }
        }

        return null;
    }
}
