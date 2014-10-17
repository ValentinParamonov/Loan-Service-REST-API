package paramonov.valentine.loan_service.util;

public final class ValidationUtils {
    private ValidationUtils() {
    }

    public static <C extends Comparable> boolean isBetweenIncluding(C c, C from, C to) {
        if(c.compareTo(from) < 0) {
            return false;
        }

        if(c.compareTo(to) > 0) {
            return false;
        }

        return true;
    }
}
