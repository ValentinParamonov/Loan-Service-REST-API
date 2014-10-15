package paramonov.valentine.loan_service.db.repositories.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import paramonov.valentine.loan_service.db.entities.User;
import paramonov.valentine.loan_service.db.repositories.UserRepository;

@Repository("userRepository")
final class UserRepositoryImpl implements UserRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User getUserByName(String name) {
        final SimpleExpression eqName = Restrictions.eq("name", name);
        return (User) sessionFactory
            .getCurrentSession()
            .createCriteria(User.class)
            .add(eqName)
            .uniqueResult();
    }

    @Override
    public void newUser(String userName, String password) {
        final User user = new User()
            .setName(userName)
            .setPassword(password);
        final Session session = sessionFactory.getCurrentSession();

        session.save(user);
    }
}
