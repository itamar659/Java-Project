package components.mutations;

import engine.base.Mutation;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import logic.evoAlgorithm.factory.MutationFactory;

public class MutationsController {

    private final MutationFactory factory = new MutationFactory();

    private ListProperty<Mutation> mutations = new SimpleListProperty<>();

    @FXML
    private ComboBox<String> comboboxMutations;

    @FXML
    private ListView<String> listViewMutations;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonClear;

    @FXML
    private void initialize() {

        // Init the combobox
        //factory.getAvailableInstances();

        // Bind the buttons to the list
        buttonClear.disableProperty().bind(mutations.sizeProperty().greaterThan(0).not());
        listViewMutations.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() < 0) {
                buttonDelete.setDisable(false);
            } else {
                buttonDelete.setDisable(true);
            }
        });
    }

    @FXML
    void buttonAdd_Clicked(ActionEvent event) {

    }

    @FXML
    void buttonClear_Clicked(ActionEvent event) {

    }

    @FXML
    void buttonDelete_Clicked(ActionEvent event) {

    }

    @FXML
    void listViewMutations_SelectedItem(ActionEvent event) {

    }

}
