package paramonov.valentine.loan_service.web.managers.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.repositories.LoanEventRepository;
import paramonov.valentine.loan_service.properties.LoanManagerProperties;
import paramonov.valentine.loan_service.util.Time;
import paramonov.valentine.loan_service.web.managers.RiskManager;
import paramonov.valentine.loan_service.web.managers.exceptions.ApplicationDeniedException;

import java.math.BigDecimal;

@Component("riskManager")
class RiskManagerImpl implements RiskManager {
    @Autowired
    private LoanManagerProperties loanManagerProperties;

    @Autowired
    private LoanEventRepository loanEventRepository;

    @Override
    public void analyzeRisks(LoanApplicationVo applicationDetails) {
        if(isMaxAmount(applicationDetails)) {
            checkRiskyTime();
        }

        checkNumberOfApplications(applicationDetails);
    }

    @Override
    public void checkNumberOfApplications(LoanApplicationVo applicationDetails) {
        final String applicantIp = applicationDetails.getApplicantIp();
        final int numberOfApplications = loanEventRepository.getNumberOfApplicationsInLast24Hours(applicantIp);
        final int maxApplicationsPerDay = loanManagerProperties.getMaxApplicationsPerDay();

        if(numberOfApplications > maxApplicationsPerDay) {
            throw new ApplicationDeniedException();
        }
    }

    private boolean isMaxAmount(LoanApplicationVo application) {
        final BigDecimal applicationAmount = application.getAmount();
        final BigDecimal maxAmount = loanManagerProperties.getMaxAmount();

        return maxAmount.equals(applicationAmount);
    }

    private void checkRiskyTime() {
        final Time riskyTimeFrom = loanManagerProperties.getRiskyTimeFrom();
        final Time riskyTimeTill = loanManagerProperties.getRiskyTimeTill();
        final Time currentTime = new Time();

        if(currentTime.between(riskyTimeFrom, riskyTimeTill)) {
            throw new ApplicationDeniedException();
        }
    }
}
