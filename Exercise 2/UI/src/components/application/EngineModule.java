package components.application;

import engine.base.Crossover;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import logic.Engine;
import logic.timeTable.TimeTable;

public class EngineModule {
    private final Engine theEngine;

    private final ObjectProperty<Crossover<TimeTable>> crossoverProperty = new SimpleObjectProperty<>();

    public EngineModule() {
        this.theEngine = new Engine();
    }

    public Crossover<TimeTable> getCrossoverProperty() {
        return crossoverProperty.get();
    }

    public Engine getTheEngine() {
        return theEngine;
    }

    public ObjectProperty<Crossover<TimeTable>> crossoverPropertyProperty() {
        return crossoverProperty;
    }
}
