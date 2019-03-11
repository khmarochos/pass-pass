package ua.tucha.passpass.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ua.tucha.passpass.core.service.UserService;

import java.util.ArrayList;

@Slf4j
@Component
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private UserService userService;

    @Autowired
    public AuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();
        if(isPasswordCorrect(email, password)) {
            return new UsernamePasswordAuthenticationToken(email, password, new ArrayList<>());
        } else {
            throw new BadCredentialsException("ZALOOPA"); // FIXME
        }
    }

    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean isPasswordCorrect(String email, String password) {
        return(userService.findUserByEmail(email).getPassword().equals(password));
    }

}
