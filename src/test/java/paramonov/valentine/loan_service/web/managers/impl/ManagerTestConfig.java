package paramonov.valentine.loan_service.web.managers.impl;

import org.apache.logging.log4j.core.Logger;
import org.jasypt.encryption.StringEncryptor;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import paramonov.valentine.loan_service.common.loggers.LoanEventLogger;
import paramonov.valentine.loan_service.common.validators.LoanApplicationValidator;
import paramonov.valentine.loan_service.common.vos.LoanApplicationVo;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.repositories.GenericRepository;
import paramonov.valentine.loan_service.db.repositories.LoanApplicationRepository;
import paramonov.valentine.loan_service.db.repositories.LoanEventRepository;
import paramonov.valentine.loan_service.db.repositories.UserRepository;
import paramonov.valentine.loan_service.properties.LoanServiceProperties;
import paramonov.valentine.loan_service.web.managers.RiskManager;

import java.util.Date;

@Configuration
class ManagerTestConfig {
    @Bean LoanManagerImpl loanManagerImpl() {
        final Logger log = Mockito.mock(Logger.class);
        final LoanManagerImpl loanManagerImpl = Mockito.spy(new LoanManagerImpl());
        loanManagerImpl.log = log;
        Mockito.doNothing().when(loanManagerImpl).init();

        return loanManagerImpl;
    }

    @Bean RiskManagerImpl riskManagerImpl() {
        return Mockito.spy(new RiskManagerImpl());
    }

    @Bean UserManagerImpl userManagerImpl() {
        return Mockito.spy(new UserManagerImpl());
    }

    @Bean LoanApplicationRepository loanApplicationRepository() {
        return Mockito.mock(LoanApplicationRepository.class);
    }

    @Bean GenericRepository genericRepository() {
        return Mockito.mock(GenericRepository.class);
    }

    @Bean LoanEventRepository loanEventRepository() {
        return Mockito.mock(LoanEventRepository.class);
    }

    @Bean UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean LoanServiceProperties loanServiceProperties() {
        return Mockito.mock(LoanServiceProperties.class);
    }

    @Bean RiskManager riskManager() {
        return Mockito.mock(RiskManager.class);
    }

    @Bean LoanEventLogger eventLogger() {
        return Mockito.mock(LoanEventLogger.class);
    }

    @Bean LoanApplicationValidator loanApplicationValidator() {
        return Mockito.mock(LoanApplicationValidator.class);
    }

    @Bean LoanApplicationVo loanApplicationVo() {
        return Mockito.mock(LoanApplicationVo.class);
    }

    @Bean LoanApplication loanApplication() {
        final LoanApplication application = Mockito.mock(LoanApplication.class);

        Mockito.doReturn(application).when(application).setDueDate(Mockito.any(Date.class));

        return application;
    }

    @Bean StringEncryptor stringEncryptor() {
        return Mockito.mock(StringEncryptor.class);
    }
}
