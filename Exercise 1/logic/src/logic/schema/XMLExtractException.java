package logic.schema;

public class XMLExtractException extends Exception {

    private final Exception mainCause;

    public Exception getMainCause() {
        return mainCause;
    }

    public XMLExtractException(String message, Exception e) {
        super(message);

        this.mainCause = e;
    }

    public XMLExtractException(String message) {
        super(message);

        this.mainCause = null;
    }
}
