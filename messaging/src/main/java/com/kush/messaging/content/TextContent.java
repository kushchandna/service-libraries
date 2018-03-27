package com.kush.messaging.content;

public class TextContent implements Content {

    private final String text;

    public TextContent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
