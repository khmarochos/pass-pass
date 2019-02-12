package ua.tucha.passpass.model.validator;

import lombok.extern.slf4j.Slf4j;
import org.passay.LengthRule;
import org.passay.MessageResolver;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.PropertiesMessageResolver;
import org.passay.RuleResult;
import org.passay.RuleResultDetail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;

@Slf4j
public class UserPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private class MessageResolverProxy implements MessageResolver {
        private final PropertiesMessageResolver messageResolver = new PropertiesMessageResolver();
        @Override
        public String resolve(@NotNull RuleResultDetail detail) {
            return
                    messageResolver.resolve(
                            new RuleResultDetail(
                                    "validator.password." + detail.getErrorCode(),
                                    detail.getParameters()
                            )
                    );
        }
    }

    private MessageResolverProxy messageResolver = new MessageResolverProxy(); // TODO: consider making it static
    private PasswordValidator passwordValidator = new PasswordValidator(
            messageResolver,
            new LengthRule(8)
    );

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!validateUserEmail(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("{validator.password.default}").addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateUserEmail(@NotNull String password) {
        log.debug("Password validator validates the password :-)");
        PasswordData passwordData = new PasswordData(password);
        RuleResult validate = passwordValidator.validate(passwordData);
        if (!validate.isValid()) {
            RuleResultDetail ruleResultDetail = validate.getDetails().get(0);
            log.debug("Password validator is disappointed: {}", passwordValidator.getMessages(validate));
            return false;
        }
        return true;
    }

}
