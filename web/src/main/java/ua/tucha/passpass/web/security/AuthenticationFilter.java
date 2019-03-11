//package ua.tucha.passpass.web.security;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    @Override
//    public Authentication attemptAuthentication(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws AuthenticationException {
//        AuthenticationToken authenticationToken = getAuthenticationRequest(request);
//        setDetails(request, authenticationToken);
//        return getAuthenticationManager().authenticate(authenticationToken);
//    }
//
//    private AuthenticationToken getAuthenticationRequest(HttpServletRequest request) {
//        String username = obtainUsername(request);
//        String password = obtainPassword(request);
//        return new AuthenticationToken(username, password);
//    }
//
//}
