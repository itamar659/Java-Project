package components.timeTable.rawInfo;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import logic.timeTable.TimeTable;

public class RawInfoController {

    @FXML
    private GridPane gridLessonsTable;

    private final ObjectProperty<TimeTable> timeTableSolution = new SimpleObjectProperty<>();

    public ObjectProperty<TimeTable> timeTableSolutionProperty() {
        return timeTableSolution;
    }

    @FXML
    private void initialize() {

        timeTableSolution.addListener((observable, oldValue, newValue) -> {
            // The first node has the lines highlights
            Node saveGroupForTable = gridLessonsTable.getChildren().get(0);
            gridLessonsTable.getChildren().clear();
            gridLessonsTable.getChildren().add(saveGroupForTable);

            // Add headers
            gridLessonsTable.addRow(0,
                    new Label("Day"), new Label("Hour"), new Label("Class"), new Label("Teacher"), new Label("Course"));

            // Add the body
            int[] rowIdx = {1};
            if (timeTableSolution.get() != null) {
                timeTableSolution.get().getLessons().forEach(lesson -> {
                    Label day = new Label(Integer.toString(lesson.getDay() + 1));
                    Label hour = new Label(Integer.toString(lesson.getHour() + 1));
                    Label aclass = new Label(String.format("%s (%s)", lesson.getaClass().getName(), lesson.getaClass().getId()));
                    Label teacher = new Label(String.format("%s (%s)", lesson.getTeacher().getName(), lesson.getTeacher().getId()));
                    Label course = new Label(String.format("%s (%s)", lesson.getCourse().getName(), lesson.getCourse().getId()));

                    gridLessonsTable.addRow(rowIdx[0], day, hour, aclass, teacher, course);
                    rowIdx[0]++;
                });
            }

            gridLessonsTable.getChildren().forEach(child -> {
                if (child instanceof Label) {
                    Label label = (Label) child;
//                    GridPane.setHalignment(label, HPos.CENTER);
                    label.setPadding(new Insets(10, 10, 10, 10));
                }
            });
        });
    }
}
