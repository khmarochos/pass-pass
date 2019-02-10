package ua.tucha.passpass.service.exception;

public class EmailNotUniqueException extends Exception {

    public EmailNotUniqueException(){
        super();
    }

    public EmailNotUniqueException(String message) {
        super(message);
    }

}
