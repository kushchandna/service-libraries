package com.kush.lib.questionnaire.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.questionnaire.common.Question;
import com.kush.lib.questionnaire.common.QuestionBuilder;
import com.kush.lib.questionnaire.common.Questionnaire;
import com.kush.lib.questionnaire.common.QuestionnaireQuestion;
import com.kush.lib.questionnaire.services.QuestionnaireService;
import com.kush.service.BaseServiceTest;

public class QuestionnaireServiceTest extends BaseServiceTest {

    private QuestionnaireService questionnaireService;

    @Before
    public void beforeEachTest() throws Exception {
        questionnaireService = registerService(QuestionnaireService.class);
    }

    @Test
    public void e2e() throws Exception {
        runAuthenticatedOperation(() -> {
            Questionnaire questionnaire = questionnaireService.createQuestionnaire();

            Question ques1 = new QuestionBuilder()
                .withContent("Choose yes or no")
                .withOption("Yes")
                .withOption("No")
                .build();
            QuestionnaireQuestion questionnaireQues1 = questionnaireService.addQuestion(questionnaire.getId(), ques1);

            Question ques2 = new QuestionBuilder()
                .withContent("Give Percentage Preference for cities")
                .withOption("Delhi")
                .withOption("London")
                .withOption("New York")
                .build();
            QuestionnaireQuestion questionnaireQues2 = questionnaireService.addQuestion(questionnaire.getId(), ques2);

            Question ques3 = new QuestionBuilder()
                .withContent("What is your name?")
                .build();
            QuestionnaireQuestion questionnaireQues3 = questionnaireService.addQuestion(questionnaire.getId(), ques3);

            Question ques4 = new QuestionBuilder()
                .withContent("Enter names of five preffered cities?")
                .build();
            QuestionnaireQuestion questionnaireQues4 = questionnaireService.addQuestion(questionnaire.getId(), ques4);

            List<QuestionnaireQuestion> questions = questionnaireService.getQuestions();
            assertThat(questions.get(0), is(equalTo(questionnaireQues1)));
            assertThat(questions.get(1), is(equalTo(questionnaireQues2)));
            assertThat(questions.get(2), is(equalTo(questionnaireQues3)));
            assertThat(questions.get(3), is(equalTo(questionnaireQues4)));
        });
    }
}
