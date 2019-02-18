package com.kush.lib.questionnaire.ans;

import com.kush.utils.id.Identifier;

public class IntegerAnswer extends OptionAnswer<Integer> {

    public IntegerAnswer(Identifier answerId, Identifier answerer, OptionResponse<Integer> response) {
        super(answerId, answerer, response);
    }
}
