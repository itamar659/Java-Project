package logic.timeTable;

public interface HasName {

    String getName();

    default int compareByID(HasName other) {
        return this.getName().compareTo(other.getName());
    }
}
