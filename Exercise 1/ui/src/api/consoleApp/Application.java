package api.consoleApp;

import api.consoleApp.consoleMenu.ApplicationMenus;
import api.consoleApp.consoleMenu.menu.MainMenu;
import logic.*;
import logic.schema.exceptions.XMLExtractException;
import logic.timeTable.TimeTable;
import logic.timeTable.rules.base.Rule;
import logic.timeTable.rules.base.Rules;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Application {

    private boolean isMultiThreaded;
    private final Scanner scanner;
    private MainMenu mainMenu;
    private Engine engine;

    private final Runnable updateMethod = this::displayAlgorithmProgressOnUpdate;

    public Application() {
        initializeMenu();

        isMultiThreaded = false;
        scanner = new Scanner(System.in);
        engine = new Engine();
        engine.setMultiThreaded(isMultiThreaded);
        engine.addFinishRunListener(this::displayAlgorithmResultsOnFinished);
        subscribeAndUnsubscribeFromListeners();
    }

    public void run() {
        mainMenu.show();
    }

    private void initializeMenu() {
        mainMenu = new MainMenu();

        ApplicationMenus.MenuOptions.LOAD_FILE_TO_SYSTEM.updateActivation(this::openXMLFile);
        ApplicationMenus.MenuOptions.SHOW_SETTINGS.updateActivation(this::showSettingsNProperties);
        ApplicationMenus.MenuOptions.RUN_ALGORITHM.updateActivation(this::runAlgorithm);
        ApplicationMenus.MenuOptions.SHOW_BEST_RESULT.updateActivation(this::showBestResult);
        ApplicationMenus.MenuOptions.SHOW_ALGORITHM_HISTORY.updateActivation(this::showAlgorithmHistory);
        ApplicationMenus.MenuOptions.SAVE_TO_FILE.updateActivation(this::saveToFile);
        ApplicationMenus.MenuOptions.LOAD_FROM_FILE.updateActivation(this::loadFromFile);
        ApplicationMenus.MenuOptions.THREAD_OPTIONAL.updateActivation(this::enableDisableMultiThreading);
        ApplicationMenus.MenuOptions.EXIT.updateActivation(this::exit);

        mainMenu.setCurrentMenu(ApplicationMenus.createFunctionalMenuFromEnum());
    }

    private synchronized void displayAlgorithmResultsOnFinished() {
        System.out.println("Algorithm finished!");
        System.out.printf("Best fitness found: %f%n", engine.getBestResult().getFitness());
        notifyAll();
    }

    private void displayAlgorithmProgressOnUpdate() {
        if (engine.getStopCondition() == Engine.StopCondition.MAX_GENERATIONS) {
            float progressPercentage = ((float) engine.getCurrentGeneration()) / engine.getMaxGenerationsCondition() * 100f;
            System.out.printf("Progress (%.2f%%): %d / %d generations%n",
                    progressPercentage, engine.getCurrentGeneration(), engine.getMaxGenerationsCondition());
            System.out.printf("Best current fitness: %f%n", engine.getBestResult().getFitness());

        } else if (engine.getStopCondition() == Engine.StopCondition.REQUESTED_FITNESS) {
            float progressPercentage = (engine.getBestResult().getFitness()) / engine.getMaxFitnessCondition() * 100f;
            if (progressPercentage > 100) progressPercentage = 100;
            System.out.printf("Generation: %d%n", engine.getCurrentGeneration());
            System.out.printf("Progress (%.2f%%): %f / %f best fitness%n",
                    progressPercentage, engine.getBestResult().getFitness(), engine.getMaxFitnessCondition());

        }
        System.out.println("------------------------------");
    }

    private void openXMLFile() {
        if (engine.getState() == Engine.State.RUNNING) {
            System.out.println("The algorithm now running.");
            System.out.println("Please wait for it to finish, or stop it before load a new file.");
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

    private boolean isFileXMLExists(String filePath) {
        if (!filePath.endsWith(".xml")) {
            System.out.println("The fine is not an xml file.");
            return false;
        } else if (!Files.exists(Paths.get(filePath))) {
            System.out.println("File not exists in the current path.");
            return false;
        }

        return true;
    }

    private void showSettingsNProperties() { // Multi-threaded supported
        if (!isFileLoadedCheck()) {
            return;
        }

        // Display courses information
        System.out.println(EvoInfoOutput.getCoursesDetails(engine.getEvoEngineSettings()));
        // Display teachers information
        System.out.println(EvoInfoOutput.getTeachersDetails(engine.getEvoEngineSettings()));
        // Display classes information
        System.out.println(EvoInfoOutput.getClassesDetails(engine.getEvoEngineSettings()));
        // Display rules
        System.out.println(EvoInfoOutput.getRulesDetails(engine.getEvoEngineSettings()));
        // Algorithm settings
        System.out.println("Evolution algorithm properties:");
        System.out.println(EvoInfoOutput.getEvoAlgorithmDetails(engine.getEvoEngineSettings()));
    }

    private void runAlgorithm() {
        if (!isFileLoadedCheck()) {
            return;
        } else if (engine.getState() == Engine.State.RUNNING) {
            System.out.println("The algorithm already running.");
            System.out.println("Do you want to stop the algorithm? (y/n)");
            if (!getUserYesNoAnswer()) {
                System.out.println("Back to main menu");
            } else {
                engine.stopAlgorithm();
            }
            return;
        } else if (engine.getState() == Engine.State.COMPLETED) {
            if (!confirmUserWantToEraseTheResults()) {
                return;
            }
        }

        // TODO: Create a new menu and display it
        System.out.println("Choose a stop condition for the algorithm:");
        System.out.println("1. Stop Condition: Maximum number of generations.");
        System.out.println("2. Stop Condition: Required fitness.");
        System.out.print("Enter an option: ");
        String input;
        do {
            input = scanner.nextLine();
            if (input.equals("1")) {
                stopCondMaxGenerations();
                break;
            } else if (input.equals("2")) {
                stopCondMaxFitness();
                break;
            }

            System.out.println("Please try again.");
        } while (true);
    }

    private void stopCondMaxGenerations() {
        int generations = askIntegerFromUser("Please insert the maximum number of generations:", 100);
        int updateEvery = askIntegerFromUser("Enter the generations interval to keep updated:", 1);

        // Start the algorithm
        System.out.printf("Starting the algorithm%n%n");
        engine.setUpdateGenerationInterval(updateEvery);
        engine.setMaxGenerationsCondition(generations);
        engine.setStopCondition(Engine.StopCondition.MAX_GENERATIONS);
        engine.startAlgorithm();
    }

    private void stopCondMaxFitness() {
        System.out.println("Please insert the fitness to stop at (a value between 0 to 1):");
        float maxFitness;
        String input;
        do {
            input = scanner.nextLine();
            try {
                maxFitness = Float.parseFloat(input);
                if (maxFitness < 0 || maxFitness > 1.000001f) {
                    System.out.println("Choose a value between 0 to 1.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("The input has to be a number.");
            }
        } while(true);

        int updateEvery = askIntegerFromUser("Enter the generations interval to keep updated:", 1);

        // Start the algorithm
        System.out.printf("Starting the algorithm%n%n");
        engine.setUpdateGenerationInterval(updateEvery);
        engine.setMaxFitnessCondition(maxFitness);
        engine.setStopCondition(Engine.StopCondition.REQUESTED_FITNESS);
        engine.startAlgorithm();
    }

    private void showBestResult() {
        if (!isFileLoadedCheck()) {
            return;
        } else if (engine.getState() == Engine.State.IDLE) {
            System.out.println("There are no results. Please run the algorithm first.");
            return;
        }

        System.out.println("Analyzing the best result - the best time table!");
        System.out.println("Please insert a way to represent the data (RAW, TEACHER, CLASS):");
        System.out.println("RAW - shows all the lessons written in the time table.");
        System.out.println("TEACHER - shows for each teacher his time tables ");
        System.out.println("CLASS - shows for each class his time table");

        String input = scanner.nextLine();
        displayRequiredInformation(input);

        System.out.printf("Best solution fitness: %f%n", engine.getBestResult().getFitness());

        for (Rule<TimeTable> rule : engine.getBestResult().getRules().getListOfRules()) {
            System.out.printf("Rule '%s' (%s) Score: %f%n",
                    rule.getId(), rule.getType(), rule.calcFitness(engine.getBestResult()));
        }

        System.out.printf("HARD rules avg fitness: %.1f%n", engine.getBestResult().getAvgFitness(Rules.RULE_TYPE.HARD));
        System.out.printf("SOFT rules avg fitness: %.1f%n", engine.getBestResult().getAvgFitness(Rules.RULE_TYPE.SOFT));

        if (engine.getState() == Engine.State.RUNNING) {
            displayAlgorithmProgressOnUpdate();
        }
    }

    private void displayRequiredInformation(String input) {
        switch (input.toUpperCase()) {
            case "RAW":
                System.out.println(TimeTableInfoOutput.bestResultAsRAW(engine.getBestResult()));
                break;
            case "TEACHER":
                System.out.println(TimeTableInfoOutput.bestResultTEACHER(engine.getBestResult()));
                break;
            case "CLASS":
                System.out.println(TimeTableInfoOutput.bestResultCLASS(engine.getBestResult()));
                break;
            default:
                System.out.printf("'%s' not found as an option. Shows default information:%n", input);
                break;
        }
    }

    private void showAlgorithmHistory() {
        if (!isFileLoadedCheck()) {
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
                System.out.printf("First generation fitness: %f%n", currentTTGeneration.getValue());
            } else {
                System.out.printf("Generation %d fitness: %f (%+.4f)%n",
                        currentTTGeneration.getKey(), currentTTGeneration.getValue(), (currentTTGeneration.getValue() - prevFitness));
            }

            prevFitness = currentTTGeneration.getValue();
        }

        if (engine.getState() == Engine.State.RUNNING) {
            displayAlgorithmProgressOnUpdate();
        }
    }

    private void saveToFile() {
        if (engine.getState() == Engine.State.RUNNING) {
            System.out.println("Please wait for the algorithm to finish first.");
            return;
        }
        System.out.println("Enter a file name or full path of the save file:");
        String path = scanner.nextLine();
        if (Files.exists(Paths.get(path))) {
            System.out.println("This file already exists. Are you sure you overwrite it? (y/n)");
            if (!getUserYesNoAnswer()) {
                System.out.println("Back to main menu");
                return;
            }
        }

        try {
            AppIO.SerializeToFile(path, this.engine);
        } catch (IOException e) {
            System.out.println("Cannot write into the file.");
            return;
        }

        System.out.println("File saved completed!");
    }

    private void loadFromFile() {
        if (engine.getState() == Engine.State.RUNNING) {
            System.out.println("Please wait for the algorithm to finish first.");
            return;
        }
        System.out.println("Enter a file name or full path to load the file:");
        String path = scanner.nextLine();
        if (!Files.exists(Paths.get(path))) {
            System.out.println("This doesn't exists.");
            return;
        }

        // If already have results, ask before replace
        if (engine.getState() == Engine.State.COMPLETED) {
            if (!confirmUserWantToEraseTheResults()) {
                return;
            }
        }

        try {
            engine = AppIO.DeserializeFromFile(path);
            engine.addFinishRunListener(this::displayAlgorithmResultsOnFinished);
            engine.afterDeserialized();
            this.isMultiThreaded = engine.isMultiThreaded();
            subscribeAndUnsubscribeFromListeners();
        } catch (IOException e) {
            System.out.println("The file corrupted or not built for this application.");
            return;
        } catch (ClassNotFoundException e) {
            System.out.println("The file is not match for this application.");
            return;
        }

        System.out.println("File loaded completed!");
    }

    private void enableDisableMultiThreading() {
        if (engine.getState() == Engine.State.RUNNING) {
            System.out.println("Please wait for the algorithm to finish first.");
            return;
        }

        isMultiThreaded = !isMultiThreaded;
        engine.setMultiThreaded(isMultiThreaded);
        subscribeAndUnsubscribeFromListeners();
        System.out.printf("Multi threading: %s%n", isMultiThreaded ? "ON" : "OFF");
    }

    private synchronized void exit() {
        engine.stopAlgorithm();
        while (engine.getState() == Engine.State.RUNNING) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.mainMenu.exitMainMenu();
    }

    private void subscribeAndUnsubscribeFromListeners() {
        if (!isMultiThreaded) {
            engine.addGenerationEndListener(updateMethod);
        } else {
            engine.removeGenerationEndListener(updateMethod);
        }
    }

    private boolean isFileLoadedCheck() {
        if (!engine.isFileLoaded()) {
            System.out.println("Please open first an XML file with the correct format of the application.");
            return false;
        }

        return true;
    }


    // HELPERS

    private int askIntegerFromUser(String message, int minValue) {
        int value = minValue - 1;

        do {
            System.out.println(message); // "Please insert the maximum number of generations:"
            String input = scanner.nextLine();
            try {
                value = Integer.parseInt(input);
                if (value < minValue) {
                    System.out.printf("The number has to be above or equal to %s.%n", minValue);
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Please insert a natural number.");
            }
        } while (value < minValue);

        return value;
    }

    private boolean confirmUserWantToEraseTheResults() {
        System.out.println("Your previous results will be erased permanently.");
        System.out.println("Are you sure you want to erase everything? (type y/n)");
        boolean userAccept = getUserYesNoAnswer();
        if (!userAccept) {
            System.out.println("Operation canceled successfully.");
            return false;
        }

        return true;
    }

    private boolean getUserYesNoAnswer() {
        String input;

        do {
            input = scanner.nextLine().toLowerCase();
            if (input.equals("y")) {
                return true;
            }
            else if (input.equals("n")) {
                return false;
            }

            System.out.println("Please type (y/n). 'y' to accept. 'n' to decline.");
        } while(true);
    }
}
