package paramonov.valentine.loan_service.web.managers;

import paramonov.valentine.loan_service.common.dtos.LoanApplicationDto;

public interface LoanManager {
    void applyForLoan(LoanApplicationDto application);
}
