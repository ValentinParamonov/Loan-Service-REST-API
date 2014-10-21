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
import paramonov.valentine.loan_service.db.entities.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDatabaseConfig.class, RepositoryTestConfig.class})
@TransactionConfiguration(defaultRollback = false)
public class UserRepositoryImplTest {
    private static boolean populated;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static User user;

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
    public void testGetUserByName_WhenUserExists_ShouldNotBeNull() {
        final String userName = user.getName();

        final User loadedUser = userRepositoryImpl.getUserByName(userName);

        assertThat(loadedUser, not((User) null));
    }

    @Test
    @Transactional
    public void testGetUserByName_WhenUserExists_ShouldBeNull() {
        final String userName = "";

        final User loadedUser = userRepositoryImpl.getUserByName(userName);

        assertThat(loadedUser, is((User) null));
    }

    @Test
    @Transactional
    public void testNewUser_WhenSavedUserIsLoadedByName_IdShouldNotBeNull() {
        final String userName = "Ulrich";
        final String password = "passwd";
        final String userIdQuery = String.format("select id from user where name ='%s'", userName);

        userRepositoryImpl.newUser(userName, password);
        final Long userId = jdbcTemplate.queryForObject(userIdQuery, Long.class);

        assertThat(userId, not((Long) null));
    }

    private void populateDatabase() {
        user = new User().setName("A").setPassword("B");
        sessionFactory.getCurrentSession().save(user);
    }
}
