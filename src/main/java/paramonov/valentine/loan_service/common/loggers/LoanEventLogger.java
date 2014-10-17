package paramonov.valentine.loan_service.common.loggers;

import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;

public interface LoanEventLogger {
    void logNewApplication(LoanApplicationVo applicationDetails, LoanApplication application);

    void logApplicationExtension(LoanApplicationVo applicationDetails, LoanApplication application);

    void logApplicationDenied(LoanApplicationVo applicationDetails);
}
