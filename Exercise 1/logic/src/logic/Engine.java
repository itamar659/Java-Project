package logic;

import logic.Algorithm.EvolutionAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class Engine {

//    public enum State {
//        INIT, FILE_LOADED, RUNNING, COMPLETED;
//    }
//    private State state;


    private boolean isCompletedRun;
    private boolean isFileLoaded;
    private boolean isRunning;

    private Schedule schedule;
    private EvolutionAlgorithm evolutionAlgorithm;

    public boolean isCompletedRun() {
        return isCompletedRun;
    }

    public boolean isFileLoaded() {
        return isFileLoaded;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Engine() {
        this.evolutionAlgorithm = new EvolutionAlgorithm();

    }

    public void Test() {
        // evolutionAlgorithm.runAlgorithm();

        isFileLoaded = true;
        Course c1 = new Course();
        c1.setCourseID("123");
        c1.setName("Math");
        Course c2 = new Course();
        c2.setCourseID("777");
        c2.setName("JAVA");
        Course c3 = new Course();
        c3.setCourseID("555");
        c3.setName("C++");

        Teacher t1 = new Teacher();
        t1.setName("Aviad");
        t1.setTeacherID("123456789");
        t1.addCourseToTeach("777");
        t1.addCourseToTeach("050");
        Teacher t2 = new Teacher();
        t2.setName("Moshe");
        t2.setTeacherID("987654321");
        t2.addCourseToTeach("555");

        StudentsClass s1 = new StudentsClass();
        s1.setClassID("121");
        s1.setName("bla bla");
        s1.addCourseToLearn("555", 4);
        StudentsClass s2 = new StudentsClass();
        s2.setClassID("120");
        s2.setName("gra gra");
        s2.addCourseToLearn("159", 2);
        s2.addCourseToLearn("777", 8);

        this.schedule = new Schedule();
        this.schedule.addClass(s1);
        this.schedule.addClass(s2);
        this.schedule.addCourse(c1);
        this.schedule.addCourse(c2);
        this.schedule.addCourse(c3);
        this.schedule.addTeacher(t1);
        this.schedule.addTeacher(t2);
    }
}
