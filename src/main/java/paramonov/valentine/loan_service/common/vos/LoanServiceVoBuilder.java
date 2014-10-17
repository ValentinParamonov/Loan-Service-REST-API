package paramonov.valentine.loan_service.common.vos;

import paramonov.valentine.loan_service.db.entities.User;

import java.math.BigDecimal;

public interface LoanServiceVoBuilder {
    LoanApplicationVo newLoanApplicationVo(User applicant, BigDecimal amount, Integer term, String applicantIp);
}
