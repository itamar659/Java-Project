package components.application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import logic.evoAlgorithm.TimeTableProblem;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.Teacher;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;

public class ProblemModule {

    private TimeTableProblem theProblem;

    // General information about the problem
    private final ListProperty<Teacher> teachers = new SimpleListProperty<>();
    private final ListProperty<Class> classes = new SimpleListProperty<>();
    private final ListProperty<Course> courses = new SimpleListProperty<>();
    private final IntegerProperty days = new SimpleIntegerProperty(0);
    private final IntegerProperty hours = new SimpleIntegerProperty(0);

    // Information about the rules
    private final ListProperty<Rule<TimeTable>> rules = new SimpleListProperty<>();
    private final IntegerProperty hardRuleWeight = new SimpleIntegerProperty();

    public void setTheProblem(TimeTableProblem theProblem) {
        this.theProblem = theProblem;

        teachers.set(FXCollections.observableArrayList(theProblem.getTeachers()));
        classes.set(FXCollections.observableArrayList(theProblem.getClasses()));
        courses.set(FXCollections.observableArrayList(theProblem.getCourses()));
        days.set(theProblem.getDays());
        hours.set(theProblem.getHours());

        rules.set(FXCollections.observableArrayList(theProblem.getRules().getListOfRules()));
        hardRuleWeight.set(theProblem.getRules().getHardRuleWeight());
    }

    public ListProperty<Teacher> teachersProperty() {
        return teachers;
    }

    public ListProperty<Class> classesProperty() {
        return classes;
    }

    public ListProperty<Course> coursesProperty() {
        return courses;
    }

    public IntegerProperty daysProperty() {
        return days;
    }

    public IntegerProperty hoursProperty() {
        return hours;
    }

    public ListProperty<Rule<TimeTable>> rulesProperty() {
        return rules;
    }

    public IntegerProperty hardRuleWeightProperty() {
        return hardRuleWeight;
    }
}
