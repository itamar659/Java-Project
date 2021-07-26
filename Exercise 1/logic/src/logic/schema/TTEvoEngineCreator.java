package logic.schema;

import logic.evoAlgorithm.base.Crossover;
import logic.evoAlgorithm.base.EvolutionEngine;
import logic.evoAlgorithm.base.Mutation;
import logic.evoAlgorithm.base.Selection;
import logic.evoAlgorithm.timeTableEvolution.TimeTableEvolutionEngine;
import logic.evoAlgorithm.timeTableEvolution.TimeTableProblem;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.Teacher;
import logic.timeTable.rules.base.Rules;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class TTEvoEngineCreator implements Serializable {

    private EvolutionEngine lastCreatedTTEEngine;

    public EvolutionEngine getLastCreatedTTEEngine() {
        return lastCreatedTTEEngine;
    }

    public void createFromXMLFile(File xmlFile) throws JAXBException, XMLExtractException {
        XMLExtractor xmlExtractor = new XMLExtractor();
        xmlExtractor.initializeJAXB(xmlFile);

        // --- Extract Time table problem ---

        int days = xmlExtractor.extractDays();
        int hours = xmlExtractor.extractHours();
        List<Course> courses = xmlExtractor.extractCourses();
        List<Teacher> teachers = xmlExtractor.extractTeachers(courses);
        List<Class> classes = xmlExtractor.extractClasses(courses, days * hours);
        Rules rules = xmlExtractor.extractRules();

        // Initialize the problem

        TimeTableProblem problem = new TimeTableProblem();
        problem.setDays(days);
        problem.setHours(hours);
        problem.setCourses(courses);
        problem.setTeachers(teachers);
        problem.setClasses(classes);
        problem.setRules(rules);

        // --- Extract Evolution Engine Modifications ---

        Selection selection = xmlExtractor.extractSelectionOperator();
        Crossover crossover = xmlExtractor.extractCrossoverOperator();
        Set<Mutation> mutations = xmlExtractor.extractMutationsOperator();
        int populationSize = xmlExtractor.extractPopulationSize();

        // Initialize the Evolution engine

        EvolutionEngine evoEngine = new TimeTableEvolutionEngine();
        evoEngine.setSelection(selection);
        evoEngine.setCrossover(crossover);
        evoEngine.setMutations(mutations);
        evoEngine.setPopulationSize(populationSize);
        evoEngine.setProblem(problem);

        lastCreatedTTEEngine = evoEngine;
    }
}
