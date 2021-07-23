package api.consoleApp;

import api.consoleApp.menu.MainMenu;
import api.consoleApp.menu.Menu;
import api.consoleApp.menu.MenuItem;
import logic.*;
import logic.actions.Action;
import logic.schema.XMLExtractException;
import logic.timeTable.rules.base.Rule;
import logic.timeTable.rules.base.Rules;

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
        engine.setUpdateGenerationInterval(25);
    }

    private void displayAlgorithmResultsOnFinished() {
        System.out.println("Algorithm finished!");
        System.out.printf("Best fitness found: %.4f%n", engine.getBestResult().getFitness());
    }

    private void displayAlgorithmProgressOnUpdate() {
        float progressPercentage = ((float) engine.getCurrentGeneration()) / engine.getMaxGenerations() * 100f;
        System.out.printf("Progress (%.2f%%): %d / %d generations%n",
                progressPercentage, engine.getCurrentGeneration(), engine.getMaxGenerations());
        System.out.printf("Current best fitness: %.4f%n", engine.getBestResult().getFitness());
        System.out.println("----------");

    }

    public void run() {
        mainMenu.show();
    }

    private void initializeMenu() {
        mainMenu = new MainMenu();

        MenuOptions.LOAD_FILE_TO_SYSTEM.updateActivation(this::openXMLFile);
        MenuOptions.SHOW_SETTINGS.updateActivation(this::showSettingsNProperties);
        MenuOptions.RUN_ALGORITHM.updateActivation(this::runAlgorithm);
        MenuOptions.SHOW_BEST_RESULT.updateActivation(this::showBestResult);
        MenuOptions.SHOW_ALGORITHM_HISTORY.updateActivation(this::showAlgorithmHistory);
        MenuOptions.EXIT.updateActivation(mainMenu::exitMainMenu);

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

        engine.updateEvoEngine();

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
        System.out.println("Evolution algorithm properties:");
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

        int generations = askNumberOfGenerationsFromUser();
        int updateEvery = askGenerationIntervalFromUser();

        // Start the algorithm
        System.out.printf("Starting the algorithm%n%n");
        engine.setUpdateGenerationInterval(updateEvery);
        engine.startAlgorithm(generations);
    }

    private void showBestResult() {
        if (!isFileLoadedWrapper()) {
            return;
        } else if (engine.getState() == Engine.State.IDLE) {
            System.out.println("There are no results. Please run the algorithm first."); // TODO: Copy code #1
            return;
        }

        System.out.println("Analyzing the best result - the best time table!");
        System.out.println("Please insert a way to represent the data (RAW, TEACHER, CLASS):");
        System.out.println("RAW - shows all the lessons written in the time table.");
        System.out.println("TEACHER - shows for each teacher his time tables ");
        System.out.println("CLASS - shows for each class his time table");

        String input = scanner.nextLine();
        switch (input.toUpperCase()) {
            case "RAW":
                System.out.println(EngineOutput.bestResultAsRAW(engine.getBestResult()));
                break;
            case "TEACHER":
                System.out.println(EngineOutput.bestResultTEACHER(engine.getBestResult()));
                break;
            case "CLASS":
                System.out.println(EngineOutput.bestResultCLASS(engine.getBestResult()));
                break;
            default:
                System.out.printf("'%s' not found as an option. Shows default information:%n", input);
                break;
        }

        System.out.printf("Best solution fitness: %.4f%n", engine.getBestResult().getFitness());

        for (Rule rule : engine.getBestResult().getRules().getListOfRules()) {
            System.out.printf("Rule '%s' (%s) Score: %.4f%n",
                    rule.getId(), rule.getType(), rule.calcFitness(engine.getBestResult()));
        }

        System.out.printf("HARD rules avg fitness: %.1f%n", engine.getBestResult().getAvgFitness(Rules.RULE_TYPE.HARD));
        System.out.printf("SOFT rules avg fitness: %.1f%n", engine.getBestResult().getAvgFitness(Rules.RULE_TYPE.SOFT));
    }

    private void showAlgorithmHistory() {
        if (!isFileLoadedWrapper()) {
            return;
        } else if (engine.getState() == Engine.State.IDLE) {
            System.out.println("There are no results. Please run the algorithm first.");
            return;
        }

        Map<Integer, Float> generations2Fitness = engine.getHistoryGeneration2Fitness();
        System.out.printf("Recorded %d stages of generations.%n", generations2Fitness.size());

        Float prevFitness = null;
        for (Map.Entry<Integer, Float> currentTTGeneration : generations2Fitness.entrySet()) {
            if (prevFitness == null) {
                System.out.printf("First generation fitness: %.4f%n", currentTTGeneration.getValue());
            } else {
                System.out.printf("Generation %d fitness: %.4f (%+.4f)%n",
                        currentTTGeneration.getKey(), currentTTGeneration.getValue(), (currentTTGeneration.getValue() - prevFitness));
            }

            prevFitness = currentTTGeneration.getValue();
        }
    }

    private boolean isFileLoadedWrapper() {
        if (!engine.isFileLoaded()) {
            System.out.println("Please open first an XML file with the correct format of the application.");
            return false;
        }

        return true;
    }

    private int askGenerationIntervalFromUser() {
        int generationInterval = -1;

        do {
            System.out.println("Enter the generations interval to keep updated:");
            String input = scanner.nextLine();
            try {
                generationInterval = Integer.parseInt(input);
                if (generationInterval < 1) {
                    System.out.println("The number has to be above 1.");
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Please insert a natural number.");
            }
        } while (generationInterval < 1);

        return generationInterval;
    }

    private int askNumberOfGenerationsFromUser() {
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
