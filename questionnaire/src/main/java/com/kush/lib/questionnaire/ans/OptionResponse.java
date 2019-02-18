package com.kush.lib.questionnaire.ans;

import com.kush.utils.id.Identifier;

public class OptionResponse<T> {

    private final Identifier optionId;
    private final T response;

    public OptionResponse(Identifier optionId, T response) {
        this.optionId = optionId;
        this.response = response;
    }

    public Identifier getOptionId() {
        return optionId;
    }

    public T getResponse() {
        return response;
    }
}
