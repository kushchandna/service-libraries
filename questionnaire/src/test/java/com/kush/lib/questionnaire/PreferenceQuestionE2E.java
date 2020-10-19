package com.kush.lib.questionnaire;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kush.lib.persistence.api.Persister;
import com.kush.lib.persistence.helpers.InMemoryPersister;
import com.kush.service.BaseServiceTest;

public class PreferenceQuestionE2E extends BaseServiceTest {

    private PreferenceQuestionService questionService;

    @Before
    public void beforeEachTest() throws Exception {
        Persister<PreferenceQuestion> delegatePrefQuestPersistor = InMemoryPersister.forType(PreferenceQuestion.class);
        Persister<PreferenceOption> optionPersistor = InMemoryPersister.forType(PreferenceOption.class);
        Persister<PreferenceAnswer> answerPersistor = InMemoryPersister.forType(PreferenceAnswer.class);
        addToContext(PreferenceQuestionPersister.class,
                new DefaultPreferenceQuestionPersistor(delegatePrefQuestPersistor, optionPersistor, answerPersistor));
        questionService = registerService(PreferenceQuestionService.class);
    }

    @Test
    public void e2e() throws Exception {
        runAuthenticatedOperation(user(0), () -> {
            PreferenceQuestion question = questionService.createQuestion("Preferred places?");
            PreferenceOption option = questionService.addOption(question.getId(), "Delhi");
            questionService.addAnswer(question.getId(), option.getId(), Preference.PREFERRED);
        });

        runAuthenticatedOperation(user(1), () -> {
            PreferenceQuestion question = getQuestion();
            List<PreferenceOption> options = questionService.getOptions(question.getId());
            PreferenceOption delhiOption = options.get(0);
            questionService.addAnswer(question.getId(), delhiOption.getId(), Preference.PREFERRED);

            PreferenceOption londonOption = questionService.addOption(question.getId(), "London");
            questionService.addAnswer(question.getId(), londonOption.getId(), Preference.PREFERRED);
        });

        runAuthenticatedOperation(user(2), () -> {
            PreferenceQuestion question = getQuestion();
            List<PreferenceOption> options = questionService.getOptions(question.getId());
            PreferenceOption delhiOption = options.get(0);
            PreferenceOption londonOption = options.get(1);
            questionService.addAnswer(question.getId(), delhiOption.getId(), Preference.REJECTED);
            questionService.addAnswer(question.getId(), londonOption.getId(), Preference.PREFERRED);
        });

        runAuthenticatedOperation(user(0), () -> {
            PreferenceQuestion onlyQuestion = getQuestion();

            PreferenceQuestion question = questionService.getQuestion(onlyQuestion.getId());
            assertThat(question.getContent(), is(equalTo("Preferred places?")));
            assertThat(question.getAddedBy(), is(equalTo(user(0).getId())));
            List<PreferenceOption> options = questionService.getOptions(question.getId());
            assertThat(options, hasSize(2));
            PreferenceOption delhiOption = options.get(0);
            assertThat(delhiOption.getContent(), is(equalTo("Delhi")));
            assertThat(delhiOption.getAddedBy(), is(equalTo(user(0).getId())));
            PreferenceOption londonOption = options.get(1);
            assertThat(londonOption.getContent(), is(equalTo("London")));
            assertThat(londonOption.getAddedBy(), is(equalTo(user(1).getId())));

            List<PreferenceAnswer> answers = questionService.getAnswers(question.getId());
            assertThat(answers, hasSize(5));

            PreferenceAnswer answer1 = answers.get(0);
            assertThat(answer1.getContent(), is(equalTo("Delhi")));
            assertThat(answer1.getOption().getContent(), is(equalTo("Delhi")));
            assertThat(answer1.getAddedBy(), is(equalTo(user(0).getId())));
            assertThat(answer1.getPreference(), is(equalTo(Preference.PREFERRED)));

            PreferenceAnswer answer2 = answers.get(1);
            assertThat(answer2.getContent(), is(equalTo("Delhi")));
            assertThat(answer2.getOption().getContent(), is(equalTo("Delhi")));
            assertThat(answer2.getAddedBy(), is(equalTo(user(1).getId())));
            assertThat(answer2.getPreference(), is(equalTo(Preference.PREFERRED)));

            PreferenceAnswer answer3 = answers.get(2);
            assertThat(answer3.getContent(), is(equalTo("London")));
            assertThat(answer3.getOption().getContent(), is(equalTo("London")));
            assertThat(answer3.getAddedBy(), is(equalTo(user(1).getId())));
            assertThat(answer3.getPreference(), is(equalTo(Preference.PREFERRED)));

            PreferenceAnswer answer4 = answers.get(3);
            assertThat(answer4.getContent(), is(equalTo("Delhi")));
            assertThat(answer4.getOption().getContent(), is(equalTo("Delhi")));
            assertThat(answer4.getAddedBy(), is(equalTo(user(2).getId())));
            assertThat(answer4.getPreference(), is(equalTo(Preference.REJECTED)));

            PreferenceAnswer answer5 = answers.get(4);
            assertThat(answer5.getContent(), is(equalTo("London")));
            assertThat(answer5.getOption().getContent(), is(equalTo("London")));
            assertThat(answer5.getAddedBy(), is(equalTo(user(2).getId())));
            assertThat(answer5.getPreference(), is(equalTo(Preference.PREFERRED)));
        });
    }

    private PreferenceQuestion getQuestion() throws Exception {
        List<PreferenceQuestion> questions = questionService.getAllQuestions();
        return questions.get(0);
    }
}
