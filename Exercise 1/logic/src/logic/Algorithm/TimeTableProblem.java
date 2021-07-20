package logic.Algorithm;

import logic.Algorithm.genericEvolutionAlgorithm.Problem;
import logic.timeTable.rules.base.Rules;
import logic.Algorithm.genericEvolutionAlgorithm.Solution;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.Lesson;
import logic.timeTable.Teacher;

import java.util.List;
import java.util.Random;

public class TimeTableProblem implements Problem {

    private static Random rand = new Random();

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

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
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
    public Solution createSolution() {
        // TODO: If the teacher doesn't teach that course, change a teacher / course
        // TODO: Also if the class doesn't study this course, change the class / course

        TimeTableSolution solution = new TimeTableSolution(this);
        solution.setRules(this.rules);

        final int maxLessons = classes.size() * days * hours;
        int lessons = rand.nextInt(maxLessons) + 1;

        for (int i = 0; i < lessons; i++) {
            // Randomize lesson
            Lesson lesson = new Lesson();
            lesson.setaClass(randomizeClass());
            lesson.setCourse(randomizeCourse());
            lesson.setTeacher(randomizeTeacher());
            lesson.setDay(randomizeDay());
            lesson.setHour(randomizeHour());

            // Add to the time table
            solution.addLesson(lesson);
        }

        return solution;
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
