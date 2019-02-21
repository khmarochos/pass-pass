package ua.tucha.passpass.core.model.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import ua.tucha.passpass.core.service.UserService;
import ua.tucha.passpass.core.util.ApplicationContextProvider;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class UserEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static UserService userService;
    private final static EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        // I would gladly use @Autowired, but have to implement this ugly hack, because
        // a custom validator called by Hibernate isn't being injected by Spring :-(
        userService = (UserService) ApplicationContextProvider.getBean(UserService.class);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return emailValidator.isValid(value);
    }

}
