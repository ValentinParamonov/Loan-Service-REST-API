package paramonov.valentine.loan_service.web.managers.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.common.LoanEventStatus;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.db.repositories.GenericRepository;
import paramonov.valentine.loan_service.db.repositories.LoanApplicationRepository;
import paramonov.valentine.loan_service.db.repositories.LoanEventRepository;
import paramonov.valentine.loan_service.properties.LoanManagerProperties;
import paramonov.valentine.loan_service.util.ValidationUtils;
import paramonov.valentine.loan_service.web.managers.LoanManager;
import paramonov.valentine.loan_service.web.managers.RiskManager;
import paramonov.valentine.loan_service.web.managers.exceptions.ApplicationDeniedException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidAmountException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidTermException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Component("loanManager")
final class LoanManagerImpl implements LoanManager {
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private LoanEventRepository loanEventRepository;

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private LoanManagerProperties loanManagerProperties;

    @Autowired
    private RiskManager riskManager;

    private Logger log;

    @PostConstruct
    private void init() {
        log = LogManager.getLogger(getClass());
    }

    @Override
    public void applyForLoan(LoanApplicationVo application) {
        validateLoanApplication(application);
        try {
            riskManager.analyzeRisks(application);
        } catch(ApplicationDeniedException ade) {
            loanEventRepository.newEvent(application, null, LoanEventStatus.DENIED);
            throw ade;
        }

        final LoanApplication loanApplication = newLoanApplication(application);
        genericRepository.save(loanApplication);

        loanEventRepository.newEvent(application, loanApplication, LoanEventStatus.APPLICATION);
    }

    private LoanApplication newLoanApplication(LoanApplicationVo application) {
        final BigDecimal amount = application.getAmount();
        final User applicant = application.getApplicant();
        final Integer term = application.getTerm();
        final Date dueDate = getDateAfterDays(term);
        final BigDecimal interest = loanManagerProperties.getDefaultInterest();

        return new LoanApplication()
            .setLoanAmount(amount)
            .setLoanInterest(interest)
            .setUser(applicant)
            .setDueDate(dueDate);
    }

    private Date getDateAfterDays(Integer term) {
        final Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, term);

        return calendar.getTime();
    }

    private void validateLoanApplication(LoanApplicationVo application) {
        final BigDecimal applicationAmount = application.getAmount();
        final Integer term = application.getTerm();

        validateAmount(applicationAmount);
        validateTerm(term);
    }

    private void validateAmount(BigDecimal applicationAmount) {
        final BigDecimal minAmount = loanManagerProperties.getMinAmount();
        final BigDecimal maxAmount = loanManagerProperties.getMaxAmount();

        if(!ValidationUtils.isBetweenIncluding(applicationAmount, minAmount, maxAmount)) {
            throw new InvalidAmountException();
        }
    }

    private void validateTerm(Integer term) {
        final Integer minTerm = loanManagerProperties.getMinTermDays();
        final Integer maxTerm = loanManagerProperties.getMaxTermDays();

        if(!ValidationUtils.isBetweenIncluding(term, minTerm, maxTerm)) {
            throw new InvalidTermException();
        }
    }
}
