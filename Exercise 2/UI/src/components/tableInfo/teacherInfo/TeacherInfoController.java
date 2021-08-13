package components.tableInfo.teacherInfo;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import logic.timeTable.Course;
import logic.timeTable.Teacher;

public class TeacherInfoController {

    private ListProperty<Course> courses = new SimpleListProperty<>();

    private Teacher displayedTeacher;

    public void setDisplayedTeacher(Teacher displayedTeacher) {
        this.displayedTeacher = displayedTeacher;
        if (displayedTeacher == null) {
            return;
        }

        this.nameLbl.setText(displayedTeacher.getName());
        this.idLbl.setText(displayedTeacher.getId());
    }

    @FXML
    private Label nameLbl;

    @FXML
    private Label idLbl;

    @FXML
    private ListView<Course> teachesViewList;

    public void setCourses(ObservableList<Course> courses) {
        this.courses.set(courses);
    }

    @FXML
    private void initialize() {
        teachesViewList.itemsProperty().bind(this.courses);
        teachesViewList.setCellFactory(lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? null : String.format("%s - %s", item.getName(), item.getId()));
            }
        });
    }
}
