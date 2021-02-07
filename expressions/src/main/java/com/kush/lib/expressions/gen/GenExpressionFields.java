package com.kush.lib.expressions.gen;

import static java.util.Collections.emptyList;

import java.util.List;

public class GenExpressionFields {

    private List<GenExpressionField> childExpressions;
    private List<GenExpressionField> otherFields;
    
    public GenExpressionFields() {
    }

    public GenExpressionFields(List<GenExpressionField> childExpressions) {
        this(childExpressions, emptyList());
    }

    public GenExpressionFields(List<GenExpressionField> childExpressions, List<GenExpressionField> otherFields) {
        this.childExpressions = childExpressions;
        this.otherFields = otherFields;
    }

    public List<GenExpressionField> getChildExpressions() {
        return childExpressions;
    }

    public List<GenExpressionField> getOtherFields() {
        return otherFields;
    }
}
