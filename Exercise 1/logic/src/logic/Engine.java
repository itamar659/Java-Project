package logic;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import logic.actions.Action;
import logic.actions.ParameterizedAction;
import logic.algorithm.TimeTableSolution;
import logic.algorithm.factory.*;
import logic.algorithm.TimeTableProblem;
import logic.algorithm.genericEvolutionAlgorithm.*;
import logic.algorithm.selections.Truncation;
import logic.algorithm.crossovers.DayTimeOriented;
import logic.algorithm.mutations.Flipping;
import logic.schema.generated.*;
import logic.algorithm.TimeTableEvolutionAlgorithm;
import logic.timeTable.*;
import logic.timeTable.Class;
import logic.timeTable.rules.base.Rule;
import logic.timeTable.rules.base.Rules;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

public class Engine {

    public enum State {
        IDLE, RUNNING, COMPLETED;
    }

    private boolean isFileLoaded;
    private State state;

    private final EvolutionAlgorithm evolutionAlgorithm;
    private final evoEngineSettingsWrapper algorithmSettings;

    public evoEngineSettingsWrapper getAlgorithmSettings() {
        return algorithmSettings;
    }

    public boolean isFileLoaded() {
        return isFileLoaded;
    }

    public State getState() {
        return state;
    }

    public void addEndOfGenerationListener(Action action) {
        evolutionAlgorithm.addEndOfGenerationListener(action);
    }

    public void addFinishAlgorithmListener(Action action) {
        evolutionAlgorithm.addFinishAlgorithmListener(action);
    }

    public  int getMaxGenerations() {
        return evolutionAlgorithm.getMaxGenerations();
    }

    public  int getCurrentGeneration() {
        return evolutionAlgorithm.getCurrentGeneration();
    }

    public void setUpdateEveryGeneration(int everyGens) {
        evolutionAlgorithm.setListenEveryGeneration(everyGens);
    }

    // TODO: Change TimeTableSolution to TimeTable ??? (Wrapper)
    public TimeTableSolution getBestResult() {
        return (TimeTableSolution) evolutionAlgorithm.getPopulation().getSolutionByIndex(0);
    }

    public Engine() {
        TimeTableEvolutionAlgorithm algorithm = new TimeTableEvolutionAlgorithm();

        this.state = State.IDLE;
        this.evolutionAlgorithm = algorithm;
        this.algorithmSettings = new evoEngineSettingsWrapper(algorithm);
    }

