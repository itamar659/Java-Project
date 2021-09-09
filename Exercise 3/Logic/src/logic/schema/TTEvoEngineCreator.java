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

    public static EvolutionEngine<TimeTable> createEngineFromXMLString(String xmlFileAsString) throws JAXBException, XMLExtractException {
        EvolutionEngine<TimeTable> evoEngine = new TimeTableEvolutionEngine();
        evoEngine.setProblem(createProblemFromXMLString(xmlFileAsString));

        return evoEngine;
    }

    public static EvolutionEngine<TimeTable> createEngineFromProblem(Problem<TimeTable> problem) {
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
        List<Teacher> teachers = xmlExtractor.extractTeachers(courses, days * hours);
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
