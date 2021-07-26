package api.consoleApp.consoleMenu;

import api.consoleApp.consoleMenu.menu.Menu;
import api.consoleApp.consoleMenu.menu.MenuItem;
import logic.actions.Action;

public class ApplicationMenus {

    public enum MenuOptions {
        LOAD_FILE_TO_SYSTEM("Load a new XML file"),
        SHOW_SETTINGS("Display the settings and algorithm properties"),
        RUN_ALGORITHM("Start the algorithm"),
        SHOW_BEST_RESULT("Display the best result"),
        SHOW_ALGORITHM_HISTORY("Display the progress of the algorithm"),
        EXIT("Exit");

        private final String title;
        private Action activation;

        public String getTitle() {
            return this.title;
        }

        public void updateActivation(Action activation) { this.activation = activation; }
        public Action getActivation() { return this.activation;}

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
