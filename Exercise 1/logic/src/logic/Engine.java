package logic;

import logic.Algorithm.TestRule;
import logic.Algorithm.TimeTableProblem;
import logic.Algorithm.genericEvolutionAlgorithm.*;
import logic.Algorithm.genericEvolutionAlgorithm.selections.SelectionFactory;
import logic.Algorithm.genericEvolutionAlgorithm.selections.Truncation;
import logic.Algorithm.crossovers.DayTimeOriented;
import logic.Algorithm.mutations.Flipping;
import logic.schema.generated.*;
import logic.Algorithm.TimeTableEvolutionAlgorithm;
import logic.timeTable.*;
import logic.timeTable.Class;

import javax.management.Descriptor;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Engine {

//    public enum State {
//        INIT, FILE_LOADED, RUNNING, COMPLETED;
//    }
//    private State state;


    private boolean isCompletedRun;
    private boolean isFileLoaded;
    private boolean isRunning;

    private TimeTable timeTable;
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

    public TimeTable getSchedule() {
        return timeTable;
    }



    public Engine() {
        this.evolutionAlgorithm = new TimeTableEvolutionAlgorithm();
    }

    public void Test() {
        isRunning = true;
        Course c1 = new Course();
        c1.setCourseID("123");
        c1.setName("Math");
        Course c2 = new Course();
        c2.setCourseID("777");
        c2.setName("JAVA");
        Course c3 = new Course();
        c3.setCourseID("555");
        c3.setName("C++");
        List<Course> courses = new ArrayList<>();
        courses.add(c1);
        courses.add(c2);
        courses.add(c3);

        Teacher t1 = new Teacher();
        t1.setName("Aviad");
        t1.setTeacherID("123456789");
        t1.addCourseToTeach("777");
        t1.addCourseToTeach("050");
        Teacher t2 = new Teacher();
        t2.setName("Moshe");
        t2.setTeacherID("987654321");
        t2.addCourseToTeach("555");
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(t1);
        teachers.add(t2);

        Class s1 = new Class();
        s1.setClassID("121");
        s1.setName("bla bla");
        s1.addCourseToLearn("555", 4);
        Class s2 = new Class();
        s2.setClassID("120");
        s2.setName("gra gra");
        s2.addCourseToLearn("159", 2);
        s2.addCourseToLearn("777", 8);
        List<Class> classes = new ArrayList<>();
        classes.add(s1);
        classes.add(s2);

        Rules rules = new Rules();
        rules.setHardRuleWeight(100);
        rules.addRule(new TestRule());

        TimeTableProblem problem = new TimeTableProblem();
        problem.setCourses(courses);
        problem.setTeachers(teachers);
        problem.setClasses(classes);
        problem.setHours(24);
        problem.setDays(7);
        problem.setRules(rules);

        Truncation selection = new Truncation();
        selection.setTopPercent(20);

        DayTimeOriented crossover = new DayTimeOriented();
        crossover.setCuttingPoints(5);

        Flipping mutation1 = new Flipping();
        mutation1.setComponent("H");
        mutation1.setMaxTuples(5);
        Flipping mutation2 = new Flipping();
        mutation2.setComponent("D");
        mutation2.setMaxTuples(3);
        Flipping mutation3 = new Flipping();
        mutation3.setComponent("C");
        mutation3.setMaxTuples(3);

        int popSize = 100;
        int gens = 50000;

        evolutionAlgorithm.setPopulationSize(popSize);
        evolutionAlgorithm.setProblem(problem);
        evolutionAlgorithm.setSelection(selection);
        evolutionAlgorithm.setCrossover(crossover);
        evolutionAlgorithm.addMutation(mutation1);
        evolutionAlgorithm.addMutation(mutation2);
        evolutionAlgorithm.addMutation(mutation3);

        evolutionAlgorithm.runAlgorithm(gens);
    }

    // TODO:  * MAYBE * transfer the String return to ENUM. And let the api handle the messages
    public String loadXMLFile(String filePath) {
        if (filePath.endsWith(".xml")) {
            return "The fine is not an xml file.";
        }

        File xmlFile = new File(filePath);
        if (!xmlFile.exists()) {
            return "File not exists in the current path.";
        }

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(Descriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor ettDescriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(xmlFile);


            // TODO: .................
            //=================
            // Evolution Engine
            //=================
            ETTCrossover ettCrossover = ettDescriptor.getETTEvolutionEngine().getETTCrossover();
            Crossover crossover = null;

            ETTSelection ettSelection = ettDescriptor.getETTEvolutionEngine().getETTSelection();
            Selection selection = SelectionFactory.createSelectionOperator(ettSelection.getType(), ettSelection.getConfiguration());

            evolutionAlgorithm.setCrossover(crossover);
            evolutionAlgorithm.setSelection(selection);

            //====================
            // Time table settings
            //====================
            // ...
            TimeTableProblem problem = new TimeTableProblem();
            Rules rules = new Rules();

            // ...

            problem.setRules(rules);
            evolutionAlgorithm.setProblem(problem);

        } catch (JAXBException e) {
            return "The schema is not as required." + System.lineSeparator() +
                    e.getMessage() + System.lineSeparator() +
                    Arrays.toString(e.getStackTrace());
        }

        return null;
    }
}
