package components.timeTable.lessonsInfo;

import components.timeTable.LessonsInfoResourcesConsts;
import components.timeTable.lessonInfo.LessonInfoController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import logic.timeTable.Lesson;

import java.io.IOException;
import java.util.ArrayList;

public class LessonsInfoController {

    private final ListProperty<Lesson> lessons;
    private final ObservableList<Lesson> lessonsArray;

    public LessonsInfoController() {
        lessonsArray = FXCollections.observableArrayList(new ArrayList<Lesson>());
        lessons = new SimpleListProperty<>(lessonsArray);
    }

    private boolean displayTeachers = true;

    public void setDisplayTeachers(boolean displayTeachers) {
        this.displayTeachers = displayTeachers;
    }

    public void setLessons(ObservableList<Lesson> lessons) {
        this.lessons.set(lessons);
        onLessonsChanged();
    }

    public void addLesson(Lesson lesson) {
        this.lessonsArray.add(lesson);
        onLessonsChanged();
    }

    private void onLessonsChanged() {
        lessonsOptionComboBox.getSelectionModel().selectFirst();
        selectedLessonChanged(null);
    }

    @FXML
    private void initialize() {
        lessonsOptionComboBox.setCellFactory(lv -> new LessonCell());
        lessonsOptionComboBox.setButtonCell(new LessonCell());
        lessonsOptionComboBox.itemsProperty().bind(this.lessons);
        lessonsOptionComboBox.managedProperty().bind(lessonsOptionComboBox.visibleProperty());
        lessons.sizeProperty().addListener((observable, oldValue, newValue) -> {
            lessonsOptionComboBox.setVisible(this.lessons.size() > 1);
        });
    }

    @FXML
    private ComboBox<Lesson> lessonsOptionComboBox;

    @FXML
    private Pane lessonInfoAnchor;

    @FXML
    public void selectedLessonChanged(ActionEvent event) {
        lessonInfoAnchor.getChildren().clear();
        if (lessonsOptionComboBox.getItems().size() == 0) {
            return;
        }

        createLessonsBox();
    }

    private void createLessonsBox() {
        try {
            Lesson chosen = lessonsOptionComboBox.getValue();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LessonsInfoResourcesConsts.SINGLE_LESSON_TT_FXML_RESOURCE);
            Node lessInfo = loader.load();

            LessonInfoController controller = loader.getController();
            controller.setCourse(chosen.getCourse());
            if (displayTeachers) {
                controller.setTeacher(chosen.getTeacher());
            } else {
                controller.setaClass(chosen.getaClass());
            }

            lessonInfoAnchor.getChildren().add(lessInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class LessonCell extends ListCell<Lesson> {

        @Override
        protected void updateItem(Lesson item, boolean empty) {
            super.updateItem(item, empty);

            textProperty().unbind();
            if (empty || item == null) {
                setText("ERROR - The object is empty...");
            } else {
                textProperty().bind(Bindings.format("Lesson - %d", getIndex()));
            }
        }
    }
}
