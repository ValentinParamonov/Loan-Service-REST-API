package paramonov.valentine.loan_service.db.repositories.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import paramonov.valentine.loan_service.db.repositories.LoanApplicationRepository;

import javax.annotation.PostConstruct;

@Repository("loanApplicationRepository")
final class LoanApplicationRepositoryImpl implements LoanApplicationRepository {
    @Autowired
    private SessionFactory sessionFactory;

    private Logger log;

    @PostConstruct
    private void init() {
        log = LogManager.getLogger(getClass());
    }

    @Override
    public void newApplication() {

    }
}
