package com.kush.lib.questionnaire;

import java.util.List;
import java.util.Optional;

import com.kush.lib.persistence.api.DelegatingPersister;
import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.utils.id.Identifier;

public class DefaultPreferenceQuestionPersistor extends DelegatingPersister<PreferenceQuestion>
        implements PreferenceQuestionPersister {

    private final Persister<PreferenceOption> optionPersistor;
    private final Persister<PreferenceAnswer> answerPersistor;

    public DefaultPreferenceQuestionPersistor(Persister<PreferenceQuestion> delegate,
            Persister<PreferenceOption> optionPersistor, Persister<PreferenceAnswer> answerPersistor) {
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

    @Override
    public Optional<PreferenceOption> getOption(Identifier questionId, String content) throws PersistorOperationFailedException {
        List<PreferenceOption> matchingOptions = optionPersistor.fetch(opt -> optionMatch(questionId, content, opt));
        if (matchingOptions.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(matchingOptions.get(0));
        }
    }

    private boolean optionMatch(Identifier questionId, String content, PreferenceOption opt) {
        return opt.getQuestionId().equals(questionId) && opt.getContent().equals(content);
    }
}
