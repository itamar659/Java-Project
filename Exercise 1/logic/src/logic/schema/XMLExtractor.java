package logic.schema;

import logic.Parameterizable;
import logic.evoAlgorithm.base.Crossover;
import logic.evoAlgorithm.base.Mutation;
import logic.evoAlgorithm.base.Selection;
import logic.evoAlgorithm.timeTableEvolution.crossovers.DayTimeOriented;
import logic.evoAlgorithm.timeTableEvolution.factory.CrossoverFactory;
import logic.evoAlgorithm.timeTableEvolution.factory.MutationFactory;
import logic.evoAlgorithm.timeTableEvolution.factory.RuleFactory;
import logic.evoAlgorithm.timeTableEvolution.factory.SelectionFactory;
import logic.schema.generated.*;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.HasId;
import logic.timeTable.Teacher;
import logic.timeTable.rules.base.Rule;
import logic.timeTable.rules.base.Rules;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.*;

public class XMLExtractor {

    private ETTDescriptor ettDescriptor;

    public void initializeJAXB(File xmlFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ETTDescriptor.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        this.ettDescriptor = (ETTDescriptor) jaxbUnmarshaller.unmarshal(xmlFile);
    }

    public Selection extractSelectionOperator() throws XMLExtractException {
        ETTSelection ettSelection = ettDescriptor.getETTEvolutionEngine().getETTSelection();

        // Step 1 - create the object
        SelectionFactory factory = new SelectionFactory();
        Selection selection = factory.create(ettSelection.getType());
        if (selection == null) {
            throw new XMLExtractException(String.format("There is no selection type of '%s', or type not given.", ettSelection.getType()));
        }
        // Step 2 - set the configurations (object parameters) if there are any
        setParametersIfPossible(selection, ettSelection.getConfiguration());

        // Step 3 - return the object
        return selection;
    }

    public Crossover extractCrossoverOperator() throws XMLExtractException {
        ETTCrossover ettCrossover = ettDescriptor.getETTEvolutionEngine().getETTCrossover();

        // Step 1 - create the object
        CrossoverFactory factory = new CrossoverFactory();
        Crossover crossover = factory.create(ettCrossover.getName());
        if (crossover == null) {
            throw new XMLExtractException(String.format("There is no crossover named '%s', or name not given.", ettCrossover.getName()));
        }

        // Step 2 - set the configurations (object parameters) if there are any
        setParametersIfPossible(crossover, ettCrossover.getConfiguration());

        // Step 2.1 - set DayTimeOriented parameter
        if (crossover instanceof DayTimeOriented) {
            if (ettCrossover.getCuttingPoints() < 0) {
                throw new XMLExtractException("Cutting-Points is a non-negative number.");
            }

            ((DayTimeOriented) crossover).setCuttingPoints(ettCrossover.getCuttingPoints());
        }

        // Step 3 - return the object
        return crossover;
    }

    public Set<Mutation> extractMutationsOperator() throws XMLExtractException {
        ETTMutations ettMutations = ettDescriptor.getETTEvolutionEngine().getETTMutations();

        MutationFactory factory = new MutationFactory();
        Set<Mutation> mutations = new HashSet<>();

        for (ETTMutation ettMutation : ettMutations.getETTMutation()) {
            // Step 1 - create the object
            Mutation mutation = factory.create(ettMutation.getName());
            if (mutation == null) {
                throw new XMLExtractException(String.format("There is no mutation named '%s', or name not given.", ettMutation.getName()));
            }

            if (ettMutation.getProbability() < 0 || ettMutation.getProbability() > 1) {
                throw new XMLExtractException(String.format("mutation '%s' probability should be between 0 to 1.", ettMutation.getName()));
            }

            mutation.setProbability(ettMutation.getProbability());

            mutations.add(mutation);

            // Step 2 - set the configurations (object parameters) if there are any
            setParametersIfPossible(mutation, ettMutation.getConfiguration());
        }

        // Step 3 - return the object
        return mutations;
    }

    public Rules extractRules() throws XMLExtractException {
        ETTRules ettRules = ettDescriptor.getETTTimeTable().getETTRules();

        RuleFactory factory = new RuleFactory();
        Rules rules = new Rules();

        if (ettRules.getHardRulesWeight() < 0 || ettRules.getHardRulesWeight() > 100) {
            throw new XMLExtractException("Hard Rules Weight has to be a value between 0-100.");
        }

        rules.setHardRuleWeight(ettRules.getHardRulesWeight());

        for (ETTRule ettRule : ettRules.getETTRule()) {
            // Step 1 - create the object
            Rule rule = factory.create(ettRule.getETTRuleId());
            if (rule == null) {
                throw new XMLExtractException(String.format("There is no rule ID '%s', or ID not given.", ettRule.getETTRuleId()));
            }else if (ettRule.getType() == null) {
                throw new XMLExtractException(String.format("Rule '%s' doesn't have a type.", ettRule.getETTRuleId()));
            } else if (rules.getListOfRules().contains(rule)) {
                throw new XMLExtractException(String.format("Rule '%s' already exists.", ettRule.getETTRuleId()));
            }

            // Step 2 - set the configurations (object parameters) if there are any
            if (ettRule.getType().equalsIgnoreCase(Rules.RULE_TYPE.HARD.name())) {
                rule.setType(Rules.RULE_TYPE.HARD);
            } else {
                rule.setType(Rules.RULE_TYPE.SOFT);
            }

            rules.addRule(rule);

            if (ettRule.getETTConfiguration() != null) {
                setParametersIfPossible(rule, String.join(",", ettRule.getETTConfiguration()));
            }
        }

        // Step 3 - return the object
        return rules;
    }

