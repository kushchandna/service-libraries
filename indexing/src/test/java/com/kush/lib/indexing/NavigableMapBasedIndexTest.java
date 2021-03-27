package com.kush.lib.indexing;

import static com.kush.utils.testhelpers.SampleObjectUtils.obj;
import static com.kush.utils.testhelpers.SampleObjectsTestRepository.addObjects;
import static java.util.Comparator.naturalOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.Comparator;
import java.util.function.Function;

import org.junit.Rule;
import org.junit.Test;

import com.kush.lib.collections.iterables.IterableResult;
import com.kush.lib.collections.ranges.Range;
import com.kush.lib.collections.ranges.RangeSets;
import com.kush.lib.indexing.factory.SortedKeyBasedIndex;
import com.kush.utils.testhelpers.SampleObject;

public class NavigableMapBasedIndexTest {

    private static final Comparator<String> COMPARATOR = naturalOrder();

    @Rule
    public IndexableSampleObjectsTestRepository repository = new IndexableSampleObjectsTestRepository();

    @Test
    public void getMatchesForKey() throws Exception {
        SortedKeyBasedIndex<String, SampleObject> index = createIndex(SampleObject::getName);
        repository.setUpdateHandler(index);

        addObjects(repository,
                obj("id1", "name1"),
                obj("id2", "name2"),
                obj("id3", "name3"),
                obj("id4", "name4"),
                obj("id5", "name5"));

        IterableResult<SampleObject> result = index.getMatches(RangeSets.fromValues("name2"));
        assertThat(result.asList(), containsInAnyOrder(
                obj("id2")));
    }

    @Test
    public void getMatchesForKeys() throws Exception {
        SortedKeyBasedIndex<String, SampleObject> index = createIndex(SampleObject::getName);
        repository.setUpdateHandler(index);

        addObjects(repository,
                obj("id1", "name1"),
                obj("id2", "name2"),
                obj("id3", "name3"),
                obj("id4", "name4"),
                obj("id5", "name5"));

        IterableResult<SampleObject> result = index.getMatches(RangeSets.fromValues("name2", "name4"));
        assertThat(result.asList(), containsInAnyOrder(
                obj("id2"),
                obj("id4")));
    }

    @Test
    public void getMatchesForRange() throws Exception {
        SortedKeyBasedIndex<String, SampleObject> index = createIndex(SampleObject::getName);
        repository.setUpdateHandler(index);

        addObjects(repository,
                obj("id1", "name1"),
                obj("id2", "name2"),
                obj("id3", "name3"),
                obj("id4", "name4"),
                obj("id5", "name5"));

        IterableResult<SampleObject> result = index.getMatches(RangeSets.fromRanges(Range.<String>builder()
            .startingFrom("name2", true)
            .endingAt("name5", false)
            .build()));
        assertThat(result.asList(), containsInAnyOrder(
                obj("id2"),
                obj("id3"),
                obj("id4")));
    }

    private SortedKeyBasedIndex<String, SampleObject> createIndex(Function<SampleObject, String> keyGetter) {
        return new SortedKeyBasedIndex<>(COMPARATOR, keyGetter);
    }
}
