package api.consoleApp.consoleMenu;

import api.consoleApp.consoleMenu.menu.Menu;
import api.consoleApp.consoleMenu.menu.MenuItem;

public class ApplicationMenus {

    public enum MenuOptions {
        LOAD_FILE_TO_SYSTEM("Load a new XML file"),
        SHOW_SETTINGS("Display the settings and algorithm properties"),
        RUN_ALGORITHM("Start the algorithm"),
        SHOW_BEST_RESULT("Display the best result"),
        SHOW_ALGORITHM_HISTORY("Display the progress of the algorithm"),
        SAVE_TO_FILE("Save to file"),
        LOAD_FROM_FILE("Load from file"),
        THREAD_OPTIONAL("Enable/Disable multi-threading"),
        EXIT("Exit");

        private final String title;
        private Runnable activation;

        public String getTitle() {
            return this.title;
        }

        public void updateActivation(Runnable activation) { this.activation = activation; }
        public Runnable getActivation() { return this.activation;}

        MenuOptions(String title) {
            this.title = title;
        }
    }

    public static Menu createFunctionalMenuFromEnum() {
        Menu theMenu = new Menu("Main Menu");

        for (MenuOptions option : MenuOptions.values()) {
            MenuItem currentMenuItem = new MenuItem(option.getTitle());
            currentMenuItem.setMethod(option.getActivation());

            theMenu.addMenuItem(currentMenuItem);
        }

        return theMenu;
    }

}
