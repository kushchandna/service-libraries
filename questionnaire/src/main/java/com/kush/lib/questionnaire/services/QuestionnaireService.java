package com.kush.lib.questionnaire.services;

import java.util.List;

import com.kush.lib.persistence.api.PersistorOperationFailedException;
import com.kush.lib.questionnaire.core.Option;
import com.kush.lib.questionnaire.core.Question;
import com.kush.lib.questionnaire.persistance.OptionPersistor;
import com.kush.lib.questionnaire.persistance.QuestionPersistor;
import com.kush.service.BaseService;
import com.kush.utils.id.Identifier;

public class QuestionnaireService extends BaseService {

    public Question createQuestion(String content) throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        QuestionPersistor questionPersistor = getInstance(QuestionPersistor.class);
        return questionPersistor.createQuestion(content, currentUserId);
    }

    public Option addOption(Identifier questionId, String content) throws PersistorOperationFailedException {
        Identifier currentUserId = getCurrentUser().getId();
        OptionPersistor optionPersistor = getInstance(OptionPersistor.class);
        return optionPersistor.addOption(questionId, content, currentUserId);
    }

    public List<Question> getAllQuestions() throws PersistorOperationFailedException {
        QuestionPersistor questionPersistor = getInstance(QuestionPersistor.class);
        return questionPersistor.fetchAll();
    }

    public Question getQuestion(Identifier questionId) throws PersistorOperationFailedException {
        QuestionPersistor questionPersistor = getInstance(QuestionPersistor.class);
        return questionPersistor.fetch(questionId);
    }

    public List<Option> getOptions(Identifier questionId) throws PersistorOperationFailedException {
        OptionPersistor optionPersistor = getInstance(OptionPersistor.class);
        return optionPersistor.fetch(opt -> questionId.equals(opt.getQuestionId()));
    }

    @Override
    protected void processContext() {
        checkContextHasValueFor(QuestionPersistor.class);
        checkContextHasValueFor(OptionPersistor.class);
    }
}
