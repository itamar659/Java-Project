package api.consoleApp;

import logic.*;

import java.util.*;

public class EngineOutput {

    private static final String newLine = System.lineSeparator();
    private static final String indents = "   ";

    public EngineOutput(Engine engine) {
    }

    public static String getCoursesDetails(Schedule schedule) {
        List<Course> courses = schedule.getCourses();
        StringBuilder strBuilder = new StringBuilder();

        courses.sort(Comparator.comparing(Course::getCourseID));
        strBuilder.append(String.format("Number of courses: %d%s", courses.size(), newLine));

        // Write a list of the courses
        int index = 1;
        for (Course course : courses) {
            strBuilder.append(String.format("%s%d. %s - %s%s", indents, index, course.getName(), course.getCourseID(), newLine));
            index += 1;
        }

        return strBuilder.toString();
    }

    public static String getTeachersDetails(Schedule schedule) {
        List<Teacher> teachers = schedule.getTeachers();
        StringBuilder strBuilder = new StringBuilder();

        teachers.sort(Comparator.comparing(Teacher::getTeacherID));
        strBuilder.append(String.format("Number of teachers: %d%s", teachers.size(), newLine));

        // Write the required information about every teacher
        for (Teacher teacher : teachers) {
            strBuilder.append(String.format("%sTeacher ID: %s (%s)%s",
                    indents, teacher.getTeacherID(), teacher.getName(), newLine));
            strBuilder.append(String.format("%sTeaches the courses (total %d):%s",
                    indents, teacher.getTeachesCoursesIDs().size(), newLine));

            // Write all the courses this teacher teaches
            for (String courseID : teacher.getTeachesCoursesIDs()) {
                Course course = schedule.findCourse(courseID);

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

    public static String getClassesDetails(Schedule schedule) {
        List<StudentsClass> classes = schedule.getClasses();
        StringBuilder strBuilder = new StringBuilder();

        classes.sort(Comparator.comparing(StudentsClass::getClassID));
        strBuilder.append(String.format("Number of classes: %d%s", classes.size(), newLine));

        // Write all the information about every class
        for (StudentsClass sclass : classes) {
            strBuilder.append(String.format("%sClass ID: %s (%s)%s",
                    indents, sclass.getClassID(), sclass.getName(), newLine));
            strBuilder.append(String.format("%sCourses in schedule (total %d):%s",
                    indents, sclass.getCourseID2Hours().size(), newLine));

            // Write the courses for the current class
            for (Map.Entry<String, Integer> courseID2Hours : sclass.getCourseID2Hours().entrySet()) {
                Course course = schedule.findCourse(courseID2Hours.getKey());

                if (course == null) {
                    strBuilder.append(String.format("%s%sCourse ID %s NOT FOUND in courses database%s",
                            indents, indents, courseID2Hours.getKey(), newLine));
                } else {
                    strBuilder.append(String.format("%s%sLearning %s hours %s - %s%s",
                            indents, indents, courseID2Hours.getValue(), course.getName(), course.getCourseID(), newLine));
                }
            }
        }

        return strBuilder.toString();
    }
}
