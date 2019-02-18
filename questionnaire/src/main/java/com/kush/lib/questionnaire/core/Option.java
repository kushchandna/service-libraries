package com.kush.lib.questionnaire.core;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Option implements ObjectWithContent, Identifiable {

    private final Identifier optionId;
    private final Identifier questionId;
    private final String content;
    private final Identifier addedBy;

    public Option(Identifier optionId, Identifier questionId, String content, Identifier addedBy) {
        this.optionId = optionId;
        this.questionId = questionId;
        this.content = content;
        this.addedBy = addedBy;
    }

    @Override
    public Identifier getId() {
        return optionId;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Identifier getAddedBy() {
        return addedBy;
    }

    public Identifier getQuestionId() {
        return questionId;
    }
}
