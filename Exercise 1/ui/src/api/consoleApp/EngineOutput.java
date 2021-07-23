package api.consoleApp;

import logic.*;
import logic.evoAlgorithm.base.Mutation;
import logic.timeTable.*;
import logic.timeTable.Class;
import logic.timeTable.rules.base.Rule;
import logic.timeTable.rules.base.Rules;

import java.util.*;
import java.util.function.Function;

public class EngineOutput {

    private static final String newLine = System.lineSeparator();
    private static final String indents = "   ";

    private EngineOutput() {
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

        strBuilder.append(String.format("Mutations operators: %d%s", algorithmSettings.getMutations().size(), newLine));
        int index = 1;
        for (Mutation mutation : algorithmSettings.getMutations()) {
            strBuilder.append(String.format("%s%d. %s%s", indents, index, mutation.toString(), newLine));
            index++;
        }

        return strBuilder.toString();
    }

    public static String bestResultAsRAW(TimeTable bestResult) {
        StringBuilder strBuilder = new StringBuilder();

        // Write the headers
        strBuilder.append(String.format("%-10s|%-10s|%-10s|%-10s|%-10s%n", "Day", "Hour", "Class", "Teacher", "Course"));

        // Write the table
        bestResult.getLessons().sort(Lesson::compareByDHCTS); // Sort by D-H-CLASS-T-COURSE
        for (Lesson lesson : bestResult.getLessons()) {
            strBuilder.append(String.format("%-10s|%-10s|%-10s|%-10s|%-10s%n",
                    lesson.getDay(), lesson.getHour(), lesson.getaClass().getId(), lesson.getTeacher().getId(), lesson.getCourse().getId()));
        }
        //TODO: Write down the meaning of the indexes.
        // Change the table size

        return strBuilder.toString();
    }

    private static List<Lesson>[][] createSchedule(int days, int hours, List<Lesson> lessons) {
        List<Lesson>[][] schedule = new ArrayList[hours][days];
        for (Lesson lesson : lessons) {
            if (schedule[lesson.getHour()][lesson.getDay()] == null) {
                schedule[lesson.getHour()][lesson.getDay()] = new ArrayList<>();
            }
            schedule[lesson.getHour()][lesson.getDay()].add(lesson);
        }

        return schedule;
    }

    private static String createScheduleString(int days, int hours, List<Lesson> lessons, Function<Lesson, String> lessonInformationId) {
        StringBuilder strBuilder = new StringBuilder(11 * (days + 1) * hours);

        // Create a schedule to print
        lessons.sort(Lesson::compareByDHCTS);
        List<Lesson>[][] schedule = createSchedule(days, hours, lessons);

        for (int hour = 0; hour < hours; hour++) {
            strBuilder.append(String.format("|%-10d|", hour));
            strBuilder.append(createHourlySchedule(days, schedule[hour], lessonInformationId));
            strBuilder.append(String.format("%n%s%n", createLine(11 * (days + 1) + 1)));
        }

        return strBuilder.toString();
    }

    private static String createHourlySchedule(int days, List<Lesson>[] dailyScheduleForHour, Function<Lesson, String> lessonInformationId) {
        StringBuilder strBuilder = new StringBuilder(11 * days);

        int maxAtSameTime = 1;
        for (int i = 0; i < maxAtSameTime; i++) {
            for (int day = 0; day < days; day++) {
                if (dailyScheduleForHour[day] != null) {
                    if (dailyScheduleForHour[day].size() > maxAtSameTime) {
                        maxAtSameTime =dailyScheduleForHour[day].size();
                    }

                    if (dailyScheduleForHour[day].size() > i) {
                        strBuilder.append(String.format("%-10s|",
                                String.format("(%s, %s)",
                                        lessonInformationId.apply(dailyScheduleForHour[day].get(i)),
                                        dailyScheduleForHour[day].get(i).getCourse().getId())));
                    } else {
                        strBuilder.append(String.format("%-10s|", ""));
                    }
                } else {
                    strBuilder.append(String.format("%-10s|", ""));
                }
            }
            if (i != maxAtSameTime - 1) {
                strBuilder.append(String.format("%n|%-10s|", ""));
            }
        }

        return strBuilder.toString();
    }

    public static String bestResultTEACHER(TimeTable bestResult) {
        StringBuilder strBuilder = new StringBuilder();

        for (Map.Entry<Teacher, List<Lesson>> teacherTimeTable : bestResult.getTeachersTimeTable().entrySet()) {
            // Which teacher
            strBuilder.append(String.format("Time table for teacher %s: (%s)%n",
                    teacherTimeTable.getKey().getId(), teacherTimeTable.getKey().getName()));

            // Write the headers (+ days)
            strBuilder.append(String.format("|%-10s|", "Hour\\Day"));
            for (int i = 0; i < bestResult.getProblem().getDays(); i++) {
                strBuilder.append(String.format("%-10d|", i));
            }
            strBuilder.append(String.format("%n%s%n", createLine(11*(bestResult.getProblem().getDays()+1)+1)));

            // Write the schedule
            strBuilder.append(createScheduleString(
                    bestResult.getProblem().getDays(), bestResult.getProblem().getHours(), teacherTimeTable.getValue(),
                    lesson -> lesson.getTeacher().getId()));

            strBuilder.append(System.lineSeparator());
        }

        return strBuilder.toString();
    }

    public static String bestResultCLASS(TimeTable bestResult) {
        StringBuilder strBuilder = new StringBuilder();

        for (Map.Entry<Class, List<Lesson>> classesTimeTable : bestResult.getClassesTimeTable().entrySet()) {
            // Which teacher
            strBuilder.append(String.format("Time table for class %s: (%s)%n",
                    classesTimeTable.getKey().getId(), classesTimeTable.getKey().getName()));

            // Write the headers (+ days)
            strBuilder.append(String.format("|%-10s|", "Hour\\Day"));
            for (int i = 0; i < bestResult.getProblem().getDays(); i++) {
                strBuilder.append(String.format("%-10d|", i));
            }
            strBuilder.append(String.format("%n%s%n", createLine(11*(bestResult.getProblem().getDays()+1)+1)));

            // Write the schedule
            strBuilder.append(createScheduleString(
                    bestResult.getProblem().getDays(), bestResult.getProblem().getHours(), classesTimeTable.getValue(),
                    lesson -> lesson.getaClass().getId()));

            strBuilder.append(System.lineSeparator());
        }

        return strBuilder.toString();
    }

    private static String createLine(int length) {
        StringBuilder strB = new StringBuilder();
        for (int i = 0; i < length; i++) {
            strB.append("-");
        }
        return strB.toString();
    }
}
