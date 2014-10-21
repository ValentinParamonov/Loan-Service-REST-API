package paramonov.valentine.loan_service.web.managers.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.repositories.LoanEventRepository;
import paramonov.valentine.loan_service.properties.LoanServiceProperties;
import paramonov.valentine.loan_service.util.Time;
import paramonov.valentine.loan_service.web.managers.exceptions.ApplicationRejectedException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ManagerTestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RiskManagerImplTest {
    @Autowired
    private RiskManagerImpl riskManagerImpl;

    @Autowired
    private LoanServiceProperties loanServiceProperties;

    @Autowired
    private LoanEventRepository loanEventRepository;

    @Autowired
    private LoanApplicationVo loanApplicationVo;

    @Test(expected = ApplicationRejectedException.class)
    public void testCheckRiskyTime_WhenTimeIsRisky_ShouldThrowException() {
        doReturn(Time.valueOf("0:00:00")).when(loanServiceProperties).riskyTimeFrom();
        doReturn(Time.valueOf("23:59:59")).when(loanServiceProperties).riskyTimeTill();

        riskManagerImpl.checkRiskyTime();
    }

    @Test(expected = ApplicationRejectedException.class)
    public void testCheckNumberOfApplications_WhenNumberIsGreaterThanAllowed_ShouldThrowException() {
        doReturn(5).when(loanEventRepository).getNumberOfApplicationsInLast24Hours(any(String.class));
        doReturn(3).when(loanServiceProperties).maxApplicationsPerDay();

        riskManagerImpl.checkNumberOfApplications(loanApplicationVo);
    }
}
