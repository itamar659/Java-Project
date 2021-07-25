package api.consoleApp;

import logic.timeTable.*;
import logic.timeTable.Class;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TimeTableInfoOutput {

    public static String bestResultAsRAW(TimeTable bestResult) {
        final String headerFormat = "%-8s|%-8s|%-8s|%-8s|%-8s%n";
        StringBuilder strBuilder = new StringBuilder();

        // Write the headers
        strBuilder.append(String.format("Found %d lessons in this time table.%n", bestResult.getLessons().size()));
        strBuilder.append(String.format(headerFormat, "Day", "Hour", "Class", "Teacher", "Course"));
        strBuilder.append(String.format("%s%n", createLineSeparator(9 * 5)));

        // Write the table
        bestResult.getLessons().sort(Lesson::compareByDHCTS); // Sort by D-H-CLASS-T-COURSE
        for (Lesson lesson : bestResult.getLessons()) {
            strBuilder.append(String.format(headerFormat,
                    lesson.getDay(), lesson.getHour(), lesson.getaClass().getId(), lesson.getTeacher().getId(), lesson.getCourse().getId()));
        }

        strBuilder.append(System.lineSeparator());
        strBuilder.append(getIndexInformation(bestResult, true, true, true));

        return strBuilder.toString();
    }

    public static String bestResultTEACHER(TimeTable bestResult) {
        StringBuilder strBuilder = new StringBuilder();

        for (Map.Entry<Teacher, List<Lesson>> teacherTimeTable : bestResult.getTeachersTimeTable().entrySet()) {
            // Which teacher
            strBuilder.append(String.format("Time table for teacher %s: (%s)%n",
                    teacherTimeTable.getKey().getId(), teacherTimeTable.getKey().getName()));

            // Write the headers (+ days)
            addTimeTableHeader(strBuilder, bestResult.getProblem().getDays());

            // Write the schedule
            strBuilder.append(createScheduleString(
                    bestResult.getProblem().getDays(), bestResult.getProblem().getHours(), teacherTimeTable.getValue(),
                    lesson -> lesson.getaClass().getId()));

            strBuilder.append(System.lineSeparator());
        }

        strBuilder.append(String.format("Each cell contains a tuple: (Class ID, Course ID)%n"));
        strBuilder.append(getIndexInformation(bestResult, true, false, true));

        return strBuilder.toString();
    }

    public static String bestResultCLASS(TimeTable bestResult) {
        StringBuilder strBuilder = new StringBuilder();

        for (Map.Entry<Class, List<Lesson>> classesTimeTable : bestResult.getClassesTimeTable().entrySet()) {
            // Which teacher
            strBuilder.append(String.format("Time table for class %s: (%s)%n",
                    classesTimeTable.getKey().getId(), classesTimeTable.getKey().getName()));

            // Write the headers (+ days)
            addTimeTableHeader(strBuilder, bestResult.getProblem().getDays());

            // Write the schedule
            strBuilder.append(createScheduleString(
                    bestResult.getProblem().getDays(), bestResult.getProblem().getHours(), classesTimeTable.getValue(),
                    lesson -> lesson.getTeacher().getId()));

            strBuilder.append(String.format("Total study hours: %d%n%n",
                    calculateClassHours(classesTimeTable.getKey(), classesTimeTable.getValue())));
        }

        strBuilder.append(String.format("Each cell contains a tuple: (Teacher ID, Course ID)%n"));
        strBuilder.append(getIndexInformation(bestResult, false, true, true));

        return strBuilder.toString();
    }

    private static void addTimeTableHeader(StringBuilder strBuilder, int days) {
        strBuilder.append(String.format("|%-10s|", "Hour\\Day"));
        for (int i = 0; i < days; i++) {
            strBuilder.append(String.format("%-10d|", i));
        }
        strBuilder.append(String.format("%n%s%n", createLineSeparator(11 * (days + 1) + 1)));
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
            strBuilder.append(String.format("%n%s%n", createLineSeparator(11 * (days + 1) + 1)));
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

    private static int calculateClassHours(Class checkClass, List<Lesson> lessons) {
        int hours = 0;
        for (Lesson lesson : lessons) {
            if (checkClass.equals(lesson.getaClass())) {
                hours++;
            }
        }
        return hours;
    }

    private static String createLineSeparator(int length) {
        StringBuilder strB = new StringBuilder();
        for (int i = 0; i < length; i++) {
            strB.append("-");
        }
        return strB.toString();
    }

    private static String getIndexInformation(TimeTable timeTable, boolean includeClasses, boolean includeTeachers, boolean includeCourses) {
        final String infoStyleFormat = "%s - %s%n";
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(String.format("Information about the indexes:%n"));

        if (includeClasses) {
            strBuilder.append(String.format("Classes:%n"));
            for (Class aclass : timeTable.getProblem().getClasses()) {
                strBuilder.append(String.format(infoStyleFormat, aclass.getId(), aclass.getName()));
            }
            strBuilder.append(System.lineSeparator());
        }

        if (includeTeachers) {
            strBuilder.append(String.format("Teachers:%n"));
            for (Teacher teacher : timeTable.getProblem().getTeachers()) {
                strBuilder.append(String.format(infoStyleFormat, teacher.getId(), teacher.getName()));
            }
            strBuilder.append(System.lineSeparator());
        }

        if (includeCourses) {
            strBuilder.append(String.format("Courses:%n"));
            for (Course course : timeTable.getProblem().getCourses()) {
                strBuilder.append(String.format(infoStyleFormat, course.getId(), course.getName()));
            }
        }

        return strBuilder.toString();
    }
}
