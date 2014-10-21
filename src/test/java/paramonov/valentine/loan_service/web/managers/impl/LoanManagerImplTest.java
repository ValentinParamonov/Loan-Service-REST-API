package paramonov.valentine.loan_service.web.managers.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import paramonov.valentine.loan_service.common.loggers.LoanEventLogger;
import paramonov.valentine.loan_service.common.validators.LoanApplicationValidator;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidAmountException;
import paramonov.valentine.loan_service.common.validators.exceptions.InvalidTermException;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.repositories.GenericRepository;
import paramonov.valentine.loan_service.db.repositories.LoanApplicationRepository;
import paramonov.valentine.loan_service.properties.LoanServiceProperties;
import paramonov.valentine.loan_service.web.managers.RiskManager;
import paramonov.valentine.loan_service.web.managers.exceptions.ApplicationRejectedException;
import paramonov.valentine.loan_service.web.managers.exceptions.InvalidApplicationIdException;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ManagerTestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LoanManagerImplTest {
    @Autowired
    private LoanManagerImpl loanManagerImpl;

    @Autowired
    private LoanApplicationValidator loanApplicationValidator;

    @Autowired
    private LoanApplicationVo loanApplicationVo;

    @Autowired
    private RiskManager riskManager;

    @Autowired
    private LoanEventLogger loanEventLogger;

    @Autowired
    private GenericRepository genericRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private LoanApplication loanApplication;

    @Autowired
    private LoanServiceProperties loanServiceProperties;

    @Test(expected = InvalidAmountException.class)
    public void testApplyForLoan_WhenValidatorThrowsInvalidAmountException_ShouldBePropagated() {
        doThrow(InvalidAmountException.class).when(loanApplicationValidator).validateLoanApplication(loanApplicationVo);

        loanManagerImpl.applyForLoan(loanApplicationVo);
    }

    @Test(expected = InvalidTermException.class)
    public void testApplyForLoan_WhenValidatorThrowsInvalidTermException_ShouldBePropagated() {
        doThrow(InvalidTermException.class).when(loanApplicationValidator).validateLoanApplication(loanApplicationVo);

        loanManagerImpl.applyForLoan(loanApplicationVo);
    }

    @Test(expected = ApplicationRejectedException.class)
    public void testApplyForLoan_WhenRiskManagerThrowsApplicationRejectedException_ShouldBePropagated() {
        doThrow(ApplicationRejectedException.class).when(riskManager).analyzeRisks(loanApplicationVo);

        loanManagerImpl.applyForLoan(loanApplicationVo);
    }

    @Test(expected = ApplicationRejectedException.class)
    public void testApplyForLoan_WhenRiskManagerThrowsApplicationRejectedException_LoanEventLoggerShouldBeCalled() {
        doThrow(ApplicationRejectedException.class).when(riskManager).analyzeRisks(loanApplicationVo);

        try {
            loanManagerImpl.applyForLoan(loanApplicationVo);
        } finally {
            verify(loanEventLogger, atMost(1)).logApplicationDenied(loanApplicationVo);
        }
    }

    @Test
    public void testApplyForLoan_WhenNoExceptions_ApplicationShouldBeSaved() {
        loanManagerImpl.applyForLoan(loanApplicationVo);

        verify(genericRepository, atMost(1)).save(any());
    }

    @Test
    public void testApplyForLoan_WhenNoExceptions_EventShouldBeLogged() {
        loanManagerImpl.applyForLoan(loanApplicationVo);

        verify(loanEventLogger, atMost(1)).logNewApplication(any(LoanApplicationVo.class), any(LoanApplication.class));
    }

    @Test(expected = InvalidApplicationIdException.class)
    public void testExtendLoan_WhenApplicationRepoReturnsNull_ShouldThrowException() {
        loanManagerImpl.extendLoan(loanApplicationVo, 0);
    }

    @Test(expected = ApplicationRejectedException.class)
    public void testExtendLoan_WhenRiskManagerThrowsApplicationRejectedException_ShouldBePropagated() {
        doThrow(ApplicationRejectedException.class).when(riskManager).checkNumberOfApplications(loanApplicationVo);
        doReturn(loanApplication).when(loanApplicationRepository).getUserApplication(null, 0);

        loanManagerImpl.extendLoan(loanApplicationVo, 0);
    }

    @Test(expected = ApplicationRejectedException.class)
    public void testExtendLoan_WhenRiskManagerThrowsApplicationRejectedException_EventShouldBeLogged() {
        doThrow(ApplicationRejectedException.class).when(riskManager).checkNumberOfApplications(loanApplicationVo);
        doReturn(loanApplication).when(loanApplicationRepository).getUserApplication(null, 0);

        try {
            loanManagerImpl.extendLoan(loanApplicationVo, 0);
        } finally {
            verify(loanEventLogger, atMost(1)).logApplicationDenied(loanApplicationVo);
        }
    }

    @Test
    public void testExtendLoan_WhenNoExceptions_EventShouldBeLogged() {
        final Date date = new Date();
        doReturn(loanApplication).when(loanApplicationRepository).getUserApplication(null, 0);
        doReturn(date).when(loanApplication).getDueDate();
        doReturn(BigDecimal.ZERO).when(loanApplication).getLoanInterest();
        doReturn(BigDecimal.ZERO).when(loanServiceProperties).extensionInterestFactor();

        loanManagerImpl.extendLoan(loanApplicationVo, 0);

        verify(loanEventLogger, atMost(1)).logNewApplication(any(LoanApplicationVo.class), any(LoanApplication.class));
    }

    @Test
    public void testModifyApplication_WhenComparingDateDifference_ShouldBeEqualToExtensionTerm() {
        final Date date = new Date();
        final int extensionTerm = 0;
        final long millisInADay = 1000 * 60 * 60;
        doReturn(date).when(loanApplication).getDueDate();
        doReturn(extensionTerm).when(loanServiceProperties).extensionTermDays();
        doReturn(loanApplication).when(loanApplicationRepository).getUserApplication(null, 0);
        doReturn(BigDecimal.ZERO).when(loanApplication).getLoanInterest();
        doReturn(BigDecimal.ZERO).when(loanServiceProperties).extensionInterestFactor();

        loanManagerImpl.modifyApplication(loanApplication);
        final Date newDate = loanApplication.getDueDate();
        final long timeDiff = newDate.getTime() - date.getTime();
        final long timeDiffDays = timeDiff / millisInADay;

        assertThat(timeDiffDays, equalTo((long) extensionTerm)) ;
    }

    @Test
    public void testModifyApplication_WhenComparingInterestDifference_ShouldBeEqualToInterestFactor() {
        final BigDecimal originalInterest = BigDecimal.ONE;
        final LoanApplication application = new LoanApplication()
            .setDueDate(new Date())
            .setLoanInterest(originalInterest);
        final BigDecimal interestFactor = BigDecimal.TEN;
        doReturn(application).when(loanApplicationRepository).getUserApplication(null, 0);
        doReturn(BigDecimal.ZERO).when(loanApplication).getLoanInterest();
        doReturn(interestFactor).when(loanServiceProperties).extensionInterestFactor();

        loanManagerImpl.modifyApplication(application);
        final BigDecimal newInterest = application.getLoanInterest();
        final BigDecimal interestDifference = newInterest.divide(originalInterest);

        assertThat(interestDifference, equalTo(interestFactor)) ;
    }
}
