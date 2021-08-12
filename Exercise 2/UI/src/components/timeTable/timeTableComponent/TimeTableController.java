package components.timeTable.timeTableComponent;

import components.timeTable.LessonsInfoResourcesConsts;
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
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import logic.timeTable.Lesson;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class TimeTableController {

    private static final int OFFSET = 1;

    @FXML
    private GridPane timeTableGrid;

    private IntegerProperty days = new SimpleIntegerProperty(0);
    private IntegerProperty hours = new SimpleIntegerProperty(0);
    private ListProperty<Lesson> lessons = new SimpleListProperty<>();

    public void createTimeTableGrid(ObservableList<Lesson> lessons, int days, int hours) {
        this.lessons.set(lessons);
        this.days.set(days);
        this.hours.set(hours);

        

        addLessons();
    }

    private void addLessons() {
        Map<Map.Entry<Integer, Integer>, LessonsInfoController> dayHour2Controller = new HashMap<>();
        for (Lesson lesson : lessons) {
            Map.Entry<Integer, Integer> time = new AbstractMap.SimpleEntry<>(lesson.getDay(), lesson.getHour());
            if (!dayHour2Controller.containsKey(time)) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(LessonsInfoResourcesConsts.MULTI_LESSONS_TT_FXML_RESOURCE);
                    Node lessInfo = loader.load();

                    timeTableGrid.add(lessInfo, lesson.getDay() + OFFSET, lesson.getHour() + OFFSET);

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
        timeTableGrid.setHgap(5);
        timeTableGrid.setVgap(5);
        timeTableGrid.setAlignment(Pos.CENTER);
        timeTableGrid.setPadding(new Insets(20, 20, 20, 20));
    }
}
