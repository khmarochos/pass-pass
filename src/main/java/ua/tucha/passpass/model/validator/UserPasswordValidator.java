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
import java.util.ArrayList;
import java.util.Map;

import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;

@Slf4j
public class UserPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private class MessageResolverProxy implements MessageResolver {
        private final PropertiesMessageResolver messageResolver = new PropertiesMessageResolver();
        private static final String MESSAGE_LABEL_PREFIX = "validator.password.";
        @Override
        public String resolve(@NotNull RuleResultDetail detail) {
            String errorCode
                    = MESSAGE_LABEL_PREFIX
                    + UPPER_UNDERSCORE.to(LOWER_UNDERSCORE, detail.getErrorCode());
            Map<String, Object> errorDetails = detail.getParameters();
            return
                    messageResolver.resolve(
                            new RuleResultDetail(
                                    errorCode,
                                    errorDetails
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
        ArrayList<String> messageContainer = new ArrayList<>();
        if (!validateUserPassword(value, messageContainer)) {
            context.disableDefaultConstraintViolation();
            for(String messageLabel: messageContainer) {
                context.buildConstraintViolationWithTemplate("{" + messageLabel + "}").addConstraintViolation();
            }
            return false;
        }
        return true;
    }

    private boolean validateUserPassword(@NotNull String password, @NotNull ArrayList<String> messageContainer) {
        PasswordData passwordData = new PasswordData(password);
        RuleResult validate = passwordValidator.validate(passwordData);
        if (!validate.isValid()) {
            messageContainer.addAll(passwordValidator.getMessages(validate));
            return false;
        }
        return true;
    }

}
