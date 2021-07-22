package api.consoleApp;

import logic.*;
import logic.evoAlgorithm.base.Mutation;
import logic.timeTable.rules.base.Rule;
import logic.timeTable.rules.base.Rules;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.Teacher;

import java.util.*;

public class EngineOutput {

    private static final String newLine = System.lineSeparator();
    private static final String indents = "   ";

    public EngineOutput(Engine engine) {
    }

    public static String getCoursesDetails(evoEngineSettingsWrapper evoEngineSettings) {
        List<Course> courses = evoEngineSettings.getCourses();
        StringBuilder strBuilder = new StringBuilder();

        courses.sort(Comparator.comparing(Course::getId));
        strBuilder.append(String.format("Number of courses: %d%s", courses.size(), newLine));

        // Write a list of the courses
        int index = 1;
        for (Course course : courses) {
            strBuilder.append(String.format("%s%d. %s - %s%s", indents, index, course.getName(), course.getId(), newLine));
            index += 1;
        }

        return strBuilder.toString();
    }

    public static String getTeachersDetails(evoEngineSettingsWrapper evoEngineSettings) {
        List<Teacher> teachers = evoEngineSettings.getTeachers();
        StringBuilder strBuilder = new StringBuilder();

        teachers.sort(Comparator.comparing(Teacher::getId));
        strBuilder.append(String.format("Number of teachers: %d%s", teachers.size(), newLine));

        // Write the required information about every teacher
        for (Teacher teacher : teachers) {
            strBuilder.append(String.format("%sTeacher ID: %s (%s)%s",
                    indents, teacher.getId(), teacher.getName(), newLine));
            strBuilder.append(String.format("%sTeaches the courses (total %d):%s",
                    indents, teacher.getTeachesCoursesIDs().size(), newLine));

            // Write all the courses this teacher teaches
            for (String courseID : teacher.getTeachesCoursesIDs()) {
                Course course = evoEngineSettings.findCourse(courseID);

                if (course == null) {
                    strBuilder.append(String.format("%s%sCourse ID %s NOT FOUND in courses database%s",
                            indents, indents, courseID, newLine));
                } else {
                    strBuilder.append(String.format("%s%s%s - %s%s",
                            indents, indents, course.getName(), courseID, newLine));
                }
            }
        }

        return strBuilder.toString();
    }

    public static String getClassesDetails(evoEngineSettingsWrapper evoEngineSettings) {
        List<Class> classes = evoEngineSettings.getClasses();
        StringBuilder strBuilder = new StringBuilder();

        classes.sort(Comparator.comparing(Class::getId));
        strBuilder.append(String.format("Number of classes: %d%s", classes.size(), newLine));

        // Write all the information about every class
        for (Class sclass : classes) {
            strBuilder.append(String.format("%sClass ID: %s (%s)%s",
                    indents, sclass.getId(), sclass.getName(), newLine));
            strBuilder.append(String.format("%sCourses in schedule (total %d):%s",
                    indents, sclass.getCourseID2Hours().size(), newLine));

            // Write the courses for the current class
            for (Map.Entry<String, Integer> courseID2Hours : sclass.getCourseID2Hours().entrySet()) {
                Course course = evoEngineSettings.findCourse(courseID2Hours.getKey());

                if (course == null) {
                    strBuilder.append(String.format("%s%sCourse ID %s NOT FOUND in courses database%s",
                            indents, indents, courseID2Hours.getKey(), newLine));
                } else {
                    strBuilder.append(String.format("%s%sLearning %s hours %s - %s%s",
                            indents, indents, courseID2Hours.getValue(), course.getName(), course.getId(), newLine));
                }
            }
        }

        return strBuilder.toString();
    }

    public static String getRulesDetails(evoEngineSettingsWrapper evoEngineSettings) {
        Rules rules = evoEngineSettings.getRules();
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(String.format("Number of rules: %d%s", rules.getListOfRules().size(), newLine));
        int index = 1;
        for (Rule rule : rules.getListOfRules()) {
            strBuilder.append(String.format("%s%d. %s - %s%s", indents, index, rule.getId(), rule.getType(), newLine));
            index++;
        }

        return strBuilder.toString();
    }

    public static String getEvoAlgorithmDetails(evoEngineSettingsWrapper algorithmSettings) {
        StringBuilder strBuilder = new StringBuilder();

        strBuilder.append(String.format("Population size: %d%s", algorithmSettings.getPopulationSize(), newLine));
        strBuilder.append(String.format("Selection operator: %s%s", algorithmSettings.getSelection().toString(), newLine));
        strBuilder.append(String.format("Crossover operator: %s%s", algorithmSettings.getCrossover().toString(), newLine));

        strBuilder.append(String.format("Num of Mutation operators: %d%s", algorithmSettings.getMutations().size(), newLine));
        int index = 1;
        for (Mutation mutation : algorithmSettings.getMutations()) {
            strBuilder.append(String.format("%s%d. %s%s", indents, index, mutation.toString(), newLine));
            index++;
        }

        return strBuilder.toString();
    }
}
