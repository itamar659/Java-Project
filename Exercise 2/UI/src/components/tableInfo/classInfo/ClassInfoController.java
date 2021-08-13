package components.tableInfo.classInfo;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import logic.timeTable.Class;
import logic.timeTable.Course;

public class ClassInfoController {

    private final ListProperty<Course> courses = new SimpleListProperty<>();

    private Class displayedClass;

    public void setDisplayedClass(Class displayedClass) {
        this.displayedClass = displayedClass;
        if (displayedClass == null) {
            return;
        }

        this.nameLbl.setText(displayedClass.getName());
        this.idLbl.setText(displayedClass.getId());
    }

    @FXML
    private Label nameLbl;

    @FXML
    private Label idLbl;

    @FXML
    private ListView<Course> studiesViewList;

    public void setCourses(ObservableList<Course> courses) {
        this.courses.set(courses);
    }

    @FXML
    private void initialize() {
        studiesViewList.itemsProperty().bind(this.courses);
        studiesViewList.setCellFactory(lv -> new ListCell<Course>() {
            @Override
            protected void updateItem(Course item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? null : String.format("%s - %s", item.getName(), item.getId()));
            }
        });
    }
}
