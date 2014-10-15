package paramonov.valentine.loan_service.db.repositories;

import paramonov.valentine.loan_service.db.entities.User;

public interface UserRepository {
    User getUserByName(String name);

    void newUser(String userName, String password);
}
