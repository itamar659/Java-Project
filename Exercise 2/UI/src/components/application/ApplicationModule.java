package components.application;

import javafx.application.Platform;

import java.util.function.Consumer;

public class ApplicationModule {

    private Consumer<String> loadNewFile;

    public ApplicationModule(Consumer<String> loadNewFile) {
        this.loadNewFile = loadNewFile;
    }

    public void loadFile(String filePath) {
        Platform.runLater(
                () -> {
                    loadNewFile.accept(filePath);
                }
        );
    }
}
