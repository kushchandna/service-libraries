package com.kush.lib.questionnaire;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class PreferenceOption implements ContentContainer, Identifiable {

    private final Identifier optionId;
    private final String content;
    private final Identifier addedBy;

    public PreferenceOption(Identifier optionId, PreferenceOption option) {
        this(optionId, option.getContent(), option.getAddedBy());
    }

    public PreferenceOption(Identifier optionId, String content, Identifier addedBy) {
        this.optionId = optionId;
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
}
