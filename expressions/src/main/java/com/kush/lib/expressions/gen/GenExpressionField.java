package com.kush.lib.expressions.gen;

public class GenExpressionField {

    private String name;
    private String type;

    public GenExpressionField() {
    }

    public GenExpressionField(String name) {
        this(name, null);
    }

    public GenExpressionField(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
