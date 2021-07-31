package logic.timeTable;

import java.io.Serializable;
import java.util.Objects;

// The data object that represents a specific time in the time table
public class Lesson implements Cloneable, Serializable {

    private Class aClass;
    private Teacher teacher;
    private Course course;
    private int hour;
    private int day;

    public final Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public final Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public final Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "aClass=" + aClass +
                ", teacher=" + teacher +
                ", course=" + course +
                ", hour=" + hour +
                ", day=" + day +
                '}' + System.lineSeparator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return hour == lesson.hour &&
                day == lesson.day &&
                Objects.equals(aClass, lesson.aClass) &&
                Objects.equals(teacher, lesson.teacher) &&
                Objects.equals(course, lesson.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aClass, teacher, course, hour, day);
    }

    @Override
    public Lesson clone() throws CloneNotSupportedException {
//        Lesson l = (Lesson) super.clone();
//        l.teacher = this.teacher.clone();
//        l.course = this.course.clone();
//        l.aClass = this.aClass.clone();
        return (Lesson) super.clone();
    }

    public int compareByDHCTS(Lesson lesson) {
        if (this.getDay() < lesson.getDay()) {
            return -1;
        }
        if (this.getDay() > lesson.getDay()) {
            return 1;
        }

        if (this.getHour() < lesson.getHour()) {
            return -1;
        }
        if (this.getHour() > lesson.getHour()) {
            return 1;
        }

        if (this.getaClass().getId().length() < lesson.getaClass().getId().length()) {
            return -1;
        }

        if (this.getaClass().getId().length() > lesson.getaClass().getId().length()) {
            return 1;
        }

        if (this.getaClass().getId().compareTo(lesson.getaClass().getId()) < 0) {
            return -1;
        }
        if (this.getaClass().getId().compareTo(lesson.getaClass().getId()) > 0) {
            return 1;
        }

        if (this.getTeacher().getId().length() < lesson.getTeacher().getId().length()) {
            return -1;
        }

        if (this.getTeacher().getId().length() > lesson.getTeacher().getId().length()) {
            return 1;
        }

        return this.getTeacher().getId().compareTo(lesson.getTeacher().getId());
    }
}
