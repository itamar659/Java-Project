package components.application;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import logic.Engine;
import logic.evoEngineSettingsWrapper;
import tasks.LoadFileTask;

import java.io.File;

public class UIAdapter {

    private Task<Boolean> currentRunningTask;

    private Engine theEngine;
    public evoEngineSettingsWrapper evoEngineSettings;

    public UIAdapter(Engine theEngine) {
        this.theEngine = theEngine;
        this.evoEngineSettings = theEngine.getEvoEngineSettings();
    }

    public void loadFile(String filePath, EventHandler<WorkerStateEvent> onSucceeded, EventHandler<WorkerStateEvent> onFailed) {
        currentRunningTask = new LoadFileTask(theEngine, new File(filePath));

        currentRunningTask.setOnFailed(onFailed);
        currentRunningTask.setOnSucceeded(onSucceeded);

        new Thread(currentRunningTask, "Load File Thread").start();
    }


}
