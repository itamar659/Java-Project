package components.timeTable.timeTablePanel;

import components.timeTable.LessonsInfoResourcesConsts;
import components.timeTable.rawInfo.RawInfoController;
import components.timeTable.timeTableComponent.TimeTableController;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import logic.timeTable.Class;
import logic.timeTable.HasName;
import logic.timeTable.Lesson;
import logic.timeTable.Teacher;
import logic.timeTable.TimeTable;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class TimeTablePanelController {

    private final String[] targetTypes = {"Teachers", "Classes"};

    private TimeTable timeTableSolution;
    private TimeTableController timeTableController;

    @FXML
    private Label labelTargetType;

    @FXML
    private AnchorPane paneTimeTable;

    @FXML
    private ComboBox<String> comboBoxTargetType;

    @FXML
    private ComboBox<HasName> comboBoxTarget;

    @FXML
    void buttonRaw_Clicked(ActionEvent event) {
        // Display the lessons in an alert.
        paneTimeTable.getChildren().clear();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/components/timeTable/rawInfo/RawInfo.fxml"));
        try {
            Parent parent = loader.load();
            RawInfoController c = loader.getController();
            c.setTimeTableSolution(this.timeTableSolution);
            paneTimeTable.getChildren().add(parent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonTable_Clicked(ActionEvent event) {
        createTimeTableController();
        comboBoxTargetType_ItemChanged(null);
    }

    @FXML
    void comboBoxTargetType_ItemChanged(ActionEvent event) {
        if(timeTableSolution == null){
            return;
        }
        comboBoxTarget.getItems().clear();

        if (comboBoxTargetType.getSelectionModel().getSelectedItem().equals(targetTypes[0])) {
            this.timeTableController.setDisplayTeachers(true);
            comboBoxTarget.getItems().addAll(timeTableSolution.getProblem().getTeachers());
        } else {
            this.timeTableController.setDisplayTeachers(false);
            comboBoxTarget.getItems().addAll(timeTableSolution.getProblem().getClasses());
        }

        comboBoxTarget.getSelectionModel().selectFirst();
        comboBoxTarget_ItemChanged(null);
    }

    @FXML
    void comboBoxTarget_ItemChanged(ActionEvent event) {
        createTimeTableGrid();
    }

    @FXML
    private void initialize() {
        // init the target combo box
        comboBoxTarget.setCellFactory(lv -> new TimeTablePanelController.TargetTypeCell());
        comboBoxTarget.setButtonCell(new TimeTablePanelController.TargetTypeCell());

        // init the combo box types
        comboBoxTargetType.getItems().addAll(targetTypes);
        comboBoxTargetType.getSelectionModel().selectFirst();

        // Bind the target label to the target type combo box
        labelTargetType.textProperty().bind(new StringBinding() {
            {
                bind(comboBoxTargetType.getSelectionModel().selectedItemProperty());
            }

            @Override
            protected String computeValue() {
                if (comboBoxTargetType.getSelectionModel().selectedItemProperty().get().equals(targetTypes[0])) {
                    return "Teacher:";
                }
                return "Class:";
            }
        });

        createTimeTableController();
    }

    private void createTimeTableController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LessonsInfoResourcesConsts.GRID_TABLE_FXML_RESOURCE);
            Parent grid = loader.load();

            paneTimeTable.getChildren().clear();
            paneTimeTable.getChildren().add(grid);

            this.timeTableController = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTimeTableSolution(TimeTable timeTableSolution) {
        this.timeTableSolution = timeTableSolution;

        comboBoxTargetType_ItemChanged(null);
    }

    private void createTimeTableGrid() {
        if (comboBoxTarget.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        String findName = comboBoxTarget.getSelectionModel().getSelectedItem().getName();
        final List<Lesson>[] lessons = new List[1];
        if (timeTableController.isDisplayTeachers()) {
            this.timeTableSolution.getTeachersTimeTable().forEach((t, l) -> {
                if (t.getName().equals(findName)) {
                    lessons[0] = l;
                }
            });
        } else {
            this.timeTableSolution.getClassesTimeTable().forEach((t, l) -> {
                if (t.getName().equals(findName)) {
                    lessons[0] = l;
                }
            });
        }

        this.timeTableController.createTimeTableGrid(
                FXCollections.observableArrayList(lessons[0]),
                timeTableSolution.getProblem().getDays(),
                timeTableSolution.getProblem().getHours()
        );
    }

    private static class TargetTypeCell extends ListCell<HasName> {
        @Override
        protected void updateItem(HasName item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText("");
            } else {
                setText(String.format("%s", item.getName()));
            }
        }
    }
}
