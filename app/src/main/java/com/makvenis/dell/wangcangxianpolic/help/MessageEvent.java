package com.makvenis.dell.wangcangxianpolic.help;

/* 创建EventBus消息机制 */

public class MessageEvent {

    /* 用于消息的传递(也可以当作具体的消息内容) */
    public String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
