package components.centerScreen.timeTable.timeTableComponent;

import components.Resources;
import components.centerScreen.timeTable.lessonsInfo.LessonsInfoController;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import logic.timeTable.Lesson;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class TimeTableController {

    @FXML
    private GridPane timeTableGrid;

    private final IntegerProperty days = new SimpleIntegerProperty(0);
    private final IntegerProperty hours = new SimpleIntegerProperty(0);
    private final ListProperty<Lesson> lessons = new SimpleListProperty<>();

    private final DoubleProperty maxBlockWidth = new SimpleDoubleProperty(LessonsInfoController.MAX_WIDTH);
    private final DoubleProperty maxBlockHeight = new SimpleDoubleProperty(LessonsInfoController.MAX_HEIGHT);

    private boolean displayTeachers = false;

    public void setDisplayTeachers(boolean displayTeachers) {
        this.displayTeachers = displayTeachers;
    }

    public boolean isDisplayTeachers() {
        return displayTeachers;
    }

    public void createTimeTableGrid(ObservableList<Lesson> lessons, int days, int hours) {
        this.lessons.set(lessons);
        this.days.set(days);
        this.hours.set(hours);

        buildGrid();
        setBorderHoursDays();
        addLessonsToTimeTableGrid();
    }

    private void buildGrid() {
        Node saveGroupForTable = timeTableGrid.getChildren().get(0);
        timeTableGrid.getChildren().clear();
        timeTableGrid.getChildren().add(saveGroupForTable);

        timeTableGrid.getColumnConstraints().clear();
        timeTableGrid.getRowConstraints().clear();

        for (int i = 0; i < days.get() + 1; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.prefWidthProperty().bind(maxBlockWidth);
            col.maxWidthProperty().bind(maxBlockWidth);
            col.minWidthProperty().bind(maxBlockWidth);
            timeTableGrid.getColumnConstraints().add(col);
        }

        for (int i = 0; i < hours.get() + 1; i++) {
            RowConstraints row = new RowConstraints();
            row.prefHeightProperty().bind(maxBlockHeight);
            row.maxHeightProperty().bind(maxBlockHeight);
            row.minHeightProperty().bind(maxBlockHeight);
            timeTableGrid.getRowConstraints().add(row);
        }
    }

    private void setBorderHoursDays() {
        for (int day = 0; day < days.get(); day++) {
            StackPane node = new StackPane();
            node.getChildren().add(new Label(String.format("%d", day + 1)));
            timeTableGrid.add(node, day + 1, 0);
        }

        for (int hour = 0; hour < hours.get(); hour++) {
            StackPane node = new StackPane();
            node.getChildren().add(new Label(String.format("%d", hour + 1)));
            timeTableGrid.add(node, 0, hour + 1);
        }
    }

    private void addLessonsToTimeTableGrid() {
        Map<Map.Entry<Integer, Integer>, LessonsInfoController> dayHour2Controller = new HashMap<>();
        lessons.forEach(lesson -> {
            Map.Entry<Integer, Integer> time = new AbstractMap.SimpleEntry<>(lesson.getDay(), lesson.getHour());
            if (!dayHour2Controller.containsKey(time)) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(Resources.MULTI_LESSONS_TT_FXML_RESOURCE);
                    Node lessonInfo = loader.load();

                    StackPane node = new StackPane();
                    node.setPrefSize(maxBlockWidth.get(), maxBlockHeight.get());
                    node.setMaxSize(maxBlockWidth.get(), maxBlockHeight.get());
                    node.setMinSize(maxBlockWidth.get(), maxBlockHeight.get());
                    node.getChildren().add(lessonInfo);
                    timeTableGrid.add(node, lesson.getDay() + 1, lesson.getHour() + 1);

                    LessonsInfoController controller = loader.getController();
                    controller.setDisplayTeachers(displayTeachers);

                    dayHour2Controller.put(time, controller);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            LessonsInfoController controller = dayHour2Controller.get(time);
            controller.addLesson(lesson);
        });
    }
}
