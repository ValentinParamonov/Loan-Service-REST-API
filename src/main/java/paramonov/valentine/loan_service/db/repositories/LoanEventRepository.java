package paramonov.valentine.loan_service.db.repositories;

import paramonov.valentine.loan_service.common.LoanEventStatus;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;

public interface LoanEventRepository {
    void newEvent(LoanApplicationVo applicationDetails, LoanApplication application, LoanEventStatus status);

    int getNumberOfApplicationsInLast24Hours(String ipAddress);
}
