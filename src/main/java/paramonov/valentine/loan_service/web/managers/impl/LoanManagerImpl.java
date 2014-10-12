package paramonov.valentine.loan_service.web.managers.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.db.repositories.LoanRepository;
import paramonov.valentine.loan_service.web.managers.LoanManager;

import javax.annotation.PostConstruct;

@Component("loanManager")
final class LoanManagerImpl implements LoanManager {
    @Value("${loan.manager.maxTermDays}")
    private String maxTermDays;

    @Value("${loan.manager.maxApplicationsPerDay}")
    private String maxApplicationsPerDay;

    @Value("loan.manager.maxAmount")
    private String maxAmount;

    @Autowired
    private LoanRepository loanRepository;

    private Logger log;

    @PostConstruct
    private void init() {
        log = LogManager.getLogger(getClass());
    }

    @Override
    public String sayHi() {
        loanRepository.getUser();
        return "OK";
    }
}
