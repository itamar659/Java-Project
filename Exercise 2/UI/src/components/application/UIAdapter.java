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

    private EngineModule engineModule;
    public evoEngineSettingsWrapper evoEngineSettings;

    public UIAdapter(EngineModule engineModule) {
        this.engineModule = engineModule;
        this.evoEngineSettings = engineModule.getTheEngine().getEvoEngineSettings();
    }

    public void loadFile(String filePath, EventHandler<WorkerStateEvent> onSucceeded, EventHandler<WorkerStateEvent> onFailed) {
        currentRunningTask = new LoadFileTask(engineModule.getTheEngine(), new File(filePath));

        currentRunningTask.setOnFailed(onFailed);
        currentRunningTask.setOnSucceeded(onSucceeded);

        new Thread(currentRunningTask, "Load File Thread").start();
    }


}
