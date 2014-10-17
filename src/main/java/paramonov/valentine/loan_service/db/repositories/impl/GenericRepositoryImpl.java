package paramonov.valentine.loan_service.db.repositories.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import paramonov.valentine.loan_service.db.repositories.GenericRepository;

@Repository("genericRepository")
class GenericRepositoryImpl implements GenericRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Object object) {
        final Session session = sessionFactory.getCurrentSession();

        session.save(object);
    }
}
