package com.kush.messaging.content;

import com.kush.lib.service.server.annotations.Exportable;

@Exportable
public class TextContent implements Content {

    private static final long serialVersionUID = 1L;

    private final String text;

    public TextContent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "TextContent [text=" + text + "]";
    }
}
