package logic.schema;

import engine.base.*;
import logic.evoAlgorithm.TimeTableEvolutionEngine;
import logic.evoAlgorithm.TimeTableProblem;
import logic.schema.exceptions.XMLExtractException;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.Teacher;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rules;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public final class TTEvoEngineCreator implements Serializable {

    private TTEvoEngineCreator() { }

    public static EvolutionEngine<TimeTable> createFromXMLString(String xmlFileAsString) throws JAXBException, XMLExtractException {
//        XMLExtractor xmlExtractor = new XMLExtractor();
//        xmlExtractor.initializeJAXB(xmlFileAsString);


        // --- Extract Time table problem ---

//        int days = xmlExtractor.extractDays();
//        int hours = xmlExtractor.extractHours();
//        List<Course> courses = xmlExtractor.extractCourses();
//        List<Teacher> teachers = xmlExtractor.extractTeachers(courses);
//        List<Class> classes = xmlExtractor.extractClasses(courses, days * hours);
//        Rules<TimeTable> rules = xmlExtractor.extractRules();
//
//
//        // Initialize the problem
//
//        TimeTableProblem problem = new TimeTableProblem();
//        problem.setDays(days);
//        problem.setHours(hours);
//        problem.setCourses(courses);
//        problem.setTeachers(teachers);
//        problem.setClasses(classes);
//        problem.setRules(rules);


        // --- Extract Evolution Engine Modifications ---

//        Selection<TimeTable> selection = xmlExtractor.extractSelectionOperator();
//        Crossover<TimeTable> crossover = xmlExtractor.extractCrossoverOperator();
//        Set<Mutation<TimeTable>> mutations = xmlExtractor.extractMutationsOperator();
//        int populationSize = xmlExtractor.extractPopulationSize();
//        int elitism = xmlExtractor.extractElitism();


        // Initialize the Evolution engine

        EvolutionEngine<TimeTable> evoEngine = new TimeTableEvolutionEngine();
        evoEngine.setProblem(createProblemFromXMLString(xmlFileAsString));
//        evoEngine.setSelection(selection);
//        evoEngine.setCrossover(crossover);
//        evoEngine.setMutations(mutations);
//        evoEngine.setElitism(elitism);
//        evoEngine.setPopulationSize(populationSize);


        return evoEngine;
    }

    public static EvolutionEngine<TimeTable> createFromProblem(Problem<TimeTable> problem) {
        EvolutionEngine<TimeTable> evoEngine = new TimeTableEvolutionEngine();
        evoEngine.setProblem(problem);
        return evoEngine;
    }

    public static TimeTableProblem createProblemFromXMLString(String xmlFileAsString) throws JAXBException, XMLExtractException {
        XMLExtractor xmlExtractor = new XMLExtractor();
        xmlExtractor.initializeJAXB(xmlFileAsString);

        // --- Extract Time table problem ---
        int days = xmlExtractor.extractDays();
        int hours = xmlExtractor.extractHours();
        List<Course> courses = xmlExtractor.extractCourses();
        List<Teacher> teachers = xmlExtractor.extractTeachers(courses);
        List<Class> classes = xmlExtractor.extractClasses(courses, days * hours);
        Rules<TimeTable> rules = xmlExtractor.extractRules();


        // Initialize the problem
        TimeTableProblem problem = new TimeTableProblem();
        problem.setDays(days);
        problem.setHours(hours);
        problem.setCourses(courses);
        problem.setTeachers(teachers);
        problem.setClasses(classes);
        problem.setRules(rules);

        return problem;
    }
}
