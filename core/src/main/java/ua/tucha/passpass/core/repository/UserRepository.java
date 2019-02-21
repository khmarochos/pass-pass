package ua.tucha.passpass.core.repository;

import org.springframework.data.repository.CrudRepository;
import ua.tucha.passpass.core.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
