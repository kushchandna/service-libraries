package com.kush.lib.questionnaire;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.service.BaseService;
import com.kush.utils.id.Identifier;

public class PreferenceQuestionService extends BaseService {

    public PreferenceQuestion createQuestion(String content) throws PersistorOperationFailedException {
        Identifier addedBy = getCurrentUser().getId();
        PreferenceQuestionPersistor persistor = getInstance(PreferenceQuestionPersistor.class);
        return persistor.createQuestion(content, addedBy);
    }

    public PreferenceOption addOption(Identifier questionId, String content) throws PersistorOperationFailedException {
        Identifier addedBy = getCurrentUser().getId();
        PreferenceQuestionPersistor persistor = getInstance(PreferenceQuestionPersistor.class);
        return persistor.addOption(questionId, content, addedBy);
    }

    public PreferenceOption addOptionIfDoesNotExist(Identifier questionId, String content)
            throws PersistorOperationFailedException {
        Identifier addedBy = getCurrentUser().getId();
        PreferenceQuestionPersistor persistor = getInstance(PreferenceQuestionPersistor.class);
        Optional<PreferenceOption> option = persistor.getOption(questionId, content);
        if (option.isPresent()) {
            return option.get();
        } else {
            return persistor.addOption(questionId, content, addedBy);
        }
    }

    public PreferenceAnswer addAnswer(Identifier questionId, Identifier optionId, Preference preference)
            throws PersistorOperationFailedException {
        Identifier addedBy = getCurrentUser().getId();
        PreferenceQuestionPersistor persistor = getInstance(PreferenceQuestionPersistor.class);
        return persistor.addAnswer(questionId, optionId, preference, addedBy);
    }

    public List<PreferenceQuestion> getAllQuestions() throws PersistorOperationFailedException {
        PreferenceQuestionPersistor persistor = getQuestionPersistor();
        return persistor.fetchAll();
    }

    public PreferenceQuestion getQuestion(Identifier questionId) throws PersistorOperationFailedException {
        PreferenceQuestionPersistor persistor = getQuestionPersistor();
        return persistor.fetch(questionId);
    }

    public List<PreferenceOption> getOptions(Identifier questionId) throws PersistorOperationFailedException {
        PreferenceQuestionPersistor persistor = getQuestionPersistor();
        return persistor.fetchOptions(questionId);
    }

    public List<PreferenceAnswer> getAnswers(Identifier questionId) throws PersistorOperationFailedException {
        PreferenceQuestionPersistor persistor = getQuestionPersistor();
        return persistor.fetchAnswers(questionId);
    }

    public Map<Preference, List<PreferenceAnswer>> getAnswersSummary(Identifier questionId)
            throws PersistorOperationFailedException {
        Map<Preference, List<PreferenceAnswer>> ansSummary = new HashMap<>();
        ansSummary.put(Preference.PREFERRED, new LinkedList<>());
        ansSummary.put(Preference.NEUTRAL, new LinkedList<>());
        ansSummary.put(Preference.REJECTED, new LinkedList<>());
        List<PreferenceAnswer> answers = getAnswers(questionId);
        answers.stream().forEach(ans -> addToSummary(ansSummary, ans));
        return ansSummary;
    }

    private boolean addToSummary(Map<Preference, List<PreferenceAnswer>> ansSummary, PreferenceAnswer ans) {
        return ansSummary.get(ans.getPreference()).add(ans);
    }

    private PreferenceQuestionPersistor getQuestionPersistor() {
        return getInstance(PreferenceQuestionPersistor.class);
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(PreferenceQuestionPersistor.class);
    }
}
