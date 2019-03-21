package ua.tucha.passpass.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ua.tucha.passpass.core.model.UserPrivilege;
import ua.tucha.passpass.core.model.UserRole;
import ua.tucha.passpass.core.service.UserPrivilegeService;
import ua.tucha.passpass.core.service.UserRoleService;
import ua.tucha.passpass.core.service.UserService;

import java.util.Arrays;

@Component
public class SetupDatabase implements ApplicationListener<ContextRefreshedEvent> {

    private UserService userService;
    private UserRoleService userRoleService;
    private UserPrivilegeService userPrivilegeService;
    private boolean needSetup;

    @Autowired
    public SetupDatabase(UserService userService, UserRoleService userRoleService, UserPrivilegeService userPrivilegeService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.userPrivilegeService = userPrivilegeService;
        this.needSetup = true;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!needSetup) return;
        UserRole userRole = userRoleService.findOrCreate("ROLE_USER", Arrays.asList(new UserPrivilege[]{
                userPrivilegeService.findOrCreate("note_own_read"),
                userPrivilegeService.findOrCreate("note_own_send"),
                userPrivilegeService.findOrCreate("note_own_delete")
        }));
        needSetup = false;
    }

}