    public int extractPopulationSize() throws XMLExtractException {
        int popSize = ettDescriptor.getETTEvolutionEngine().getETTInitialPopulation().getSize();
        if (popSize <= 0) {
            throw new XMLExtractException("Population size have to be positive number.");
        }

        return popSize;
    }

    public int extractDays() throws XMLExtractException {
        int days = ettDescriptor.getETTTimeTable().getDays();
        if (days < 0 || days > 7) {
            throw new XMLExtractException("Studies days has to be between 0-7");
        }
        return days;
    }

    public int extractHours() throws XMLExtractException {
        int hours = ettDescriptor.getETTTimeTable().getHours();
        if (hours < 0 || hours > 24) {
            throw new XMLExtractException("Hours value has to be between 0-24");
        }
        return ettDescriptor.getETTTimeTable().getHours();
    }

    public List<Course> extractCourses() throws XMLExtractException {
        ETTSubjects ettSubjects = ettDescriptor.getETTTimeTable().getETTSubjects();

        List<Course> courses = new ArrayList<>();

        for (ETTSubject ettSubject : ettSubjects.getETTSubject()) {
            Course course = new Course();
            course.setId(Integer.toString(ettSubject.getId()));
            course.setName(ettSubject.getName());

            courses.add(course);
        }

        validateSequence(Collections.unmodifiableList(courses));

        return courses;
    }

    public List<Teacher> extractTeachers(List<Course> courses) throws XMLExtractException {
        ETTTeachers ettTeachers = ettDescriptor.getETTTimeTable().getETTTeachers();

        List<Teacher> teachers = new ArrayList<>();

        for (ETTTeacher ettTeacher : ettTeachers.getETTTeacher()) {
            Teacher teacher = new Teacher();
            teacher.setId(Integer.toString(ettTeacher.getId()));
            teacher.setName(ettTeacher.getETTName());

            for (ETTTeaches ettTeaches : ettTeacher.getETTTeaching().getETTTeaches()) {
                String courseID = Integer.toString(ettTeaches.getSubjectId());

                if (courses.stream().noneMatch(course -> course.getId().equals(courseID))) {
                    throw new XMLExtractException(String.format("The teacher '%s' teaches course id: '%s', that does not exists.", teacher.getName(), courseID));
                }

                teacher.addCourseToTeach(courseID);
            }

            teachers.add(teacher);
        }

        validateSequence(Collections.unmodifiableList(teachers));

        return teachers;
    }

    public List<Class> extractClasses(List<Course> courses, int totalTime) throws XMLExtractException {
        ETTClasses ettClasses = ettDescriptor.getETTTimeTable().getETTClasses();

        List<Class> classes = new ArrayList<>();

        for (ETTClass ettClass : ettClasses.getETTClass()) {
            Class aclass = new Class();
            aclass.setId(Integer.toString(ettClass.getId()));
            aclass.setName(ettClass.getETTName());

            int studyHours = 0;
            for (ETTStudy ettStudy : ettClass.getETTRequirements().getETTStudy()) {
                String courseID = Integer.toString(ettStudy.getSubjectId());

                if (courses.stream().noneMatch(course -> course.getId().equals(courseID))) {
                    throw new XMLExtractException(String.format("Class '%s' try to study '%s', that does not exists.", aclass.getName(), courseID));
                }

                studyHours+= ettStudy.getHours();
                aclass.addCourseToLearn(courseID, ettStudy.getHours());
            }

            if (studyHours > totalTime) {
                throw new XMLExtractException(String.format("The class studies more hours than allowed. studies %d hours", studyHours));
            }

            classes.add(aclass);
        }

        validateSequence(Collections.unmodifiableList(classes));

        return classes;
    }


    // --- HELPERS ---

    private void validateSequence(List<HasId> hasIds) throws XMLExtractException {
        int n = hasIds.size();
        if (hasIds.stream().mapToInt(t -> Integer.parseInt(t.getId())).sum() != n * (n + 1) / 2) {
            throw new XMLExtractException(String.format("%s list ids are not in ordinary sequence.", hasIds.get(0).getClass().getSimpleName()));
        }
    }

    private  void setParametersIfPossible(Object object, String configuration) throws XMLExtractException {
        // Step 2 - get the configuration (if possible)
        String[][] parameterNameNValue = createConfiguration(configuration);

        // Step 2.1 - set the configurations (object parameters) if there are any
        if (object instanceof Parameterizable && parameterNameNValue != null) {
            setParameterizableParameters((Parameterizable) object, parameterNameNValue);
        }
    }

    private String[][] createConfiguration(String configuration) {
        if (configuration == null) {
            return null;
        }

        String[] configs = configuration.split(",");
        String[][] parameters = new String[configs.length][];

        for (int i = 0; i < configs.length; i++) {
            parameters[i] = configs[i].split("=");
        }

        return parameters;
    }

    private void setParameterizableParameters(Parameterizable parameterizable, String[][] configuration) throws XMLExtractException {
        for (String[] parameterNameValue : configuration) {
            if (parameterNameValue.length != 2) {
                throw new XMLExtractException(String.format("Sent to '%s' wrong configuration.", parameterizable.getClass().getSimpleName()));
            }
            String paramName = parameterNameValue[0];
            String paramValue = parameterNameValue[1];

            try {
                parameterizable.setValue(paramName, paramValue);

            } catch (IllegalArgumentException e) {
                throw new XMLExtractException(
                        String.format("Sent to '%s' a bad value for the parameter: %s.", parameterizable.getClass().getSimpleName(), paramName), e);
            } catch (ClassCastException e) {
                throw new XMLExtractException(
                        String.format("Sent to '%s' a wrong parameter type for the parameter value: %s.", parameterizable.getClass().getSimpleName(), paramName), e);
            }
        }
    }
}
