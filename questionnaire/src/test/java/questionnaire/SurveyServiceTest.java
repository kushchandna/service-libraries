package questionnaire;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.kush.service.BaseServiceTest;

public class SurveyServiceTest extends BaseServiceTest {

    private SurveyService surveyService;

    @Before
    public void beforeEachTest() throws Exception {
        surveyService = registerService(SurveyService.class);
    }

    @Test
    public void e2e() throws Exception {
        runAuthenticatedOperation(() -> {
            Survey survey = surveyService.createSurvey();

            Question ques1 = new QuestionBuilder()
                .withContent("Choose yes or no")
                .withOption(1, "Yes")
                .withOption(2, "No")
                .build();
            SurveyQuestion surveyQues1 = surveyService.addQuestion(survey.getId(), ques1);

            Question ques2 = new QuestionBuilder()
                .withContent("Give Percentage Preference for cities")
                .withOption(1, "Delhi")
                .withOption(2, "London")
                .withOption(3, "New York")
                .build();
            SurveyQuestion surveyQues2 = surveyService.addQuestion(survey.getId(), ques2);

            Question ques3 = new QuestionBuilder()
                .withContent("What is your name?")
                .build();
            SurveyQuestion surveyQues3 = surveyService.addQuestion(survey.getId(), ques3);

            List<SurveyQuestion> questions = surveyService.getQuestions();
            assertThat(questions.get(0), is(equalTo(surveyQues1)));
            assertThat(questions.get(1), is(equalTo(surveyQues2)));
            assertThat(questions.get(2), is(equalTo(surveyQues3)));
        });
    }
}