    public void Test() {
        // No rules!!
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
        s2.addCourseToLearn("123", 2);
        s2.addCourseToLearn("777", 8);
        List<Class> classes = new ArrayList<>();
        classes.add(s1);
        classes.add(s2);

        Rules rules = new Rules();
        rules.setHardRuleWeight(100);
        //rules.addRule(new TestRule());

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
        mutation1.setMaxTupples(5);
        Flipping mutation2 = new Flipping();
        mutation2.setComponent("D");
        mutation2.setMaxTupples(3);
        Flipping mutation3 = new Flipping();
        mutation3.setComponent("C");
        mutation3.setMaxTupples(3);

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

    // TODO: * MAYBE * transfer the String return to ENUM. And let the api handle the messages
    // TODO: * OR MAYBE * Can also return an Result object that contains if the file loads correctly or not,
    //  and if not, look in the right property to know the problem.
    public String loadXMLFile(String filePath) {
        if (!filePath.endsWith(".xml")) {
            return "The fine is not an xml file.";
        }

        File xmlFile = new File(filePath);
        if (!xmlFile.exists()) {
            return "File not exists in the current path.";
        }

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ETTDescriptor ettDescriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(xmlFile);


            // TODO: Create a parameterized interface with a method setParameter(String parameterName, Object value)

            // TODO: Errors, check for string instead of int in the arguments
            //  i.e. configuration can contain faulty string, cutting-points can be negative

            // TODO: .................
            //=================
            // Evolution Engine
            //=================

            // Initialize Selection operator
            ETTSelection ettSelection = ettDescriptor.getETTEvolutionEngine().getETTSelection();
            FactoryResult frSelection = SelectionFactory.createSelection(
                    ettSelection.getType(), createConfiguration(ettSelection.getConfiguration()));
            if (frSelection.isErrorOccurred()) {
                return frSelection.getErrorMessage();
            }

            Selection selection = (Selection) frSelection.getReturnedFactoryObject();

            // Initialize Crossover operator
            ETTCrossover ettCrossover = ettDescriptor.getETTEvolutionEngine().getETTCrossover();
            FactoryResult frCrossover = CrossoverFactory.createCrossover(ettCrossover.getName(), null);
            if (frCrossover.isErrorOccurred()) {
                return frCrossover.getErrorMessage();
            }

            Crossover crossover = (Crossover) frCrossover.getReturnedFactoryObject();
            if (crossover instanceof DayTimeOriented) {
                ((DayTimeOriented) crossover).setCuttingPoints(ettCrossover.getCuttingPoints());
            }

            // Initialize Mutations operators
            ETTMutations ettMutations = ettDescriptor.getETTEvolutionEngine().getETTMutations();
            Set<Mutation> mutations = new HashSet<>();
            for (ETTMutation ettMutation : ettMutations.getETTMutation()) {
                // Try initialize the mutation
                FactoryResult frMutation = MutationFactory.createMutation(
                        ettMutation.getName(), ettMutation.getProbability(), createConfiguration(ettMutation.getConfiguration()));
                // Check if mutation created
                if (frMutation.isErrorOccurred()) {
                    return frMutation.getErrorMessage();
                }
                // Add the mutation to the list
                mutations.add((Mutation) frMutation.getReturnedFactoryObject());
            }

            // Initialize the population size
            int populationSize = ettDescriptor.getETTEvolutionEngine().getETTInitialPopulation().getSize();
            if (populationSize <= 0) {
                return "The population cannot be non-positive number";
            }

            //====================
            // Time table settings
            //====================

            // Initialize the number of days
            int days = ettDescriptor.getETTTimeTable().getDays();

            // Initialize the number of hours
            int hours = ettDescriptor.getETTTimeTable().getHours();

            // Initialize the courses
            List<Course> courses = new ArrayList<>();
            for (ETTSubject ettSubject : ettDescriptor.getETTTimeTable().getETTSubjects().getETTSubject()) {
                Course course = new Course();
                course.setCourseID(Integer.toString(ettSubject.getId()));
                course.setName(ettSubject.getName().get(0));

                courses.add(course);
            }

            // Initialize the teachers
            List<Teacher> teachers = new ArrayList<>();
            for (ETTTeacher ettTeacher : ettDescriptor.getETTTimeTable().getETTTeachers().getETTTeacher()) {
                Teacher teacher = new Teacher();
                teacher.setTeacherID(Integer.toString(ettTeacher.getId()));

                // Check if the teacher have a name TODO: change the xml file, see if need to check it at all.
                // TODO: The strings splits by spaces.
                if (ettTeacher.getETTName() == null || ettTeacher.getETTName().size() == 0) {
                    return String.format("The teacher with the id %s, does not have a name", teacher.getTeacherID());
                }

                teacher.setName(ettTeacher.getETTName().get(0));

                for (ETTTeaches ettTeaches : ettTeacher.getETTTeaching().getETTTeaches()) {
                    String courseID = Integer.toString(ettTeaches.getSubjectId());

                    // Check if the teacher teaches a valid courses
                    if (courses.stream().noneMatch(c -> c.getCourseID().equals(courseID))) {
                        return String.format("The teacher %s teaches course id: %s, that does not exists.", teacher.getName(), courseID);
                    }

                    teacher.addCourseToTeach(courseID);
                }

                teachers.add(teacher);
            }

            // Initialize the classes
            List<Class> classes = new ArrayList<>();
            for (ETTClass ettClass : ettDescriptor.getETTTimeTable().getETTClasses().getETTClass()) {
                Class aclass = new Class();
                aclass.setClassID(Integer.toString(ettClass.getId()));

                if (ettClass.getETTName() == null || ettClass.getETTName().size() == 0) {
                    return String.format("The class with the id %s, does not have a name", aclass.getClassID());
                }

                aclass.setName(ettClass.getETTName().get(0));

                for (ETTStudy ettStudy : ettClass.getETTRequirements().getETTStudy()) {
                    String courseID = Integer.toString(ettStudy.getSubjectId());

                    // Check if the teacher teaches a valid courses
                    if (courses.stream().noneMatch(c -> c.getCourseID().equals(courseID))) {
                        return String.format("The teacher %s teaches course id: %s, that does not exists.", aclass.getName(), courseID);
                    }

                    aclass.addCourseToLearn(courseID, ettStudy.getHours());
                }

                classes.add(aclass);
            }

            // TODO: Check every class dont study more than max time

            // Initialize the rules
            Rules rules = new Rules();
            ETTRules ettRules = ettDescriptor.getETTTimeTable().getETTRules();
            for (ETTRule ettRule : ettRules.getETTRule()) {
                FactoryResult frRule = RuleFactory.createRule(ettRule.getETTRuleId(), createConfiguration(ettRule.getETTConfiguration()));

                if (frRule.isErrorOccurred()) {
                    return frRule.getErrorMessage();
                }

                Rule rule = (Rule) frRule.getReturnedFactoryObject();

                if (ettRule.getType().equalsIgnoreCase("HARD")) {
                    rule.setType(Rules.RULE_TYPE.HARD);
                } else {
                    rule.setType(Rules.RULE_TYPE.SOFT);
                }

                rules.addRule(rule);
            }

            rules.setHardRuleWeight(ettRules.getHardRulesWeight());

            // Initialize the engine
            TimeTableProblem problem = new TimeTableProblem();
            problem.setDays(ettDescriptor.getETTTimeTable().getDays());
            problem.setHours(ettDescriptor.getETTTimeTable().getHours());
            problem.setCourses(courses);
            problem.setTeachers(teachers);
            problem.setClasses(classes);
            problem.setRules(rules);

            evolutionAlgorithm.setSelection(selection);
            evolutionAlgorithm.setCrossover(crossover);
            evolutionAlgorithm.setMutations(mutations);
            evolutionAlgorithm.setPopulationSize(populationSize);
            evolutionAlgorithm.setProblem(problem);

        } catch (JAXBException e) {
            return "The schema is not as required." + System.lineSeparator() +
                    e.getMessage() + System.lineSeparator() +
                    Arrays.toString(e.getStackTrace());
        } catch (ValueException e) {
            return e.getMessage();
        }

        isFileLoaded = true;

        return null;
    }

    private String[][] createConfiguration(List<String> configuration) {
        if (configuration.size() == 0) {
            return null;
        }

        String[] configs = configuration.get(0).split(",");
        String[][] parameters = new String[configs.length][];

        for (int i = 0; i < configs.length; i++) {
            parameters[i] = configs[i].split("=");
        }

        return parameters;
    }

    public void startAlgorithm(int generations) {
        this.state = State.RUNNING;
        evolutionAlgorithm.runAlgorithm(generations);
        this.state = State.COMPLETED;
    }
}
