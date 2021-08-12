package logic.timeTable;

public interface HasId {

    String getId();

    default int compareByID(HasId other) {

        if (this.getId().length() < other.getId().length()) {
            return -1;
        }

        if (this.getId().length() > other.getId().length()) {
            return 1;
        }

        return this.getId().compareTo(other.getId());
    }
}
