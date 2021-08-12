package components.timeTable.lessonInfo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import logic.timeTable.Class;
import logic.timeTable.Course;
import logic.timeTable.Teacher;

public class LessonInfoController {

    @FXML
    private Label topLabelInfo;

    @FXML
    private Label botLabelInfo;

    @FXML
    private Label topLabelDetails;

    @FXML
    private Label botLabelDetails;

    private Teacher teacher;
    private Class aClass;
    private Course course;

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;

        topLabelInfo.setText("Teacher:");
        topLabelDetails.setText(String.format("%s (%s)", teacher.getName(), teacher.getId()));
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;

        topLabelInfo.setText("Class:");
        topLabelDetails.setText(String.format("%s (%s)", aClass.getName(), aClass.getId()));
    }

    public void setCourse(Course course) {
        this.course = course;

        botLabelInfo.setText("Course");
        botLabelDetails.setText(String.format("%s (%s)", course.getName(), course.getId()));
    }
}
