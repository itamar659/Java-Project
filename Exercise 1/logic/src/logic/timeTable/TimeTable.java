package logic.timeTable;

import logic.Algorithm.genericEvolutionAlgorithm.Rules;
import logic.Algorithm.genericEvolutionAlgorithm.Solution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TimeTable implements Solution {

    // All the classes in this time table
    private List<Class> classes;
    // All the courses in this time table
    private List<Course> courses;
    // All the teachers that teach in this time table
    private List<Teacher> teachers;
    // The set of rules for this time table
    private Rules rules;
    // Days available to study
    private int days;
    // amount of max hours to study everyday
    private int hours;

    public List<Class> getClasses() {
        return classes;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public TimeTable() {
        this.classes = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.teachers = new ArrayList<>();
        this.rules = new Rules();
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addClass(Class clazz) {
        classes.add(clazz);
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public void sortListsByID() {
        courses.sort(Comparator.comparing(Course::getCourseID));
        teachers.sort(Comparator.comparing(Teacher::getTeacherID));
        classes.sort(Comparator.comparing(Class::getClassID));
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

    @Override
    public float getFitness() {

        return 0;
    }
}
