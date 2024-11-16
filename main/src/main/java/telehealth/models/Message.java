package telehealth.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private String fromEmail;
    private String toEmail;
    private String messageText;

    @JsonCreator
    public Message(@JsonProperty("fromEmail") String fromEmail, @JsonProperty("toEmail") String toEmail, @JsonProperty("messageText") String messageText) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.messageText = messageText;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
}
