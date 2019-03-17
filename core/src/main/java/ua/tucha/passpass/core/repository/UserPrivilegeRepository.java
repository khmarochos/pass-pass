package ua.tucha.passpass.core.repository;

import org.springframework.data.repository.CrudRepository;
import ua.tucha.passpass.core.model.UserPrivilege;

public interface UserPrivilegeRepository extends CrudRepository<UserPrivilege, Long> {
    UserPrivilege findByName(String name);
}
