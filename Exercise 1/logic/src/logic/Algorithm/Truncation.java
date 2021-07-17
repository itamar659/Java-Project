package logic.Algorithm;

public class Truncation implements Selection {

    @Override
    public Population execute(Population population, Object... args) {
        if (args.length != 1 || args[0].getClass() != Integer.class) {
            return null;
        }

        int topPercent = (int) args[0];
        int picks = topPercent * population.getSize() / 100;
        Population survivors = new Population(picks, false);

        population.sort();
        for (int i = 0; i < picks; i++) {
            survivors.setSolutionByIndex(i, population.getSolutionByIndex(i));
        }

        return survivors;
    }
}
