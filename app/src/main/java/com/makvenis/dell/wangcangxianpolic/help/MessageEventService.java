package com.makvenis.dell.wangcangxianpolic.help;

/**
 * Created by dell on 2018/5/24.
 */

public class MessageEventService {

    private String message;

    private boolean isBoolean;


    public MessageEventService(String message, boolean isBoolean) {
        this.message = message;
        this.isBoolean = isBoolean;
    }

    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isBoolean() {
        return isBoolean;
    }

    public void setBoolean(boolean aBoolean) {
        isBoolean = aBoolean;
    }

    @Override
    public String toString() {
        return "MessageEventService{" +
                "message='" + message + '\'' +
                ", isBoolean=" + isBoolean +
                '}';
    }
}
