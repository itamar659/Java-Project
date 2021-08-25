package logic.evoAlgorithm.crossovers;

public enum SupportedCrossovers {
    AspectOriented, DayTimeOriented;

    @Override
    public String toString() {
        return this.name();
    }
}
