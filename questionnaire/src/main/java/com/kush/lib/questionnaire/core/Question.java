package com.kush.lib.questionnaire.core;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Question implements ObjectWithContent, Identifiable {

    private final Identifier questionId;
    private final String content;
    private final Identifier addedBy;

    public Question(Identifier questionId, String content, Identifier addedBy) {
        this.questionId = questionId;
        this.content = content;
        this.addedBy = addedBy;
    }

    @Override
    public Identifier getId() {
        return questionId;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Identifier getAddedBy() {
        return addedBy;
    }
}
