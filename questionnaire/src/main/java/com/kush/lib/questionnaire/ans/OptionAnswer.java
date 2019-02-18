package com.kush.lib.questionnaire.ans;

import com.kush.utils.id.Identifiable;
import com.kush.utils.id.Identifier;

public class OptionAnswer<T> extends Answer<T> implements Identifiable {

    private final Identifier optionId;

    public OptionAnswer(Identifier answerId, Identifier answerer, OptionResponse<T> response) {
        super(answerId, answerer, response.getResponse());
        this.optionId = response.getOptionId();
    }

    public Identifier getOptionId() {
        return optionId;
    }
}
