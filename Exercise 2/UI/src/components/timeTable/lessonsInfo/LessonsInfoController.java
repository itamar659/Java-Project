package components.timeTable.lessonsInfo;

import components.timeTable.LessonsInfoResourcesConsts;
import components.timeTable.lessonInfo.LessonInfoController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import logic.timeTable.Lesson;

import java.io.IOException;
import java.util.ArrayList;

public class LessonsInfoController {

    public static final int MAX_WIDTH = 200;
    public static final int MAX_HEIGHT = 125;


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

    public void addLesson(Lesson lesson) {
        this.lessonsArray.add(lesson);
        lessonsComboBox.getSelectionModel().selectFirst();

        if (lessonsArray.size() == 1) {
            // Create the block for this specific day/hour and bind it.
            createLessonBlock();
        }
    }

    @FXML
    private void initialize() {
        lessonsComboBox.setCellFactory(lv -> new LessonCell());
        lessonsComboBox.setButtonCell(new LessonCell());
        lessonsComboBox.itemsProperty().bind(this.lessons);
        lessonsComboBox.managedProperty().bind(lessonsComboBox.visibleProperty());
        lessons.sizeProperty().addListener((observable, oldValue, newValue) -> {
            lessonsComboBox.setVisible(this.lessons.size() > 1);
        });
    }

    @FXML
    private ComboBox<Lesson> lessonsComboBox;

    @FXML
    private Pane lessonInfoAnchor;

    private void createLessonBlock() {
        // TODO: Do something with this shit code
        // Create a new block and bind the requested lesson
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LessonsInfoResourcesConsts.SINGLE_LESSON_TT_FXML_RESOURCE);
            Node lessInfo = loader.load();

            LessonInfoController controller = loader.getController();

            if (displayTeachers) {
                controller.topDetailsProperty().bind(Bindings.format("Teacher:"));
            } else {
                controller.topDetailsProperty().bind(Bindings.format("Class:"));
            }

            controller.topInfoProperty().bind(new StringBinding() {
                {
                    super.bind(lessonsComboBox.selectionModelProperty());
                }

                @Override
                protected String computeValue() {
                    if (displayTeachers) {
                        return lessonsComboBox.getSelectionModel().getSelectedItem().getTeacher().getName();
                    } else {
                        return lessonsComboBox.getSelectionModel().getSelectedItem().getaClass().getName();
                    }
                }
            });

            controller.botDetailsProperty().bind(Bindings.format("Course:"));

            controller.botInfoProperty().bind(new StringBinding() {
                {
                    super.bind(lessonsComboBox.selectionModelProperty());
                }

                @Override
                protected String computeValue() {
                    return String.format("%s (%s)",
                            lessonsComboBox.getSelectionModel().getSelectedItem().getCourse().getName(),
                            lessonsComboBox.getSelectionModel().getSelectedItem().getCourse().getId());
                }
            });


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
