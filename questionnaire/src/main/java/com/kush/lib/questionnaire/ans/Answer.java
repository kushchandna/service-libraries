package com.kush.lib.questionnaire.ans;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class Answer<T> implements Identifiable {

    private final Identifier answerId;
    private final Identifier answerer;
    private final T value;

    public Answer(Identifier answerId, Identifier answerer, T value) {
        this.answerId = answerId;
        this.answerer = answerer;
        this.value = value;
    }

    @Override
    public Identifier getId() {
        return answerId;
    }

    public Identifier getAnswerer() {
        return answerer;
    }

    public T getValue() {
        return value;
    }
}
