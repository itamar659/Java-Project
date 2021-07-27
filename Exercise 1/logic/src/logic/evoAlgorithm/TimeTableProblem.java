package logic.evoAlgorithm;

import engine.base.Problem;
import logic.timeTable.*;
import logic.timeTable.Class;
import logic.timeTable.rules.base.Rules;
import engine.base.Solution;

import java.util.List;
import java.util.Random;

public class TimeTableProblem implements Problem<TimeTable> {

    private static final Random rand = new Random();

    // All the classes in this time table
    private List<Class> classes;
    // All the courses in this time table
    private List<Course> courses;
    // All the teachers that teach in this time table
    private List<Teacher> teachers;
    // The set of rules for this time table
    private Rules<TimeTable> rules;
    // Days available to study
    private int days;
    // amount of max hours to study everyday
    private int hours;

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Rules<TimeTable> getRules() {
        return rules;
    }

    public void setRules(Rules<TimeTable> rules) {
        this.rules = rules;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public TimeTableProblem() {
    }

    @Override
    public Solution<TimeTable> createSolution() {
        TimeTable solution = new TimeTable(this);
        solution.setRules(this.rules);

        final int maxLessons = classes.size() * days * hours;
        int lessons = rand.nextInt(maxLessons) + 1;

        for (int i = 0; i < lessons; i++) {
            // Randomize lesson
            Lesson lesson = randomizeLesson();

            // Add to the time table
            solution.addLesson(lesson);
        }

        return solution;
    }

    private boolean isTeacherTeaches(Teacher teacher, Course course) {
        return teacher.getTeachesCoursesIDs().contains(course.getId());
    }

    public Lesson randomizeLesson() {
        Lesson lesson = new Lesson();
        lesson.setaClass(randomizeClass());
        lesson.setCourse(randomizeCourse());
        do {
            lesson.setTeacher(randomizeTeacher());
        } while (!isTeacherTeaches(lesson.getTeacher(), lesson.getCourse()));

        lesson.setDay(randomizeDay());
        lesson.setHour(randomizeHour());

        return lesson;
    }

    public Class randomizeClass() {
        return classes.get(rand.nextInt(classes.size()));
    }

    public Course randomizeCourse() {
        return courses.get(rand.nextInt(courses.size()));
    }

    public Teacher randomizeTeacher() {
        return teachers.get(rand.nextInt(teachers.size()));
    }

    public int randomizeDay() {
        return rand.nextInt(days);
    }

    public int randomizeHour() {
        return rand.nextInt(hours);
    }
}
