package com.kush.lib.survey.services;

import static java.util.Arrays.asList;

import org.junit.Before;
import org.junit.Test;

import com.kush.service.BaseServiceTest;

public class SurveyServiceTest extends BaseServiceTest {

    private SurveyService surveyService;

    private Survey survey;

    @Before
    public void setup() throws Exception {
        surveyService = registerService(SurveyService.class);
        survey = surveyService.createSurvey();
        beginSession(user(0));
    }

    @Test
    public void teardown() throws Exception {
        endSession();
    }

    @Test
    public void createYesNoSurveyQuestion() throws Exception {
        Choice choiceYes = new Choice("Yes");
        Choice choiceNo = new Choice("No");
        ChoiceSelectionPolicy policy = new YesNoSelectionPolicy();
        Question question = new MultipleChoiceQuestion("Choose one of yes or no.", asList(choiceYes, choiceNo), policy);
        surveyService.addQuestion(survey.getId(), question);
    }

    @Test
    public void createPercentChancesSurveyQuestion() throws Exception {
        Choice choiceParis = new Choice("Paris");
        Choice choiceLondon = new Choice("London");
        Choice choiceTokyo = new Choice("Tokyo");
        ChoiceSelectionPolicy policy = new PercentageSelectionPolicy();
        Question question = new MultipleChoiceQuestion("Give percent of chances for next destination.",
                asList(choiceParis, choiceLondon, choiceTokyo), policy);
        Question savedQuestion = surveyService.addQuestion(survey.getId(), question);

    }
}
