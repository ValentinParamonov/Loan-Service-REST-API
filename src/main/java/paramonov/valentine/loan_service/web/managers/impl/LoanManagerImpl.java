package paramonov.valentine.loan_service.web.managers.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import paramonov.valentine.loan_service.common.LoanEventStatus;
import paramonov.valentine.loan_service.common.dtos.LoanEventDto;
import paramonov.valentine.loan_service.common.loggers.LoanEventLogger;
import paramonov.valentine.loan_service.common.validators.LoanApplicationValidator;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.entities.LoanEvent;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.db.repositories.GenericRepository;
import paramonov.valentine.loan_service.db.repositories.LoanApplicationRepository;
import paramonov.valentine.loan_service.db.repositories.LoanEventRepository;
import paramonov.valentine.loan_service.properties.LoanServiceProperties;
import paramonov.valentine.loan_service.util.DateUtils;
import paramonov.valentine.loan_service.web.managers.LoanManager;
import paramonov.valentine.loan_service.web.managers.RiskManager;
import paramonov.valentine.loan_service.web.managers.exceptions.ApplicationRejectedException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidApplicationIdException;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("loanManager")
class LoanManagerImpl implements LoanManager {
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private LoanEventRepository loanEventRepository;

    @Autowired
    private LoanServiceProperties loanServiceProperties;

    @Autowired
    private RiskManager riskManager;

    @Autowired
    private LoanEventLogger eventLogger;

    @Autowired
    private LoanApplicationValidator loanApplicationValidator;

    Logger log;

    @PostConstruct
    void init() {
        log = LogManager.getLogger(getClass());
    }

    @Override
    public void applyForLoan(LoanApplicationVo applicationDetails) {
        loanApplicationValidator.validateLoanApplication(applicationDetails);
        try {
            riskManager.analyzeRisks(applicationDetails);
        } catch(ApplicationRejectedException ade) {
            eventLogger.logApplicationDenied(applicationDetails);
            throw ade;
        }

        final LoanApplication loanApplication = newLoanApplication(applicationDetails);
        genericRepository.save(loanApplication);

        eventLogger.logNewApplication(applicationDetails, loanApplication);
    }

    @Override
    public List<LoanEventDto> getLoanHistory(User user) {
        List<LoanEvent> events = loanEventRepository.getHistoricEventsForUser(user);

        return getEventDtos(events);
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
        } catch(ApplicationRejectedException ade) {
            eventLogger.logApplicationDenied(applicationDetails);
            throw ade;
        }

        modifyApplication(application);
        eventLogger.logApplicationExtension(applicationDetails, application);
    }

    private List<LoanEventDto> getEventDtos(List<LoanEvent> events) {
        final int eventCount = events.size();
        final ArrayList<LoanEventDto> eventDtos = new ArrayList<>(eventCount);
        for(LoanEvent event : events) {
            final LoanEventDto eventDto = getEventDto(event);
            eventDtos.add(eventDto);
        }

        return eventDtos;
    }

    private LoanEventDto getEventDto(LoanEvent event) {
        final Date eventDate = event.getEventDate();
        final LoanEventStatus eventStatus = event.getEventStatus();
        final LoanApplication application = event.getApplication();
        final Long applicationId = application.getId();

        return new LoanEventDto()
            .withApplicationId(applicationId)
            .withEventStatus(eventStatus)
            .withEventDate(eventDate);
    }

    void modifyApplication(LoanApplication application) {
        final int extensionTerm = loanServiceProperties.extensionTermDays();
        final BigDecimal interestFactor = loanServiceProperties.extensionInterestFactor();
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
        final int term = application.getTerm();
        final Date dueDate = DateUtils.getDateAfterDays(term);
        final BigDecimal interest = loanServiceProperties.defaultInterest();

        return new LoanApplication()
            .setLoanAmount(amount)
            .setLoanInterest(interest)
            .setUser(applicant)
            .setDueDate(dueDate);
    }
}
