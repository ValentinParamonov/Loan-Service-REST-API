package paramonov.valentine.loan_service.common.loggers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.common.LoanEventStatus;
import paramonov.valentine.loan_service.common.loggers.LoanEventLogger;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.repositories.LoanEventRepository;

@Component("loanEventLogger")
class LoanEventLoggerImpl implements LoanEventLogger {
    @Autowired
    private LoanEventRepository loanEventRepository;

    @Override
    public void logNewApplication(LoanApplicationVo applicationDetails, LoanApplication application) {
        loanEventRepository.newEvent(applicationDetails, application, LoanEventStatus.APPLICATION);
    }

    @Override
    public void logApplicationExtension(LoanApplicationVo applicationDetails, LoanApplication application) {
        loanEventRepository.newEvent(applicationDetails, application, LoanEventStatus.EXTENSION);
    }

    @Override
    public void logApplicationDenied(LoanApplicationVo applicationDetails) {
        loanEventRepository.newEvent(applicationDetails, null, LoanEventStatus.DENIED);
    }
}
