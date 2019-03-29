package ua.tucha.passpass.core.service.exception;

public class VerificationTokenNotAppliedException extends VerificationTokenException {

    public VerificationTokenNotAppliedException(){
        super();
    }

    public VerificationTokenNotAppliedException(String message) {
        super(message);
    }

}
