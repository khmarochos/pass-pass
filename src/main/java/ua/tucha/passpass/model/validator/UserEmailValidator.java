package ua.tucha.passpass.model.validator;

import org.apache.commons.validator.routines.EmailValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public void initialize(ValidEmail constraintAnnotation) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validateUserEmail(value);
    }

    private boolean validateUserEmail(String email) {
        return emailValidator.getInstance().isValid(email);
    }
}
