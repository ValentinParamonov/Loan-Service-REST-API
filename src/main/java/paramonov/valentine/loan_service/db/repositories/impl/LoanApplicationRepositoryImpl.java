package paramonov.valentine.loan_service.db.repositories.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.entities.User;
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
    public LoanApplication getUserApplication(User user, long applicationId) {
        final Session session = sessionFactory.getCurrentSession();
        final SimpleExpression eqUser = Restrictions.eq("user", user);
        final SimpleExpression eqId = Restrictions.eq("id", applicationId);

        return (LoanApplication) session
            .createCriteria(LoanApplication.class)
            .add(eqUser)
            .add(eqId)
            .uniqueResult();
    }
}
