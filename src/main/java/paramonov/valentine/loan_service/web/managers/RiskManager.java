package paramonov.valentine.loan_service.web.managers;

import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;

public interface RiskManager {
    void analyzeRisks(LoanApplicationVo applicationDetails);

    void checkNumberOfApplications(LoanApplicationVo applicationDetails);
}
