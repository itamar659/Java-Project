package logic.validation;

public class ValidationResult {

    private final boolean isValid;
    private final String errorMessage;

    public boolean isValid() {
        return isValid;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ValidationResult(boolean isValid) {
        this.isValid = isValid;
        this.errorMessage = null;
    }

    public ValidationResult(boolean isValid, String errorMessage) {
        this.isValid = isValid;
        this.errorMessage = errorMessage;
    }
}
