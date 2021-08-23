package components.problemInfo.configItem;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.TilePane;

import java.util.List;

public class ConfigController {
    @FXML private Label labelConfigName;
    @FXML private ListView<String> listViewConfig;
    @FXML private TitledPane tilePaneMe;

    @FXML
    private void initialize(){
    }

    public void setCongigName(String name) {
        this.labelConfigName.setText(name);
        this.tilePaneMe.setText(name);
    }

    public void setListView(ObservableList<String> lst) {
        this.listViewConfig.setManaged(true);
        this.listViewConfig.setItems(lst);
    }

}
