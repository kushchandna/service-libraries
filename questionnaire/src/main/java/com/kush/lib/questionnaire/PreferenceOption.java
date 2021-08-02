package com.kush.lib.questionnaire;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;

public class PreferenceOption implements ContentContainer, Identifiable {

    private final Identifier optionId;
    private Identifier questionId;
    private final String content;
    private final Identifier addedBy;

    public PreferenceOption(Identifier optionId, PreferenceOption option) {
        this(optionId, option.getQuestionId(), option.getContent(), option.getAddedBy());
    }

    public PreferenceOption(Identifier questionId, String content, Identifier addedBy) {
        this(Identifier.NULL, questionId, content, addedBy);
    }

    public PreferenceOption(Identifier optionId, Identifier questionId, String content, Identifier addedBy) {
        this.optionId = optionId;
        this.questionId = questionId;
        this.content = content;
        this.addedBy = addedBy;
    }

    @Override
    public Identifier getId() {
        return optionId;
    }

    public void setQuestionId(Identifier questionId) {
        this.questionId = questionId;
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
