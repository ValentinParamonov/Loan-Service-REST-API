package paramonov.valentine.loan_service.web.managers.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.common.dtos.LoanApplicationDto;
import paramonov.valentine.loan_service.db.repositories.LoanRepository;
import paramonov.valentine.loan_service.properties.LoanManagerProperties;
import paramonov.valentine.loan_service.util.Time;
import paramonov.valentine.loan_service.web.managers.LoanManager;

import javax.annotation.PostConstruct;

@Component("loanManager")
final class LoanManagerImpl implements LoanManager {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanManagerProperties loanManagerProperties;

    private Logger log;

    @PostConstruct
    private void init() {
        log = LogManager.getLogger(getClass());
    }

    @Override
    public void applyForLoan(LoanApplicationDto application) {
        final Time riskyTimeFrom = loanManagerProperties.getRiskyTimeFrom();
        final Time riskyTimeTill = loanManagerProperties.getRiskyTimeTill();
        final Time currentTime = new Time();

        log.info("from: {}", riskyTimeFrom.toString());
        log.info("till: {}", riskyTimeTill.toString());
        log.info("current: {}", currentTime.toString());

        log.info(currentTime.between(riskyTimeFrom, riskyTimeTill));
    }
}
