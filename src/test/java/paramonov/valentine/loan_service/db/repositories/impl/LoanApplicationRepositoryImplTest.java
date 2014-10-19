package paramonov.valentine.loan_service.db.repositories.impl;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import paramonov.valentine.loan_service.TestDatabaseConfig;
import paramonov.valentine.loan_service.db.entities.LoanApplication;
import paramonov.valentine.loan_service.db.entities.User;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class, RepositoryTestConfig.class})
@TransactionConfiguration(defaultRollback = false)
public class LoanApplicationRepositoryImplTest {
    private static boolean populated;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private LoanApplicationRepositoryImpl loanApplicationRepositoryImpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static User user;
    private static User userWithNoApplications;

    @Before
    @Transactional
    public void setUp() {
        if(populated) {
            return;
        }
        populated = true;

        populateDatabase();
    }

    @Test
    @Transactional(readOnly = true)
    public void testGetUserApplication_WhenUserExistsAndIdExists_ShouldNotBeNull() {
        final Long applicationId =
            jdbcTemplate.queryForObject("select id from application where amount=666", Long.class);

        final LoanApplication userApplication = loanApplicationRepositoryImpl.getUserApplication(user, applicationId);

        assertThat(userApplication, not((LoanApplication) null));
    }

    @Test
    @Transactional(readOnly = true)
    public void testGetUserApplication_WhenUserExistsButIdDoesNot_ShouldBeNull() {
        final LoanApplication userApplication = loanApplicationRepositoryImpl.getUserApplication(user, -1L);

        assertThat(userApplication, is((LoanApplication) null));
    }

    @Test
    @Transactional(readOnly = true)
    public void testGetUserApplication_WhenUserHasNoApplicationsButApplicationWithIdExists_ShouldBeNull() {
        final Long applicationId =
            jdbcTemplate.queryForObject("select id from application where amount=666", Long.class);

        final LoanApplication userApplication =
            loanApplicationRepositoryImpl.getUserApplication(userWithNoApplications, applicationId);

        assertThat(userApplication, is((LoanApplication) null));
    }

    private void populateDatabase() {
        user = saveUser("Starvin", "Marvin");
        userWithNoApplications = saveUser("No", "Applications");
        final User lance = saveUser("Lance", "Vance");
        final User phony = saveUser("Phony", "Johnny");

        saveApplication(user, 666);
        saveApplication(user, 13);
        saveApplication(lance, 12);
        saveApplication(phony, 222);
    }

    private LoanApplication saveApplication(User user, int amount) {
        final LoanApplication application = new LoanApplication()
            .setUser(user)
            .setDueDate(new Date())
            .setLoanInterest(BigDecimal.ZERO)
            .setLoanAmount(new BigDecimal(amount));

        sessionFactory.getCurrentSession().save(application);

        return application;
    }

    private User saveUser(String name, String password) {
        final User user = new User()
            .setName(name)
            .setPassword(password);

        sessionFactory.getCurrentSession().save(user);

        return user;
    }
}
