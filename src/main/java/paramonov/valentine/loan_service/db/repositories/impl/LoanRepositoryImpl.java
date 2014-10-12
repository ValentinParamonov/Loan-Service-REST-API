package paramonov.valentine.loan_service.db.repositories.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import paramonov.valentine.loan_service.common.LoanEventStatus;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.entities.LoanEvent;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.db.repositories.LoanRepository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;

@Repository("loanRepository")
final class LoanRepositoryImpl implements LoanRepository {
    @Autowired
    private SessionFactory sessionFactory;

    private Logger log;

    @PostConstruct
    private void init() {
        log = LogManager.getLogger(getClass());
    }

    @Override
    public User getUser() {
        final Session session = sessionFactory.getCurrentSession();
        final User user = new User();
        user.setName("John");
        session.save(user);

        final LoanApplication app = new LoanApplication()
            .setUser(user)
            .setDueDate(new Date())
            .setLoanAmount(BigDecimal.TEN)
            .setLoanInterest(BigDecimal.ONE);
        session.save(app);

        final LoanEvent loanEvent = new LoanEvent()
            .setUser(user)
            .setApplication(app)
            .setEventDate(new Date())
            .setEventStatus(LoanEventStatus.CLOSED);
        session.save(loanEvent);

        return (User) session.load(User.class, (long) 1);
    }
}
