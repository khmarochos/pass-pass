package ua.tucha.passpass.core.service.exception;

public class EmailNotUniqueException extends Exception {

    public EmailNotUniqueException(){
        super();
    }

    public EmailNotUniqueException(String message) {
        super(message);
    }

}
