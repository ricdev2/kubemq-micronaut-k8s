package com.ric.dev2.dto;

import java.io.Serializable;

public class MessageDTO implements Serializable {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}