package ua.tucha.passpass.repository;

import org.springframework.data.repository.CrudRepository;
import ua.tucha.passpass.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
