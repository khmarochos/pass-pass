package ua.tucha.passpass.web.security;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import ua.tucha.passpass.core.model.User;

import java.util.Collection;

@Slf4j
@NoArgsConstructor
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    @Setter
    private User user;

    @Autowired
    private UserDetailsService userDetailsService;

    public UserDetails(User user) {
        this.user = user;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(userDetailsService == null) {
            log.warn("OOPS!");
        }
        return userDetailsService.getUserGrantedAuthorities(user);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        // FIXME
        return user.getRemoved() == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        // FIXME
        return user.getDisabled() == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // FIXME
        return user.getDisabled() == null;
    }

    @Override
    public boolean isEnabled() {
        // FIXME
        return user.getDisabled() == null;
    }
}


