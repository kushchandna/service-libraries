package com.kush.lib.questionnaire;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;

public class PreferenceQuestion implements ContentContainer, Identifiable {

    private final Identifier questionId;
    private final String content;
    private final Identifier addedBy;

    public PreferenceQuestion(String content, Identifier addedBy) {
        this(Identifier.NULL, content, addedBy);
    }

    public PreferenceQuestion(Identifier questionId, PreferenceQuestion question) {
        this(questionId, question.getContent(), question.getAddedBy());
    }

    public PreferenceQuestion(Identifier questionId, String content, Identifier addedBy) {
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
