package ua.tucha.passpass.model.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static org.apache.commons.validator.routines.EmailValidator emailValidator
             = org.apache.commons.validator.routines.EmailValidator.getInstance();

    @Override
    public void initialize(ValidEmail constraintAnnotation) { }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validateEmail(value);
    }

    private boolean validateEmail(String email) {
        return emailValidator.getInstance().isValid(email);
    }

}
