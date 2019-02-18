package com.kush.lib.questionnaire.persistance;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.questionnaire.core.Question;
import com.kush.utils.id.Identifier;

public interface QuestionPersistor extends Persistor<Question> {

    Question createQuestion(String content, Identifier addedBy) throws PersistorOperationFailedException;
}
