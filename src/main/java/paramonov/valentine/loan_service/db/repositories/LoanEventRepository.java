package paramonov.valentine.loan_service.db.repositories;

import paramonov.valentine.loan_service.common.LoanEventStatus;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.entities.LoanEvent;
import paramonov.valentine.loan_service.db.entities.User;

import java.util.List;

public interface LoanEventRepository {
    void newEvent(LoanApplicationVo applicationDetails, LoanApplication application, LoanEventStatus status);

    int getNumberOfApplicationsInLast24Hours(String ipAddress);

    List<LoanEvent> getHistoricEventsForUser(User user);
}
