package com.kush.lib.survey.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class MultipleChoiceQuestion implements Question, Identifiable {

    private final Identifier quesId;
    private final String text;
    private final Collection<Choice> choices;
    private final ChoiceSelectionPolicy choicePolicy;

    public MultipleChoiceQuestion(String text, List<Choice> choices, ChoiceSelectionPolicy choicePolicy) {
        this(Identifier.NULL, text, choices, choicePolicy);
    }

    public MultipleChoiceQuestion(Identifier quesId, String text, List<Choice> choices, ChoiceSelectionPolicy choicePolicy) {
        this.quesId = quesId;
        this.text = text;
        this.choices = new ArrayList<>(choices);
        this.choicePolicy = choicePolicy;
    }

    @Override
    public Identifier getId() {
        return quesId;
    }

    public String getText() {
        return text;
    }

    public Collection<Choice> getChoices() {
        return Collections.unmodifiableCollection(choices);
    }

    public ChoiceSelectionPolicy getChoicePolicy() {
        return choicePolicy;
    }
}
