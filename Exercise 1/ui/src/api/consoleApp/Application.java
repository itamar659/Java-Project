package api.consoleApp;

import api.consoleApp.menu.MainMenu;
import api.consoleApp.menu.Menu;
import api.consoleApp.menu.MenuItem;
import logic.*;
import logic.actions.Action;
import logic.schema.XMLExtractException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private MainMenu mainMenu;
    private final Scanner scanner;
    private final Engine engine;

    public Application() {
        initializeMenu();
        scanner = new Scanner(System.in);
        engine = new Engine();
        engine.generationEndListener(this::displayAlgorithmProgressOnUpdate);
        engine.finishRunListener(this::displayAlgorithmResultsOnFinished);
        engine.setUpdateEveryGeneration(25);
    }

    private void displayAlgorithmResultsOnFinished() {
        System.out.println("Algorithm finished!");
    }

    private void displayAlgorithmProgressOnUpdate() {
        float progressPercentage = ((float) engine.getCurrentGeneration()) / engine.getMaxGenerations() * 100f;
        System.out.printf("Progress (%.2f%%): %d / %d generations%n",
                progressPercentage, engine.getCurrentGeneration(), engine.getMaxGenerations());
        System.out.printf("Current best fitness: %f%n", engine.getBestResult().getFitness());
        System.out.println("----------");
    }

    public void run() {
        mainMenu.show();
    }

    private void initializeMenu() {
        MenuOptions.LOAD_FILE_TO_SYSTEM.updateActivation(this::openXMLFile);
        MenuOptions.SHOW_SETTINGS.updateActivation(this::showSettingsNProperties);
        MenuOptions.RUN_ALGORITHM.updateActivation(this::runAlgorithm);
        MenuOptions.SHOW_BEST_RESULT.updateActivation(this::showBestResult);
        MenuOptions.SHOW_ALGORITHM_HISTORY.updateActivation(this::showAlgorithmHistory);
        MenuOptions.EXIT.updateActivation(mainMenu::exitMainMenu);

        mainMenu = new MainMenu();
        mainMenu.setCurrentMenu(createFunctionalMenuFromEnum());
    }

    private Menu createFunctionalMenuFromEnum() {
        Menu theMenu = new Menu("Main Menu");

        for (MenuOptions option : MenuOptions.values()) {
            MenuItem currentMenuItem = new MenuItem(option.getTitle());
            currentMenuItem.setMethod(option.getActivation());

            theMenu.addMenuItem(currentMenuItem);
        }

        return theMenu;
    }

    private void openXMLFile() {
        if (engine.getState() == Engine.State.RUNNING) {
            System.out.println("The algorithm now running. Please wait for it to complete.");
            // TODO: Add stop method for the algorithm. than stop the algorithm if he wants.
            return;
        }

        // Get the file XML from the user
        System.out.println("Please enter the full path of the XML file contains the properties and settings for the application: ");
        String filePath = scanner.nextLine();

        if (!isFileXMLExists(filePath)) {
            return;
        }

        // Try parse the xml file to an evo engine
        try {
            File xmlFile = new File(filePath);
            engine.validateXMLFile(xmlFile);
        } catch (JAXBException e) {
            System.out.println("Schema error. Please try again after the next update.");
            return;
        } catch (XMLExtractException e) {
            System.out.println(e.getMessage());
            return;
        }

        // If already have results, ask before replace
        if (engine.getState() == Engine.State.COMPLETED) {
            if (!confirmUserWantToEraseTheResults()) {
                return;
            }
        }

        engine.loadEvoEngine();

        System.out.println("The file loaded correctly (hopefully)! New settings and properties have been created.");
    }

    private void showSettingsNProperties() {
        if (!isFileLoadedWrapper()) {
            return;
        }

        if (engine.getEvoEngineSettings() == null) {
            // Shouldn't come to here. In case it would, something messed up with my programming skills
            System.out.println("Error! The engine for evolution algorithm not setup correctly. Forgot to initialize the engine settings.");
            return;
        }

        // Display courses information
        System.out.println(EngineOutput.getCoursesDetails(engine.getEvoEngineSettings()));
        // Display teachers information
        System.out.println(EngineOutput.getTeachersDetails(engine.getEvoEngineSettings()));
        // Display classes information
        System.out.println(EngineOutput.getClassesDetails(engine.getEvoEngineSettings()));
        // Display rules
        System.out.println(EngineOutput.getRulesDetails(engine.getEvoEngineSettings()));
        // Algorithm settings
        System.out.println(EngineOutput.getEvoAlgorithmDetails(engine.getEvoEngineSettings()));
    }

    private void runAlgorithm() {
        if (!isFileLoadedWrapper()) {
            return;

        } else if (engine.getState() == Engine.State.RUNNING) {
            System.out.println("The algorithm already running.");
            displayAlgorithmProgressOnUpdate();
            return;

        } else if (engine.getState() == Engine.State.COMPLETED) {
            if (!confirmUserWantToEraseTheResults()) {
                return;
            }
        }

        int generations = getNumberOfGenerationsInput();
        int updateEvery = getUpdateEvery();

        // Start the algorithm
        System.out.printf("Starting the algorithm%n%n");
        engine.setUpdateEveryGeneration(updateEvery);
        engine.startAlgorithm(generations);
    }

    private void showBestResult() {
        if (!isFileLoadedWrapper()) {
            return;
        } else if (engine.getState() == Engine.State.IDLE) {
            System.out.println("There are no results. Please run the algorithm first."); // TODO: Copy code #1
            return;
        }

        //System.out.println("Please enter the following parameters:");
        //System.out.printf("A day between 0 to %d ( D ): %n", engine.getAlgorithmSettings().getDays());


        // NOT CLOSE TO BE DONE 19.07

        // TOMORROW WORK ON IT  20.07 :3

        // FINISHED XML FIRST, GUESS IT'S TODAY THEN 22.07

        System.out.println("The best result:");
        System.out.println(engine.getBestResult());
        // TODO: Show best results.
    }

    private void showAlgorithmHistory() {
        if (!isFileLoadedWrapper()) {
            return;
        } else if (engine.getState() == Engine.State.IDLE) {
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




    private int getUpdateEvery() {
        int updateEvery = -1;

        do {
            System.out.println("Enter every how many generations do you want to be updated:");
            String input = scanner.nextLine();
            try {
                updateEvery = Integer.parseInt(input);
                if (updateEvery < 1) {
                    System.out.println("The number has to be above 1.");
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Please insert a natural number.");
            }
        } while (updateEvery < 1);

        return updateEvery;
    }

    private int getNumberOfGenerationsInput() {
        int generations = -1;

        do {
            System.out.println("Please insert the maximum number of generations:");
            String input = scanner.nextLine();
            try {
                generations = Integer.parseInt(input);
                if (generations < 100) {
                    System.out.println("The number of generations has to be 100 or greater.");
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Please insert a natural number.");
            }
        } while (generations < 100);

        return generations;
    }

    private boolean isFileXMLExists(String filePath) {
        if (!filePath.endsWith(".xml")) {
            System.out.println("The fine is not an xml file.");
            return false;
        }

        if (!Files.exists(Paths.get(filePath))) {
            System.out.println("File not exists in the current path.");
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
