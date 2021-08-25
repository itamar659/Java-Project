package components.timeTable.timeTablePanel;

import components.application.UIAdapter;
import components.timeTable.LessonsInfoResourcesConsts;
import components.timeTable.rawInfo.RawInfoController;
import components.timeTable.secondCenterScreen.components.CrossoverController;
import components.timeTable.timeTableComponent.TimeTableController;
import engine.base.Crossover;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import engine.base.HasName;
import logic.evoAlgorithm.configurable.Configurable;
import logic.timeTable.Lesson;
import logic.timeTable.TimeTable;

import java.io.IOException;
import java.util.List;

public class TimeTablePanelController {

    private final String[] targetTypes = {"Teachers", "Classes"};

    private final ObjectProperty<TimeTable> timeTableSolutionProperty = new SimpleObjectProperty<>();

    private final ObjectProperty<Crossover<TimeTable>> crossoverProperty = new SimpleObjectProperty<>();

    public ObjectProperty<Crossover<TimeTable>> crossoverProperty() {
        return crossoverProperty;
    }

    private Parent timeTableParent;
    private Parent rawInfoParent;
    private Parent configParent;

    private TimeTableController timeTableController;
    private RawInfoController rawInfoController;
    private CrossoverController crossoverController;

    private UIAdapter uiAdapter;

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
        paneTimeTable.getChildren().clear();
        paneTimeTable.getChildren().add(rawInfoParent);
    }

    @FXML
    void buttonTable_Clicked(ActionEvent event) {
        paneTimeTable.getChildren().clear();
        paneTimeTable.getChildren().add(timeTableParent);
    }

    @FXML
    void buttonConfig_Clicked(ActionEvent event){
        paneTimeTable.getChildren().clear();
        paneTimeTable.getChildren().add(configParent);
    }

    @FXML
    void comboBoxTargetType_ItemChanged(ActionEvent event) {
        if(timeTableSolutionProperty.get() == null){
            return;
        }

        createComboBoxes();
        comboBoxTarget.getSelectionModel().selectFirst();
        createTimeTableGrid();
    }

    @FXML
    void comboBoxTarget_ItemChanged(ActionEvent event) {
        if(timeTableSolutionProperty.get() == null){
            return;
        }

        createTimeTableGrid();
    }

    @FXML
    private void initialize() {
        // init the target combo box
        comboBoxTarget.setCellFactory(lv -> new TimeTablePanelController.TargetTypeCell());
        comboBoxTarget.setButtonCell(new TimeTablePanelController.TargetTypeCell());

        // init the combo box types
        comboBoxTargetType.getItems().addAll(targetTypes);

        // Bind the target label to the target type combo box
        labelTargetType.textProperty().bind(new StringBinding() {
            {
                bind(comboBoxTargetType.getSelectionModel().selectedItemProperty());
            }

            @Override
            protected String computeValue() {
                if (targetTypes[0].equals(comboBoxTargetType.getSelectionModel().selectedItemProperty().get())) {
                    return "Teacher:";
                }
                return "Class:";
            }
        });

        timeTableSolutionProperty.addListener((observable, oldValue, newValue) -> {
            if (paneTimeTable.getChildren().get(0) == timeTableParent) {
                createTimeTableGrid();
            }
        });

        createRawInfoController();
        createTimeTableController();
        createCrossoverController();
        paneTimeTable.getChildren().add(rawInfoParent);
    }

    private void createCrossoverController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/components/timeTable/secondCenterScreen/components/CrossoverController.fxml"));
            configParent = loader.load();
            crossoverController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ObjectProperty<TimeTable> timeTableSolutionProperty() { return timeTableSolutionProperty; }

    private void createTimeTableGrid() {
        if (comboBoxTarget.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        String findName = comboBoxTarget.getSelectionModel().getSelectedItem().getName();
        final List<Lesson>[] lessons = new List[1];
        if (timeTableController.isDisplayTeachers()) {
            this.timeTableSolutionProperty.get().getTeachersTimeTable().forEach((t, l) -> {
                if (t.getName().equals(findName)) {
                    lessons[0] = l;
                }
            });
        } else {
            this.timeTableSolutionProperty.get().getClassesTimeTable().forEach((t, l) -> {
                if (t.getName().equals(findName)) {
                    lessons[0] = l;
                }
            });
        }

        if (lessons[0] != null) {
            this.timeTableController.createTimeTableGrid(
                    FXCollections.observableArrayList(lessons[0]),
                    timeTableSolutionProperty.get().getProblem().getDays(),
                    timeTableSolutionProperty.get().getProblem().getHours()
            );
        }
    }

    private void createComboBoxes() {
        if (comboBoxTargetType.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        comboBoxTarget.getItems().clear();
        if (comboBoxTargetType.getSelectionModel().getSelectedItem().equals(targetTypes[0])) {
            this.timeTableController.setDisplayTeachers(true);
            comboBoxTarget.getItems().addAll(timeTableSolutionProperty.get().getProblem().getTeachers());
        } else {
            this.timeTableController.setDisplayTeachers(false);
            comboBoxTarget.getItems().addAll(timeTableSolutionProperty.get().getProblem().getClasses());
        }
    }

    private void createRawInfoController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/components/timeTable/rawInfo/RawInfo.fxml")); // TODO: Add to consts resources
            rawInfoParent = loader.load();
            rawInfoController = loader.getController();
            rawInfoController.timeTableSolutionProperty().bind(this.timeTableSolutionProperty);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTimeTableController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(LessonsInfoResourcesConsts.GRID_TABLE_FXML_RESOURCE);
            timeTableParent = loader.load();
            timeTableController = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void setUiAdapter(UIAdapter uiAdapter) {
        this.uiAdapter = uiAdapter;
        crossoverController.setUiAdapter(uiAdapter);
    }

}
