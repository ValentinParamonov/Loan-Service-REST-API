package paramonov.valentine.loan_service.db.repositories.impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoanRepositoryImplTestConfig {
    @Bean
    LoanEventRepositoryImpl loanEventRepositoryImpl() {
        return new LoanEventRepositoryImpl();
    }
}
