package paramonov.valentine.loan_service.web.managers;

import paramonov.valentine.loan_service.common.dtos.LoanHistoryDto;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.User;

import java.util.List;

public interface LoanManager {
    void applyForLoan(LoanApplicationVo applicationDetails);

    List<LoanHistoryDto> getLoanHistory(User user);

    void extendLoan(LoanApplicationVo applicationDetails, long applicationId);
}
