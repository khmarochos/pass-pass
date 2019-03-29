package ua.tucha.passpass.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.tucha.passpass.core.model.User;
import ua.tucha.passpass.core.model.UserRole;
import ua.tucha.passpass.core.repository.UserRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service("webUserDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;

    @Resource(name = "&userDetails")
    private UserDetailsFactory userDetailsFactory;

    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if(user == null) {
            log.debug("Can't find user {}", username);
            throw new UsernameNotFoundException("Username not found");
        }
        UserDetails userDetails;
        try {
            userDetails = userDetailsFactory.getObjectForUser(user);
        } catch (Exception e) {
            log.error("An exception raised: {}", e.toString());
            throw new UsernameNotFoundException("Something gone wrong: " + e.toString());
        }
        return userDetails;
    }


    public Collection<? extends GrantedAuthority> getUserGrantedAuthorities(User user) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (UserRole userRole : user.getUserRoleList()) {
            authorityList.add(new SimpleGrantedAuthority(userRole.getName()));
        }
        return authorityList;
    }
}
