package com.kush.lib.questionnaire.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.persistence.api.Persistor;
import com.kush.lib.persistence.helpers.InMemoryPersistor;
import com.kush.lib.questionnaire.core.Option;
import com.kush.lib.questionnaire.core.Question;
import com.kush.lib.questionnaire.persistance.DefaultOptionPersistor;
import com.kush.lib.questionnaire.persistance.DefaultQuestionPersistor;
import com.kush.lib.questionnaire.persistance.OptionPersistor;
import com.kush.lib.questionnaire.persistance.QuestionPersistor;
import com.kush.service.BaseServiceTest;

public class QuestionnaireServiceTest extends BaseServiceTest {

    private QuestionnaireService questionnaireService;

    @Before
    public void beforeEachTest() throws Exception {
        Persistor<Question> delegateQuestPersistor = InMemoryPersistor.forType(Question.class);
        addToContext(QuestionPersistor.class, new DefaultQuestionPersistor(delegateQuestPersistor));
        Persistor<Option> delegateOptPersistor = InMemoryPersistor.forType(Option.class);
        addToContext(OptionPersistor.class, new DefaultOptionPersistor(delegateOptPersistor));
        questionnaireService = registerService(QuestionnaireService.class);
    }

    @Test
    public void e2e() throws Exception {
        runAuthenticatedOperation(user(0), () -> {
            Question question = questionnaireService.createQuestion("Rate places from 0 to 5");
            questionnaireService.addOption(question.getId(), "Delhi");
            questionnaireService.addOption(question.getId(), "London");
        });

        runAuthenticatedOperation(user(1), () -> {
            List<Question> allQuestions = questionnaireService.getAllQuestions();
            assertThat(allQuestions, hasSize(1));
            Question question = questionnaireService.getQuestion(allQuestions.get(0).getId());
            assertThat(question.getAddedBy(), is(equalTo(user(0).getId())));

            questionnaireService.addOption(question.getId(), "Paris");

            List<Option> options = questionnaireService.getOptions(question.getId());
            assertThat(options, hasSize(3));
            assertThat(options.get(0).getAddedBy(), is(equalTo(user(0).getId())));
            assertThat(options.get(1).getAddedBy(), is(equalTo(user(0).getId())));
            assertThat(options.get(2).getAddedBy(), is(equalTo(user(1).getId())));
        });
    }
}
