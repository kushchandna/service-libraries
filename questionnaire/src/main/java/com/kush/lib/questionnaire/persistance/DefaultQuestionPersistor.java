package com.kush.lib.questionnaire.persistance;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.questionnaire.core.Question;
import com.kush.utils.id.Identifier;

public class DefaultQuestionPersistor extends DelegatingPersistor<Question> implements QuestionPersistor {

    public DefaultQuestionPersistor(Persistor<Question> delegate) {
        super(delegate);
    }

    @Override
    public Question createQuestion(String content, Identifier addedBy) throws PersistorOperationFailedException {
        Question question = new Question(content, addedBy);
        return save(question);
    }
}
