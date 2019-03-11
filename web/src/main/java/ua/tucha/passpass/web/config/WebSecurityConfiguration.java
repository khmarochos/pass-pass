package ua.tucha.passpass.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import ua.tucha.passpass.web.router.RouteRegistry;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/user/*")
                        .permitAll()
                    .antMatchers("/static/**")
                        .permitAll()
                .anyRequest()
                        .authenticated()
                    .and()
                .formLogin()
                    .loginPage(RouteRegistry.UserRouteRegistry.SIGN_IN)
                        .permitAll()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .and()
                .logout()
                    .logoutUrl(RouteRegistry.UserRouteRegistry.SIGN_OUT)
                    .logoutSuccessUrl(RouteRegistry.UserRouteRegistry.SIGN_IN);
    }

//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("vladimir@melnik.net.ua")
//                        .password("12345678")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
}