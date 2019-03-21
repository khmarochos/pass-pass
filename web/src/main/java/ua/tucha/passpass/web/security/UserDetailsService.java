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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service("webUserDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsFactory userDetailsFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        UserDetails userDetails;
        if(user == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        try {
            userDetails = userDetailsFactory.getObjectForUser(user);
        } catch (Exception e) {
            throw new UsernameNotFoundException("Something went wrong");
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
