package components.timeTable.timeTablePanel;

import components.timeTable.LessonsInfoResourcesConsts;
import components.timeTable.timeTableComponent.TimeTableController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import logic.timeTable.Lesson;

import java.io.IOException;

public class TimeTablePanelController {

    @FXML
    private ScrollPane timeTableScrollPane;

    private IntegerProperty days = new SimpleIntegerProperty(0);
    private IntegerProperty hours = new SimpleIntegerProperty(0);
    private ListProperty<Lesson> lessons = new SimpleListProperty<>();

    public void createTimeTableGrid(ObservableList<Lesson> lessons, int days, int hours) {
        this.lessons.set(lessons);
        this.days.set(days);
        this.hours.set(hours);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LessonsInfoResourcesConsts.GRID_TABLE_FXML_RESOURCE);
            Parent grid = loader.load();

            timeTableScrollPane.setContent(grid);

            TimeTableController controller = loader.getController();
            controller.createTimeTableGrid(lessons, days, hours);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
