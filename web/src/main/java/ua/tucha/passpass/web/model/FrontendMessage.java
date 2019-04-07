package ua.tucha.passpass.web.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class FrontendMessage {

    @NotNull
    private MessageType messageType;

    @NotNull
    private String messageTitle;

    @NotNull
    private String messageBody;

    private LocalDateTime sent;

    private LocalDateTime received;

    public enum MessageType {
        SUCCESS,
        INFO,
        WARNING,
        ERROR
    }

    public FrontendMessage(MessageType messageType, String messageTitle, String messageBody) {
        this.messageType = messageType;
        this.messageTitle = messageTitle;
        this.messageBody = messageBody;
    }
}


