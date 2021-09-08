package components.problemInfo.ruleAccordionItem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;

public class RuleAccordionItemController {

    @FXML
    private TitledPane titledPane;

    @FXML
    private Label nameLbl;

    @FXML
    private Label typeLbl;

    @FXML
    private void initialize() {
        titledPane.textProperty().bind(nameLbl.textProperty());
    }

    public void setName(String name) {
        this.nameLbl.setText(name);
    }

    public void setType(String type) {
        this.typeLbl.setText(type);
    }
}
