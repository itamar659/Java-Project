package components.timeTable.lessonInfo;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LessonInfoController {

    private final StringProperty topDetails = new SimpleStringProperty();
    private final StringProperty botDetails = new SimpleStringProperty();
    private final StringProperty topInfo = new SimpleStringProperty();
    private final StringProperty botInfo = new SimpleStringProperty();

    public StringProperty topDetailsProperty() {
        return topDetails;
    }

    public StringProperty botDetailsProperty() {
        return botDetails;
    }

    public StringProperty topInfoProperty() {
        return topInfo;
    }

    public StringProperty botInfoProperty() {
        return botInfo;
    }

    @FXML
    private void initialize() {
        topDetailsLbl.textProperty().bind(topDetails);
        botDetailsLbl.textProperty().bind(botDetails);
        topInfoLbl.textProperty().bind(topInfo);
        botInfoLbl.textProperty().bind(botInfo);
    }

    @FXML
    private Label topDetailsLbl;

    @FXML
    private Label botDetailsLbl;

    @FXML
    private Label topInfoLbl;

    @FXML
    private Label botInfoLbl;
}
