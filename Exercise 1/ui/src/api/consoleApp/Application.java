package api.consoleApp;

import api.consoleApp.menu.MainMenu;
import api.consoleApp.menu.Menu;
import api.consoleApp.menu.MenuItem;
import logic.*;
import logic.timeTable.TimeTable;

import java.util.*;

public class Application {

    public enum MenuOptions {
        LOAD_FILE_TO_SYSTEM("Load a new XML file"),
        SHOW_SETTINGS("Display the settings and algorithm properties"),
        RUN_ALGORITHM("Start the algorithm"),
        SHOW_BEST_RESULT("Display the best result"),
        SHOW_ALGORITHM_HISTORY("Display the progress of the algorithm"),
        EXIT("Exit");

        private final String title;

        public String getTitle() {
            return title;
        }

        MenuOptions(String title) {
            this.title = title;
        }
    }

    private MainMenu mainMenu;
    private final Scanner scanner;
    private final Engine engine;

    public Application() {
        initializeMenu();
        scanner = new Scanner(System.in);
        engine = new Engine();
    }

    public void run() {
        mainMenu.show();
    }

    private void initializeMenu() {
        mainMenu = new MainMenu();
        mainMenu.setCurrentMenu(createFunctionalMenuFromEnum());
    }

    private Menu createFunctionalMenuFromEnum() {
        Menu theMenu = new Menu("Main Menu");

        for (MenuOptions option : MenuOptions.values()) {
            MenuItem currentMenuItem = new MenuItem(option.getTitle());

            theMenu.addMenuItem(currentMenuItem);

            switch (option) {
                case LOAD_FILE_TO_SYSTEM:
                    currentMenuItem.setMethod(this::openXMLFile);
                    break;
                case SHOW_SETTINGS:
                    currentMenuItem.setMethod(this::showSettingsNProperties);
                    break;
                case RUN_ALGORITHM:
                    currentMenuItem.setMethod(this::runAlgorithm);
                    break;
                case SHOW_BEST_RESULT:
                    currentMenuItem.setMethod(this::showBestResult);
                    break;
                case SHOW_ALGORITHM_HISTORY:
                    currentMenuItem.setMethod(this::showAlgorithmHistory);
                    break;
                case EXIT:
                    currentMenuItem.setMethod(mainMenu::exitMainMenu);
                    break;
            }
        }

        return theMenu;
    }

    private void openXMLFile() {
        if (engine.isRunning()) {
            System.out.println("The algorithm now running. Please wait for it to complete.");
            // TODO: Add stop method for the algorithm. than stop the algorithm if he wants.
            return;
        }

        // Get the file XML from the user
        System.out.println("Please enter the full path of the XML file contains the properties and settings for the application: ");
        String filePath = scanner.nextLine();
        String result = engine.loadXMLFile(filePath);
        if (result != null) {
            System.out.println(result);
            return;
        }

        // If already have results, ask before replace
        if (engine.isCompletedRun()) {
            if (!confirmUserWantToEraseTheResults()) {
                return;
            }

            // TODO: Erase everything.
        }

        // Load the new settings to the algorithm and time table.
        System.out.println("The file loaded correctly (hopefully)! New settings and properties have been created.");
    }

    private void showSettingsNProperties() {
        if (!isFileLoadedWrapper()) {
            return;
        }

        if (engine.getAlgorithmSettings() == null) {
            // Shouldn't come to here. In case it would, something messed up with my programming skills
            System.out.println("Error! The engine for evolution algorithm not setup correctly. Forgot to initialize the engine settings.");
            return;
        }

        // Display courses information
        System.out.println(EngineOutput.getCoursesDetails(engine.getAlgorithmSettings()));

        // Display teachers information
        System.out.println(EngineOutput.getTeachersDetails(engine.getAlgorithmSettings()));

        // Display classes information
        System.out.println(EngineOutput.getClassesDetails(engine.getAlgorithmSettings()));

        // Display rules
        System.out.println(EngineOutput.getRulesDetails(engine.getAlgorithmSettings()));

        // Algorithm settings
        System.out.println(EngineOutput.getEvoAlgorithmDetails(engine.getAlgorithmSettings()));
    }

    private void runAlgorithm() {
        if (!isFileLoadedWrapper()) {
            return;

        } else if (engine.isRunning()) {
            System.out.println("The algorithm already running.");
            // TODO: Show progress
            return;

        } else if (engine.isCompletedRun()) {
            if (!confirmUserWantToEraseTheResults()) {
                return;
            }

            // TODO: Erase last result.
        }


        // Input number of generations OR back to main menu
        int generations = -1;

        do {
            System.out.println("Please insert the maximum number of generations:");
            String input = scanner.nextLine();
            try {
                generations = Integer.parseInt(input);
                if (generations < 0) {
                    System.out.println("The number of generations can not be a negative value.");
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Please insert a natural number (0 to back to the main menu).");
            }


        } while (generations < 0);

        if (generations == 0) {
            return;
        }

        // Start the algorithm
        System.out.println("Starting the algorithm");
        engine.startAlgorithm(generations);

        // TODO: Use "events" to be able to show the progress of the run.
    }

    private void showBestResult() {
        if (!isFileLoadedWrapper()) {
            return;
        } else if (!engine.isCompletedRun() && !engine.isRunning()) {
            System.out.println("There are no results. Please run the algorithm first."); // TODO: Copy code #1
            return;
        }

        System.out.println("Please enter the following parameters:");
        System.out.println("Day ( D ): "); // TODO: As integer, string, date, how is the day represent???

        // NOT CLOSE TO BE DONE

        System.out.println("The best result:");
        // TODO: Show best results.
    }

    private void showAlgorithmHistory() {
        if (!isFileLoadedWrapper()) {
            return;
        } else if (!engine.isCompletedRun() || !engine.isRunning()) {
            System.out.println("There are no results. Please run the algorithm first."); // TODO: Copy code #1
            return;
        }

        System.out.println("History:");
        // TODO: Display history.
    }

    private boolean isFileLoadedWrapper() {
        if (!engine.isFileLoaded()) {
            System.out.println("Please open first an XML file with the correct format of the application.");
            return false;
        }

        return true;
    }






    // Maybe move to utils? Can be static too.
    private boolean confirmUserWantToEraseTheResults() {
        System.out.println("Your previous results will be erased permanently.");
        System.out.println("Are you sure you want to erase everything? (type y/n)");
        boolean userAccept = getUserAnswer("y", "n");
        if (!userAccept) {
            System.out.println("Operation canceled successfully.");
            return false;
        }

        return true;
    }

    // Maybe move to utils? Can be static too.
    private boolean getUserAnswer(String accept, String decline) {
        accept = accept.toLowerCase();
        decline = decline.toLowerCase();
        String input;

        do {
            input = scanner.nextLine().toLowerCase();
            if (input.equals(accept)) {
                return true;
            }
            else if (input.equals(decline)) {
                return false;
            }

            System.out.println("Please type '" + accept + "' to accept, or '" + decline + "' to decline.");
        } while(true);
    }
}
