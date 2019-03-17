package ua.tucha.passpass.core.repository;

import org.springframework.data.repository.CrudRepository;
import ua.tucha.passpass.core.model.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
    UserRole findByName(String name);
}
