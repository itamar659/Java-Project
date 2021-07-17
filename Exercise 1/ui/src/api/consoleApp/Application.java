package api.consoleApp;

import api.consoleApp.menu.MainMenu;
import api.consoleApp.menu.Menu;
import api.consoleApp.menu.MenuItem;
import logic.*;

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

    void TEMPPPPPPPP() {
        engine.Test();
    }

    private Menu createFunctionalMenuFromEnum() {
        Menu theMenu = new Menu("Main Menu");

        for (MenuOptions option : MenuOptions.values()) {
            MenuItem currentMenuItem = new MenuItem(option.getTitle());

            theMenu.addMenuItem(currentMenuItem);

            switch (option) {
                case LOAD_FILE_TO_SYSTEM:
//                    currentMenuItem.setMethod(this::openXMLFile);
                    currentMenuItem.setMethod(this::TEMPPPPPPPP);
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
            // TODO: Add stop method for the algorithm.
            return;
        }

        // Get the file XML from the user
        System.out.println("Please enter the full path of the XML file contains the properties and settings for the application: ");
        String filePath = scanner.nextLine();

        // Check if exists.
        // TODO: Check if the file exists and the path is valid. """INSIDE LOGIC"""
        System.out.println("The file exists.");

        // Check if the syntax is valid.
        // TODO: Check if the context of the file is good and create a new properties. """INSIDE LOGIC"""
        System.out.println("The file have a valid syntax.");

        if (engine.isCompletedRun()) {
            if (!confirmUserWantToEraseTheResults()) {
                return;
            }

            // TODO: Erase everything.
        }

        // Load the new settings to the algorithm and time table.
        // TODO: if the syntax is valid, clear everything done in the program and load the properties. """INSIDE LOGIC"""
        System.out.println("The file loaded correctly! New settings and properties have been created.");
    }

    private void showSettingsNProperties() {
        if (!isFileLoadedWrapper()) {
            return;
        }

        // Schedule properties
        Schedule schedule = engine.getSchedule();
        if (schedule == null) {
            System.out.println("Not found any schedule in the system."); // Shouldn't come to here
            return;
        }

        // Display courses information
        System.out.println(EngineOutput.getCoursesDetails(schedule));

        // TODO: Make sure every course the teacher teaches is valid course!
        // TODO: Need to do that while adding the courses from the XML.
        // TODO: Also defense if any case, wont crash the program - Already done!
        // Display teachers information
        System.out.println(EngineOutput.getTeachersDetails(schedule));

        // TODO: Make sure every course the teacher teaches is valid course!
        // TODO: Need to do that while adding the courses from the XML.
        // TODO: Also defense if any case, wont crash the program - Already done!
        // Display classes information
        System.out.println(EngineOutput.getClassesDetails(schedule));

        System.out.println();

        // Display rules // TODO:

        // Algorithm settings // TODO:
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

            // TODO: Erase everything.
        }

        System.out.println("Starting the algorithm");
        // TODO: Start the algorithm

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
            System.out.println("There are no properties to display.");
            System.out.println("You first need to open an XML file with the correct format of the properties and the settings for the application.");
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
