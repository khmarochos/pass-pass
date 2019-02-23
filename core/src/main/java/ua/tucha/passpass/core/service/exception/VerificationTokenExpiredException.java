package ua.tucha.passpass.core.service.exception;

public class VerificationTokenExpiredException extends VerificationTokenException {

    public VerificationTokenExpiredException(){
        super();
    }

    public VerificationTokenExpiredException(String message) {
        super(message);
    }

}
