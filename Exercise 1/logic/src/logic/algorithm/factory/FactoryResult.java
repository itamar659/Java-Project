package logic.algorithm.factory;

public class FactoryResult {

    private final Object returnedFactoryObject;
    private final boolean isErrorOccurred;
    private final String errorMessage;

    public boolean isErrorOccurred() {
        return isErrorOccurred;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Object getReturnedFactoryObject() {
        return returnedFactoryObject;
    }

    public FactoryResult(Object object) {
        this.returnedFactoryObject = object;
        this.isErrorOccurred = false;
        this.errorMessage = null;
    }

    public FactoryResult(boolean isErrorOccurred, String errorMessage) {
        this.returnedFactoryObject = null;
        this.isErrorOccurred = isErrorOccurred;
        this.errorMessage = errorMessage;
    }
}