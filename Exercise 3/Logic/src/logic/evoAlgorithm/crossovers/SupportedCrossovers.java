package logic.evoAlgorithm.crossovers;

public enum SupportedCrossovers {
    AspectOriented("Aspect Oriented"), DayTimeOriented("Day Time Oriented");

    private final String name;
    public String getName() {
        return name;
    }

    SupportedCrossovers(String name) {
        this.name = name;
    }
}
