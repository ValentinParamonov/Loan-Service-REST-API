package paramonov.valentine.loan_service.util;

public class IllegalTimeFormatException extends IllegalArgumentException {
    public IllegalTimeFormatException() {
    }

    public IllegalTimeFormatException(String message) {
        super(message);
    }
}
