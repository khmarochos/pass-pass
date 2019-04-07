package ua.tucha.passpass.web.model;

import java.util.LinkedList;

public class FrontendMessageStack {

    private LinkedList<FrontendMessage> frontendMessageList = new LinkedList<>();

    public void pushFrontendMessage(FrontendMessage frontendMessage) {
        frontendMessageList.add(frontendMessage);
    }

    public FrontendMessage popFrontendMessage() {
        return frontendMessageList.pollLast();
    }

    public int hasFrontendMessage() {
        return frontendMessageList.size();
    }

}
