package ua.tucha.passpass.core.service.exception;

public class VerificationTokenNotFoundException extends VerificationTokenException {

    public VerificationTokenNotFoundException(){
        super();
    }

    public VerificationTokenNotFoundException(String message) {
        super(message);
    }

}
