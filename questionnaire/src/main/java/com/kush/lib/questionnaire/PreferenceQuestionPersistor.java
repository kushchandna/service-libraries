package com.kush.lib.questionnaire;

import java.util.List;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface PreferenceQuestionPersistor extends Persistor<PreferenceQuestion> {

    PreferenceQuestion createQuestion(String content, Identifier addedBy) throws PersistorOperationFailedException;

    PreferenceOption addOption(Identifier questionId, String content, Identifier addedBy)
            throws PersistorOperationFailedException;

    PreferenceAnswer addAnswer(Identifier questionId, Identifier optionId, Preference preference, Identifier addedBy)
            throws PersistorOperationFailedException;

    List<PreferenceAnswer> fetchAnswers(Identifier questionId) throws PersistorOperationFailedException;

    List<PreferenceOption> fetchOptions(Identifier questionId) throws PersistorOperationFailedException;
}
