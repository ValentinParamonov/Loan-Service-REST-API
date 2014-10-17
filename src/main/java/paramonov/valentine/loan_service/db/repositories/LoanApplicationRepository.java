package paramonov.valentine.loan_service.db.repositories;

import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.entities.User;

public interface LoanApplicationRepository {
    LoanApplication getUserApplication(User user, long applicationId);
}
