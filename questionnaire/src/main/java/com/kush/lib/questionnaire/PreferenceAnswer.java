package com.kush.lib.questionnaire;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class PreferenceAnswer implements ContentContainer, Identifiable {

    private final Identifier answerId;
    private final PreferenceOption option;
    private final Identifier addedBy;
    private final Preference preference;

    public PreferenceAnswer(PreferenceOption option, Identifier addedBy, Preference preference) {
        this(Identifier.NULL, option, addedBy, preference);
    }

    public PreferenceAnswer(Identifier answerId, PreferenceAnswer answer) {
        this(answerId, answer.getOption(), answer.getAddedBy(), answer.getPreference());
    }

    public PreferenceAnswer(Identifier answerId, PreferenceOption option, Identifier addedBy, Preference preference) {
        this.answerId = answerId;
        this.option = option;
        this.addedBy = addedBy;
        this.preference = preference;
    }

    @Override
    public Identifier getId() {
        return answerId;
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
}
