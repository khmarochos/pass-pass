package ua.tucha.passpass.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.core.model.UserPrivilege;
import ua.tucha.passpass.core.repository.UserPrivilegeRepository;

import javax.transaction.Transactional;

@Service
public class UserPrivilegeService {

    private UserPrivilegeRepository userPrivilegeRepository;

    @Autowired
    public UserPrivilegeService(UserPrivilegeRepository userPrivilegeRepository) {
        this.userPrivilegeRepository = userPrivilegeRepository;
    }

    @Transactional
    public UserPrivilege findOrCreate(String name) {
        UserPrivilege userPrivilege = userPrivilegeRepository.findByName(name);
        if (userPrivilege == null) {
            userPrivilege = new UserPrivilege(name);
            userPrivilegeRepository.save(userPrivilege);
        }
        return userPrivilege;
    }

}
