package com.kush.lib.questionnaire;

import java.util.List;

import com.kush.lib.persistence.api.DelegatingPersistor;
import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultPreferenceQuestionPersistor extends DelegatingPersistor<PreferenceQuestion>
        implements PreferenceQuestionPersistor {

    private final Persistor<PreferenceOption> optionPersistor;
    private final Persistor<PreferenceAnswer> answerPersistor;

    public DefaultPreferenceQuestionPersistor(Persistor<PreferenceQuestion> delegate,
            Persistor<PreferenceOption> optionPersistor, Persistor<PreferenceAnswer> answerPersistor) {
        super(delegate);
        this.optionPersistor = optionPersistor;
        this.answerPersistor = answerPersistor;
    }

    @Override
    public PreferenceQuestion createQuestion(String content, Identifier addedBy) throws PersistorOperationFailedException {
        PreferenceQuestion question = new PreferenceQuestion(content, addedBy);
        return save(question);
    }

    @Override
    public PreferenceOption addOption(Identifier questionId, String content, Identifier addedBy)
            throws PersistorOperationFailedException {
        PreferenceOption option = new PreferenceOption(questionId, content, addedBy);
        return optionPersistor.save(option);
    }

    @Override
    public PreferenceAnswer addAnswer(Identifier questionId, Identifier optionId, Preference preference, Identifier addedBy)
            throws PersistorOperationFailedException {
        PreferenceOption option = optionPersistor.fetch(optionId);
        PreferenceAnswer answer = new PreferenceAnswer(questionId, option, addedBy, preference);
        return answerPersistor.save(answer);
    }

    @Override
    public List<PreferenceOption> fetchOptions(Identifier questionId) throws PersistorOperationFailedException {
        return optionPersistor.fetch(opt -> opt.getQuestionId().equals(questionId));
    }

    @Override
    public List<PreferenceAnswer> fetchAnswers(Identifier questionId) throws PersistorOperationFailedException {
        return answerPersistor.fetch(ans -> ans.getQuestionId().equals(questionId));
    }
}
