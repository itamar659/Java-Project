package components.application;

import Model.EngineModel;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import logic.Engine;
import logic.evoEngineSettingsWrapper;
import tasks.LoadFileTask;
import tasks.StartAlgorithm;

import java.io.File;

public class UIAdapter {

    private Task<Boolean> currentRunningTask;

    private EngineModel theEngine;
    private ApplicationController applicationController;

    public EngineModel getTheEngine() {
        return theEngine;
    }

    public UIAdapter(EngineModel theEngine, ApplicationController applicationController) {
        this.theEngine = theEngine;
        this.applicationController = applicationController;
    }

    public void loadFile(String filePath, EventHandler<WorkerStateEvent> onSucceeded) {
        currentRunningTask = new LoadFileTask(theEngine, new File(filePath));

        currentRunningTask.setOnSucceeded(onSucceeded);

        applicationController.alertMessageLoadNewFile(currentRunningTask);

        new Thread(currentRunningTask, "Load File Thread").start();
    }

    public void startAlgorithm() {
        currentRunningTask = new StartAlgorithm(theEngine);

        // TODO: Bind the UI Components

        new Thread(currentRunningTask, "Running Algorithm Thread").start();
    }

    public void pauseAlgorithm() {
        theEngine.stopAlgorithm(); // Create pause method
    }

    public void stopAlgorithm() {
        theEngine.stopAlgorithm();
    }
}
