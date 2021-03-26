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
public class RangeSetUnionTest {

    @Parameters
    public static Object[][] getTestScenarios() {
        return new Object[][] {
                assertThatUnionOf(ranges("[30 - 40]"), with("[10 - 20]"), is("[10 - 20]", "[30 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[20 - 30)"), is("[20 - 40]")),
                assertThatUnionOf(ranges("(30 - 40]"), with("[20 - 30]"), is("[20 - 30]", "(30 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[20 - 30]"), is("[20 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[25 - 35]"), is("[25 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[25 - 35)"), is("[25 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[30 - 30]"), is("[30 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[30 - 40]"), is("[30 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[30 - 35]"), is("[30 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[32 - 37]"), is("[30 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[35 - 40]"), is("[30 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[35 - 45]"), is("[30 - 45]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("(35 - 45]"), is("[30 - 45]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[40 - 40]"), is("[30 - 40]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[40 - 50]"), is("[30 - 50]")),

                assertThatUnionOf(ranges("[30 - 40]"), with("(40 - 50]"), is("[30 - 40]", "(40 - 50]")),
                assertThatUnionOf(ranges("[30 - 40)"), with("(40 - 50]"), is("[30 - 40)", "(40 - 50]")),
                assertThatUnionOf(ranges("[30 - 40)"), with("[40 - 50]"), is("[30 - 40)", "[40 - 50]")),
                assertThatUnionOf(ranges("[30 - 40]"), with("[50 - 60]"), is("[30 - 40]", "[50 - 60]")),

                assertThatUnionOf(ranges("[30 - 40]", "[70 - 80]"), with("[50 - 60]"), is("[30 - 40]", "[50 - 60]", "[70 - 80]")),
                assertThatUnionOf(ranges("[30 - 40]", "[70 - 80]"), with("[35 - 40]"), is("[30 - 40]", "[70 - 80]")),
                assertThatUnionOf(ranges("[30 - 40]", "[70 - 80]"), with("[35 - 75]"), is("[30 - 80]")),

                assertThatUnionOf(ranges("[30 - 40]"), with("(* - *)"), is("(* - *)")),
                assertThatUnionOf(ranges("(* - *)"), with("[30 - 40]"), is("(* - *)")),
                assertThatUnionOf(ranges("(* - 30)"), with("[30 - 40]"), is("(* - 30)", "[30 - 40]")),
                assertThatUnionOf(ranges("(* - 30]"), with("[30 - 40]"), is("(* - 40]")),
                assertThatUnionOf(ranges("(* - 35)"), with("[30 - 40]"), is("(* - 40]")),
                assertThatUnionOf(ranges("(30 - *)"), with("[30 - 40]"), is("[30 - *)")),
        };
    }

    private final String[] initialRangeSet;
    private final String[] unioningRangeSet;
    private final String[] expectedRangeSet;

    public RangeSetUnionTest(String[] initialRangeSet, String[] unioningRangeSet, String[] expectedRangeSet) {
        this.initialRangeSet = initialRangeSet;
        this.unioningRangeSet = unioningRangeSet;
        this.expectedRangeSet = expectedRangeSet;
    }

    @Test
    public void runTest() throws Exception {
        RangeSet<Integer> initial = intRanges(initialRangeSet);
        RangeSet<Integer> unioning = intRanges(unioningRangeSet);
        RangeSet<Integer> expectedUnion = intRanges(expectedRangeSet);
        RangeSet<Integer> actualUnion = initial.union(unioning);
        String failureMessage = format("Union of %s with %s was expected to be %s, but got %s",
                asString(initial), asString(unioning), asString(expectedUnion), asString(actualUnion));
        assertThat(failureMessage,
                actualUnion,
                Matchers.is(Matchers.equalTo(expectedUnion)));
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

    private static Object[] assertThatUnionOf(String[] initialRangeSet, String[] unioningRangeSet,
            String[] expectedRangeSet) {
        return new Object[] { initialRangeSet, unioningRangeSet, expectedRangeSet };
    }
}
