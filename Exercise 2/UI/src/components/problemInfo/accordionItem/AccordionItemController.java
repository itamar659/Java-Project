package components.problemInfo.accordionItem;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;

public class AccordionItemController {

    @FXML
    private TitledPane titledPane;

    @FXML
    private Label nameLbl;

    @FXML
    private Label idLbl;

    @FXML
    private ListView<String> itemsListView;

    @FXML
    private Label listDescLbl;

    @FXML
    private void initialize() {
        titledPane.textProperty().bind(nameLbl.textProperty());
        this.itemsListView.setVisible(false);
        this.itemsListView.setManaged(false);
    }

    public void setName(String nameLbl) {
        this.nameLbl.setText(nameLbl);
    }

    public void setId(String idLbl) {
        this.idLbl.setText(idLbl);
    }

    public void setListDesc(String listDescLbl) {
        this.listDescLbl.setText(listDescLbl);
    }

    public void setItemsList(ObservableList<String> items) {
        this.itemsListView.setVisible(true);
        this.itemsListView.setManaged(true);
        this.itemsListView.setItems(items);
    }
}
