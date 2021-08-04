package api.consoleApp;

import engine.base.Mutation;
import logic.evoEngineSettingsWrapper;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.Teacher;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import logic.timeTable.rules.base.Rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EvoInfoOutput {

    private EvoInfoOutput() { // No need an instance
    }

    private static final String INDENTS = "  ";

    public static String getCoursesDetails(evoEngineSettingsWrapper evoEngineSettings) {
        List<Course> courses = new ArrayList<>(evoEngineSettings.getCourses());
        StringBuilder strBuilder = new StringBuilder();

        courses.sort(Course::compareByID);
        strBuilder.append(String.format("Number of courses: %d%n", courses.size()));

        // Write a list of the courses
        int index = 1;
        for (Course course : courses) {
            strBuilder.append(String.format("%s%2d. %s - %s%n", INDENTS, index, course.getName(), course.getId()));
            index += 1;
        }

        return strBuilder.toString();
    }

    public static String getTeachersDetails(evoEngineSettingsWrapper evoEngineSettings) {
        List<Teacher> teachers = new ArrayList<>(evoEngineSettings.getTeachers());
        StringBuilder strBuilder = new StringBuilder();

        teachers.sort(Teacher::compareByID);
        strBuilder.append(String.format("Number of teachers: %d%n", teachers.size()));

        // Write the required information about every teacher
        for (Teacher teacher : teachers) {
            strBuilder.append(String.format("%sTeacher ID: %s (%s)%n",
                    INDENTS, teacher.getId(), teacher.getName()));
            strBuilder.append(String.format("%s%sTeaches the courses (total %d):%n",
                    INDENTS, INDENTS, teacher.getTeachesCoursesIDs().size()));

            // Write all the courses this teacher teaches
            for (String courseID : teacher.getTeachesCoursesIDs()) {
                Course course = evoEngineSettings.findCourse(courseID);

                if (course == null) {
                    strBuilder.append(String.format("%s%s%sCourse ID %s NOT FOUND in courses database%n",
                            INDENTS, INDENTS, INDENTS, courseID));
                } else {
                    strBuilder.append(String.format("%s%s%s%s - %s%n",
                            INDENTS, INDENTS, INDENTS, course.getName(), courseID));
                }
            }
        }

        return strBuilder.toString();
    }

    public static String getClassesDetails(evoEngineSettingsWrapper evoEngineSettings) {
        List<Class> classes = new ArrayList<>(evoEngineSettings.getClasses());
        StringBuilder strBuilder = new StringBuilder();

        classes.sort(Class::compareByID);
        strBuilder.append(String.format("Number of classes: %d%n", classes.size()));

        // Write all the information about every class
        for (Class sclass : classes) {
            strBuilder.append(String.format("%sClass ID: %s (%s)%n",
                    INDENTS, sclass.getId(), sclass.getName()));
            strBuilder.append(String.format("%s%sCourses in schedule (total %d):%n",
                    INDENTS, INDENTS, sclass.getCourseID2Hours().size()));

            // Write the courses for the current class
            for (Map.Entry<String, Integer> courseID2Hours : sclass.getCourseID2Hours().entrySet()) {
                Course course = evoEngineSettings.findCourse(courseID2Hours.getKey());

                if (course == null) {
                    strBuilder.append(String.format("%s%s%sCourse ID %s NOT FOUND in courses database%n",
                            INDENTS, INDENTS, INDENTS, courseID2Hours.getKey()));
                } else {
                    strBuilder.append(String.format("%s%s%s(%s) %s - %s hours%n",
                            INDENTS, INDENTS, INDENTS, course.getId(), course.getName(), courseID2Hours.getValue()));
                }
            }
        }

        return strBuilder.toString();
    }

    public static String getRulesDetails(evoEngineSettingsWrapper evoEngineSettings) {
        Rules<TimeTable> rules = evoEngineSettings.getRules();
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(String.format("Number of rules: %d%n", rules.getListOfRules().size()));
        int index = 1;
        for (Rule<TimeTable> rule : rules.getListOfRules()) {
            strBuilder.append(String.format("%s%2d. %s - %s%n", INDENTS, index, rule.getId(), rule.getType()));
            index++;
        }

        return strBuilder.toString();
    }

    public static String getEvoAlgorithmDetails(evoEngineSettingsWrapper algorithmSettings) {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(String.format("Population size: %d%n", algorithmSettings.getPopulationSize()));
        strBuilder.append(String.format("Selection operator: %s%n", algorithmSettings.getSelection().toString()));
        strBuilder.append(String.format("Crossover operator: %s%n", algorithmSettings.getCrossover().toString()));
        strBuilder.append(String.format("Mutations operators: %d%n", algorithmSettings.getMutations().size()));

        int index = 1;
        for (Mutation<TimeTable> mutation : algorithmSettings.getMutations()) {
            strBuilder.append(String.format("%s%d. %s%n", INDENTS, index, mutation.toString()));
            index++;
        }

        return strBuilder.toString();
    }
}
