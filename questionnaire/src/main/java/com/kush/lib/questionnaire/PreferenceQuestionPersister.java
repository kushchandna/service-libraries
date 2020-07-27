package com.kush.lib.questionnaire;

import java.util.List;
import java.util.Optional;

import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public interface PreferenceQuestionPersister extends Persister<PreferenceQuestion> {

    PreferenceQuestion createQuestion(String content, Identifier addedBy) throws PersistorOperationFailedException;

    PreferenceOption addOption(Identifier questionId, String content, Identifier addedBy)
            throws PersistorOperationFailedException;

    PreferenceAnswer addAnswer(Identifier questionId, Identifier optionId, Preference preference, Identifier addedBy)
            throws PersistorOperationFailedException;

    List<PreferenceAnswer> fetchAnswers(Identifier questionId) throws PersistorOperationFailedException;

    List<PreferenceOption> fetchOptions(Identifier questionId) throws PersistorOperationFailedException;

    Optional<PreferenceOption> getOption(Identifier questionId, String content) throws PersistorOperationFailedException;
}
