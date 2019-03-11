//package ua.tucha.passpass.web.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import ua.tucha.passpass.core.service.UserService;
//
//public class UserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
//
//    private UserService userService;
//
//    @Autowired
//    public UserDetailsAuthenticationProvider(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Override
//    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
//
//    }
//
//    @Override
//    UserDetails retrieveUser (
//            String username,
//            UsernamePasswordAuthenticationToken authentication
//    ) {
//        AuthenticationToken auth = (AuthenticationToken) authentication;
//        UserDetails loadedUser;
//
//        try {
//            loadedUser = this.userService.findUserByEmail(auth.getPrincipal().toString());
//        } catch (UsernameNotFoundException notFound) {
//
//            if (authentication.getCredentials() != null) {
//                String presentedPassword = authentication.getCredentials()
//                        .toString();
//                passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
//            }
//            throw notFound;
//        } catch (Exception repositoryProblem) {
//
//            throw new InternalAuthenticationServiceException(
//                    repositoryProblem.getMessage(), repositoryProblem);
//        }
//
//        // ...
//
//        return loadedUser;
//    }
//}
