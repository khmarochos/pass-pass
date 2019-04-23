package ua.tucha.passpass.web.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class FrontendMessage {

    @NotNull
    private MessageType type;

    @NotNull
    private String title;

    @NotNull
    private String body;

    private LocalDateTime sent;

    private LocalDateTime received;

    public enum MessageType {
        SUCCESS,
        INFO,
        WARNING,
        ERROR
    }

    public FrontendMessage(MessageType messageType, String messageTitle, String messageBody) {
        this.type = messageType;
        this.title = messageTitle;
        this.body = messageBody;
    }
}


