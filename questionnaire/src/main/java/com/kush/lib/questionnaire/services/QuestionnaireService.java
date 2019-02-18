package com.kush.lib.questionnaire.services;

import java.util.List;

import com.kush.lib.questionnaire.core.Option;
import com.kush.lib.questionnaire.core.Question;
import com.kush.service.BaseService;
import com.kush.utils.id.Identifier;

public class QuestionnaireService extends BaseService {

    public Question createQuestion(String content) {
        return null;
    }

    public Option addOption(Identifier questionId, String content) {
        return null;
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
