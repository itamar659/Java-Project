package logic.evoAlgorithm.selections;

public enum SupportedSelections {
    RouletteWheel("Roulette Wheel"), Truncation("Truncation"), Tournament("Tournament");

    private final String name;
    public String getName() {
        return name;
    }

    SupportedSelections(String name) {
        this.name = name;
    }
}
