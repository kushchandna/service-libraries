package com.kush.lib.questionnaire;

import java.util.List;

import com.kush.service.BaseService;
import com.kush.utils.id.Identifier;

public class PreferenceQuestionService extends BaseService {

    public PreferenceQuestion createQuestion(String content) {
        return null;
    }

    public PreferenceOption addOption(String content) {
        return null;
    }

    public PreferenceAnswer addAnswer(Identifier questionId, Identifier optionId, Preference preference) {
        return null;
    }

    public List<PreferenceQuestion> getAllQuestions() {
        return null;
    }

    public PreferenceQuestion getQuestion(Identifier questionId) {
        return null;
    }

    public List<PreferenceAnswer> getAnswers(Identifier questionId) {
        return null;
    }
}
