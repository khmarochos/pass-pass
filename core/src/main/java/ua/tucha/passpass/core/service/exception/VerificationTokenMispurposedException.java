package ua.tucha.passpass.core.service.exception;

public class VerificationTokenMispurposedException extends VerificationTokenException {

    public VerificationTokenMispurposedException(){
        super();
    }

    public VerificationTokenMispurposedException(String message) {
        super(message);
    }

}
