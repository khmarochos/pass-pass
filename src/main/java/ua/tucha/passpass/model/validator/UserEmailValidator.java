package ua.tucha.passpass.model.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import ua.tucha.passpass.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class UserEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private final static EmailValidator emailValidator = EmailValidator.getInstance();
    private final UserService userService;

    @Autowired
    public UserEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void initialize(ValidEmail constraintAnnotation) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!emailValidator.isValid(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{validator.email.invalid}").addConstraintViolation();
            return false;
        } else if(userService.emailExists(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{validator.email.exists}").addConstraintViolation();
            return false;
        }
        return true;
    }

}
