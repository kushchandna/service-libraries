package com.kush.lib.questionnaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class PreferenceQuestion implements ContentContainer, Identifiable {

    private final Identifier questionId;
    private final String content;
    private final Identifier addedBy;
    private final List<PreferenceOption> options;

    public PreferenceQuestion(Identifier questionId, String content, Identifier addedBy, List<PreferenceOption> options) {
        this.questionId = questionId;
        this.content = content;
        this.addedBy = addedBy;
        this.options = new ArrayList<>(options);
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

    public List<PreferenceOption> getPreferenceOptions() {
        return Collections.unmodifiableList(options);
    }
}
