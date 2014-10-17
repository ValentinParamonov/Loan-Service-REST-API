package paramonov.valentine.loan_service.web.managers.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.common.LoanEventStatus;
import paramonov.valentine.loan_service.common.dtos.LoanHistoryDto;
import paramonov.valentine.loan_service.common.loggers.LoanEventLogger;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.db.repositories.GenericRepository;
import paramonov.valentine.loan_service.db.repositories.LoanApplicationRepository;
import paramonov.valentine.loan_service.db.repositories.LoanEventRepository;
import paramonov.valentine.loan_service.properties.LoanManagerProperties;
import paramonov.valentine.loan_service.util.DateUtils;
import paramonov.valentine.loan_service.util.ValidationUtils;
import paramonov.valentine.loan_service.web.managers.LoanManager;
import paramonov.valentine.loan_service.web.managers.RiskManager;
import paramonov.valentine.loan_service.web.managers.exceptions.ApplicationDeniedException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidAmountException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidApplicationIdException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidTermException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component("loanManager")
final class LoanManagerImpl implements LoanManager {
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private LoanManagerProperties loanManagerProperties;

    @Autowired
    private RiskManager riskManager;

    @Autowired
    private LoanEventLogger eventLogger;

    private Logger log;

    @PostConstruct
    private void init() {
        log = LogManager.getLogger(getClass());
    }

    @Override
    public void applyForLoan(LoanApplicationVo applicationDetails) {
        validateLoanApplication(applicationDetails);
        try {
            riskManager.analyzeRisks(applicationDetails);
        } catch(ApplicationDeniedException ade) {
            eventLogger.logApplicationDenied(applicationDetails);
            throw ade;
        }

        final LoanApplication loanApplication = newLoanApplication(applicationDetails);
        genericRepository.save(loanApplication);

        eventLogger.logNewApplication(applicationDetails, loanApplication);
    }

    @Override
    public List<LoanHistoryDto> getLoanHistory(User user) {
        return new ArrayList<LoanHistoryDto>(Arrays.asList(new LoanHistoryDto()));
    }

    @Override
    public void extendLoan(LoanApplicationVo applicationDetails, long applicationId) {
        final User user = applicationDetails.getApplicant();
        final LoanApplication application = loanApplicationRepository.getUserApplication(user, applicationId);
        if(application == null) {
            log.error("User {} tried to get application with id '{}' which doesn't exist", user, applicationId);
            throw new InvalidApplicationIdException();
        }

        try {
            riskManager.checkNumberOfApplications(applicationDetails);
        } catch(ApplicationDeniedException ade) {
            eventLogger.logApplicationDenied(applicationDetails);
            throw ade;
        }

        extendLoan(application);
        eventLogger.logApplicationExtension(applicationDetails, application);
    }

    private void extendLoan(LoanApplication application) {
        final Integer extensionTerm = loanManagerProperties.getExtensionTermDays();
        final BigDecimal interestFactor = loanManagerProperties.getExtensionInterestFactor();
        final Date dueDate = application.getDueDate();
        final BigDecimal interest = application.getLoanInterest();
        final Date newDueDate = DateUtils.getDateAfterDays(dueDate, extensionTerm);
        final BigDecimal newInterest = interest.multiply(interestFactor);

        application
            .setDueDate(newDueDate)
            .setLoanInterest(newInterest);
    }

    private LoanApplication newLoanApplication(LoanApplicationVo application) {
        final BigDecimal amount = application.getAmount();
        final User applicant = application.getApplicant();
        final Integer term = application.getTerm();
        final Date dueDate = DateUtils.getDateAfterDays(term);
        final BigDecimal interest = loanManagerProperties.getDefaultInterest();

        return new LoanApplication()
            .setLoanAmount(amount)
            .setLoanInterest(interest)
            .setUser(applicant)
            .setDueDate(dueDate);
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
