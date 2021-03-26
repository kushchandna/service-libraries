package com.kush.lib.collections.ranges;

import static com.kush.lib.collections.ranges.RangeSetTestUtils.asString;
import static com.kush.lib.collections.ranges.RangeSetTestUtils.intRanges;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RangeSetIntersectionTest {

    @Parameters
    public static Object[][] getTestScenarios() {
        return new Object[][] {
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[10 - 20]"), isEmpty()),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[20 - 30)"), isEmpty()),
                assertThatIntersectionOf(ranges("(30 - 40]"), with("[20 - 30]"), isEmpty()),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[20 - 30]"), is("[30 - 30]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[25 - 35]"), is("[30 - 35]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[25 - 35)"), is("[30 - 35)")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[30 - 30]"), is("[30 - 30]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[30 - 40]"), is("[30 - 40]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[30 - 35]"), is("[30 - 35]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[32 - 37]"), is("[32 - 37]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[35 - 40]"), is("[35 - 40]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[35 - 45]"), is("[35 - 40]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("(35 - 45]"), is("(35 - 40]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[40 - 40]"), is("[40 - 40]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[40 - 50]"), is("[40 - 40]")),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("(40 - 50]"), isEmpty()),
                assertThatIntersectionOf(ranges("[30 - 40)"), with("(40 - 50]"), isEmpty()),
                assertThatIntersectionOf(ranges("[30 - 40)"), with("[40 - 50]"), isEmpty()),
                assertThatIntersectionOf(ranges("[30 - 40]"), with("[50 - 60]"), isEmpty()),

                assertThatIntersectionOf(ranges("[30 - 40]", "[70 - 80]"), with("[50 - 60]"), isEmpty()),
                assertThatIntersectionOf(ranges("[30 - 40]", "[70 - 80]"), with("[35 - 40]"), is("[35 - 40]")),
                assertThatIntersectionOf(ranges("[30 - 40]", "[70 - 80]"), with("[35 - 75]"), is("[35 - 40]", "[70 - 75]")),

                assertThatIntersectionOf(ranges("[30 - 40]"), with("(* - *)"), is("[30 - 40]")),
                assertThatIntersectionOf(ranges("(* - *)"), with("[30 - 40]"), is("[30 - 40]")),
                assertThatIntersectionOf(ranges("(* - 30)"), with("[30 - 40]"), isEmpty()),
                assertThatIntersectionOf(ranges("(* - 30]"), with("[30 - 40]"), is("[30 - 30]")),
                assertThatIntersectionOf(ranges("(* - 35)"), with("[30 - 40]"), is("[30 - 35)")),
                assertThatIntersectionOf(ranges("(30 - *)"), with("[30 - 40]"), is("(30 - 40]")),
        };
    }

    private final String[] initialRangeSet;
    private final String[] intersectingRangeSet;
    private final String[] expectedRangeSet;

    public RangeSetIntersectionTest(String[] initialRangeSet, String[] intersectingRangeSet, String[] expectedRangeSet) {
        this.initialRangeSet = initialRangeSet;
        this.intersectingRangeSet = intersectingRangeSet;
        this.expectedRangeSet = expectedRangeSet;
    }

    @Test
    public void runTest() throws Exception {
        RangeSet<Integer> initial = intRanges(initialRangeSet);
        RangeSet<Integer> intersecting = intRanges(intersectingRangeSet);
        RangeSet<Integer> expectedIntersection = intRanges(expectedRangeSet);
        RangeSet<Integer> actualIntersection = initial.intersect(intersecting);
        String failureMessage = format("Intersection of %s with %s was expected to be %s, but got %s",
                asString(initial), asString(intersecting), asString(expectedIntersection), asString(actualIntersection));
        assertThat(failureMessage,
                actualIntersection,
                Matchers.is(Matchers.equalTo(expectedIntersection)));
    }

    private static String[] ranges(String... rangeTexts) {
        return rangeTexts;
    }

    private static String[] with(String... rangeTexts) {
        return rangeTexts;
    }

    private static String[] is(String... rangeTexts) {
        return rangeTexts;
    }

    private static String[] isEmpty() {
        return new String[0];
    }

    private static Object[] assertThatIntersectionOf(String[] initialRangeSet, String[] intersectingRangeSet,
            String[] expectedRangeSet) {
        return new Object[] { initialRangeSet, intersectingRangeSet, expectedRangeSet };
    }
}
