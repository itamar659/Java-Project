package components.timeTable.timeTableComponent;

import components.timeTable.LessonsInfoResourcesConsts;
import components.timeTable.lessonInfo.LessonInfoController;
import components.timeTable.lessonsInfo.LessonsInfoController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import logic.timeTable.Lesson;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class TimeTableController {

    private static final int OFFSET = 1;
    private static final int GAP = 5;
    private static final int PADDING = 20;

    @FXML
    private GridPane timeTableGrid;

    private IntegerProperty days = new SimpleIntegerProperty(0);
    private IntegerProperty hours = new SimpleIntegerProperty(0);
    private ListProperty<Lesson> lessons = new SimpleListProperty<>();

    private boolean displayTeachers = false;

    public void setDisplayTeachers(boolean displayTeachers) {
        this.displayTeachers = displayTeachers;
    }

    public void createTimeTableGrid(ObservableList<Lesson> lessons, int days, int hours) {
        this.lessons.set(lessons);
        this.days.set(days);
        this.hours.set(hours);

        buildGrid();
        createEmptyTimeTableView();
        adjustGridSize();
        addLessonsToTimeTableGrid();
    }

    private void buildGrid() {
        Node saveGroupForTable = timeTableGrid.getChildren().get(0);
        timeTableGrid.getChildren().clear();
        timeTableGrid.getChildren().add(saveGroupForTable);

        timeTableGrid.getColumnConstraints().clear();

        for (int i = 0; i < days.get() + OFFSET; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(1.f / (days.get() + OFFSET) * 100);
            //col.setMinWidth(LessonInfoController.PREF_WIDTH);
            timeTableGrid.getColumnConstraints().add(col);
        }

        for (int i = 0; i < hours.get() + OFFSET; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(1.f / (hours.get() + OFFSET) * 100);
            //row.setMinHeight(LessonInfoController.PREF_HEIGHT);
            timeTableGrid.getRowConstraints().add(row);
        }
    }

    private void createEmptyTimeTableView() {
        // TODO: Create a component for a single cell in the table. And change the cell instead of the grid.
        //  Only after creating and filling all the cells, put them in the grid.

        for (int day = 0; day < days.get() + OFFSET; day++) {
            for (int hour = 0; hour < hours.get() + OFFSET; hour++) {
                GridPane node = new GridPane();
                node.setStyle("-fx-border-color:black");
                node.setAlignment(Pos.CENTER);
                timeTableGrid.add(node, day, hour);

                if (hour == 0 && day != 0) {
                    node.getChildren().add(createHeaderColOrRow(day));
                } else if (day == 0 && hour != 0) {
                    node.getChildren().add(createHeaderColOrRow(hour));
                }
            }
        }
    }

    private Label createHeaderColOrRow(int val) {
        Label label = new Label(String.format("%d", val));
        label.setAlignment(Pos.CENTER);

        return label;
    }

    private void adjustGridSize() {
        timeTableGrid.setMinWidth((days.get() + OFFSET ) * (LessonInfoController.PREF_WIDTH + GAP) + PADDING * 2);
        timeTableGrid.setMinHeight((hours.get() + OFFSET) * (LessonInfoController.PREF_HEIGHT + GAP) + PADDING * 2);
    }

    private void addLessonsToTimeTableGrid() {
        Map<Map.Entry<Integer, Integer>, LessonsInfoController> dayHour2Controller = new HashMap<>();
        for (Lesson lesson : lessons) {
            Map.Entry<Integer, Integer> time = new AbstractMap.SimpleEntry<>(lesson.getDay(), lesson.getHour());
            if (!dayHour2Controller.containsKey(time)) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(LessonsInfoResourcesConsts.MULTI_LESSONS_TT_FXML_RESOURCE);
                    Node lessInfo = loader.load();

                    timeTableGrid.getChildren().removeIf(node ->
                            GridPane.getRowIndex(node) == lesson.getHour() + 1 &&
                                    GridPane.getColumnIndex(node) ==lesson.getDay() + 1
                            );

                    GridPane node = new GridPane();
                    node.setStyle("-fx-border-color:black");
                    node.getChildren().add(lessInfo);
                    timeTableGrid.add(node, lesson.getDay() + 1, lesson.getHour() + 1);

                    LessonsInfoController controller = loader.getController();

                    dayHour2Controller.put(time, controller);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            LessonsInfoController controller = dayHour2Controller.get(time);
            controller.addLesson(lesson);
        }
    }

    @FXML
    private void initialize() {
        timeTableGrid.setHgap(GAP);
        timeTableGrid.setVgap(GAP);
        timeTableGrid.setPadding(new Insets(PADDING, PADDING, PADDING, PADDING));
    }
}
