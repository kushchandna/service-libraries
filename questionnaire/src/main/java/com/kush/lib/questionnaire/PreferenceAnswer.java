package com.kush.lib.questionnaire;

import com.kush.commons.id.Identifiable;
import com.kush.commons.id.Identifier;

public class PreferenceAnswer implements ContentContainer, Identifiable {

    private final Identifier answerId;
    private final Identifier questionId;
    private final PreferenceOption option;
    private final Identifier addedBy;
    private final Preference preference;

    public PreferenceAnswer(Identifier questionId, PreferenceOption option, Identifier addedBy, Preference preference) {
        this(Identifier.NULL, questionId, option, addedBy, preference);
    }

    public PreferenceAnswer(Identifier answerId, PreferenceAnswer answer) {
        this(answerId, answer.getQuestionId(), answer.getOption(), answer.getAddedBy(), answer.getPreference());
    }

    public PreferenceAnswer(Identifier answerId, Identifier questionId, PreferenceOption option, Identifier addedBy,
            Preference preference) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.option = option;
        this.addedBy = addedBy;
        this.preference = preference;
    }

    @Override
    public Identifier getId() {
        return answerId;
    }

    public Identifier getQuestionId() {
        return questionId;
    }

    @Override
    public String getContent() {
        return option.getContent();
    }

    @Override
    public Identifier getAddedBy() {
        return addedBy;
    }

    public PreferenceOption getOption() {
        return option;
    }

    public Preference getPreference() {
        return preference;
    }

    @Override
    public String toString() {
        return "PreferenceAnswer [answerId=" + answerId + ", questionId=" + questionId + ", addedBy=" + addedBy + ", preference="
                + preference + "]";
    }

}
