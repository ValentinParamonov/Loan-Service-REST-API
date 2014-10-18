package paramonov.valentine.loan_service.common.dtos;

public class LoanServiceErrorDto {
    private String error;

    public String getError() {
        return error;
    }

    public LoanServiceErrorDto setError(String error) {
        this.error = error;
        return this;
    }
}
