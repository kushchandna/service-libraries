package com.kush.lib.questionnaire.services;

import java.util.List;

import com.kush.lib.questionnaire.core.Option;
import com.kush.lib.questionnaire.core.Question;
import com.kush.lib.questionnaire.persistance.OptionPersistor;
import com.kush.lib.questionnaire.persistance.QuestionPersistor;
import com.kush.service.BaseService;
import com.kush.utils.id.Identifier;

public class QuestionnaireService extends BaseService {

    public Question createQuestion(String content) {
        Identifier currentUserId = getCurrentUser().getId();
        QuestionPersistor questionPersistor = getInstance(QuestionPersistor.class);
        return questionPersistor.createQuestion(content, currentUserId);
    }

    public Option addOption(Identifier questionId, String content) {
        Identifier currentUserId = getCurrentUser().getId();
        OptionPersistor optionPersistor = getInstance(OptionPersistor.class);
        return optionPersistor.addOption(questionId, content, currentUserId);
    }

    public List<Question> getAllQuestions() {
        return null;
    }

    public Question getQuestion(Identifier questionId) {
        return null;
    }

    public List<Option> getOptions(Identifier questionId) {
        return null;
    }
}
