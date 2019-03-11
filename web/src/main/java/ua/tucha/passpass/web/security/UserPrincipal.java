package ua.tucha.passpass.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.tucha.passpass.core.model.User;

import java.util.Collection;

public class UserPrincipal implements UserDetails {

    private User user;

    @Autowired
    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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


