package paramonov.valentine.loan_service.db.repositories.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RepositoryTestConfig {
    @Bean LoanEventRepositoryImpl loanEventRepositoryImpl() {
        return new LoanEventRepositoryImpl();
    }

    @Bean LoanApplicationRepositoryImpl loanApplicationRepositoryImpl() {
        return new LoanApplicationRepositoryImpl();
    }

    @Bean UserRepositoryImpl userRepositoryImpl() {
        return new UserRepositoryImpl();
    }
}
