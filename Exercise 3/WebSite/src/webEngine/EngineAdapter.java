package webEngine;

import engine.base.Crossover;
import engine.base.Problem;
import logic.Engine;
import logic.evoAlgorithm.crossovers.AspectOriented;
import logic.timeTable.TimeTable;

// TODO: Create an adapter for the engine to hold the required properties.
//  Also Write an json serializer adapter for this class to be able to convert it to json.
public class EngineAdapter {

    transient private Engine engine;

    private Crossover<TimeTable> crossover;

    public Crossover<TimeTable> getCrossover() {
        return crossover;
    }

    public EngineAdapter() {
        engine = new Engine();

        crossover = new AspectOriented();
    }

    public void loadEngineByProblem(Problem<TimeTable> problem) {
        engine.loadTTEEngineByProblem(problem);

        // instantiate
        //crossover = engine.getEvoEngineSettings().getCrossover();
        // ...
    }
}
