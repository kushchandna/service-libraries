package com.kush.lib.questionnaire.services;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.kush.service.BaseServiceTest;

public class QuestionnaireServiceTest extends BaseServiceTest {

    private QuestionnaireService questionnaireService;

    @Before
    public void beforeEachTest() throws Exception {
        questionnaireService = registerService(QuestionnaireService.class);
        beginSession(user(0));
    }

    @After
    public void afterEachTest() throws Exception {
        endSession();
    }

    @Test
    public void e2e() throws Exception {
        Questionnaire questionnaire = questionnaireService.createQuestionnaire();

        Question ques1 = questionnaireService.addQuestion(questionnaire.getId(), "Give rating to cities out of 5");
        Option optDelhi = questionnaireService.addOption(ques1.getId(), "Delhi");
        Option optLondon = questionnaireService.addOption(ques1.getId(), "London");
        Option optParis = questionnaireService.addOption(ques1.getId(), "Paris");
        Option optNY = questionnaireService.addOption(ques1.getId(), "New York");

        List<Question> questions = questionnaireService.getQuestions();
        assertThat(questions, hasSize(1));
    }
}
