package ua.tucha.passpass.web.security;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import ua.tucha.passpass.core.model.User;

@Component
public class UserDetailsFactory implements FactoryBean<UserDetails> {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    public UserDetails getObjectForUser(User user) throws Exception {
        UserDetails userDetails = getObject();
        userDetails.setUser(user);
        return userDetails;
    }

    @Override
    public UserDetails getObject() throws Exception {
        UserDetails userDetails = new UserDetails();
        beanFactory.autowireBean(userDetails);
        return userDetails;
    }

    @Override
    public Class<?> getObjectType() {
        return UserDetails.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
