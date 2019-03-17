package ua.tucha.passpass.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.core.model.UserPrivilege;
import ua.tucha.passpass.core.model.UserRole;
import ua.tucha.passpass.core.repository.UserRoleRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserRoleService {

    private UserRoleRepository userRoleRepository;

    @Autowired
    public UserRoleService(UserRoleRepository userRoleRepository) {
       this.userRoleRepository = userRoleRepository;
    }

    @Transactional
    public UserRole findOrCreate(String name, List<UserPrivilege> userPrivilegeList) {
        UserRole userRole = userRoleRepository.findByName(name);
        if (userRole == null) {
            userRole = new UserRole(name);
            userRole.setUserPrivilegeList(userPrivilegeList);
            userRoleRepository.save(userRole);
        }
        return userRole;
    }

}