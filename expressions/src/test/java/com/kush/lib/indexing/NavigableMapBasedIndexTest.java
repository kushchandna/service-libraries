package com.kush.lib.indexing;

import static com.kush.utils.testhelpers.SampleObjectUtils.obj;
import static com.kush.utils.testhelpers.SampleObjectsTestRepository.addObjects;
import static java.util.Arrays.asList;
import static java.util.Comparator.naturalOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.function.Function;

import org.junit.Rule;
import org.junit.Test;

import com.kush.utils.commons.IterableResult;
import com.kush.utils.commons.Range;
import com.kush.utils.testhelpers.SampleObject;

public class NavigableMapBasedIndexTest {

    @Rule
    public IndexableSampleObjectsTestRepository repository = new IndexableSampleObjectsTestRepository();

    @Test
    public void getMatchesForKey() throws Exception {
        NavigableMapBasedIndex<String, SampleObject> index = createIndex(SampleObject::getName);
        repository.setUpdateHandler(index);

        addObjects(repository,
                obj("id1", "name1"),
                obj("id2", "name2"),
                obj("id3", "name3"),
                obj("id4", "name4"),
                obj("id5", "name5"));

        IterableResult<SampleObject> result = index.getMatchesForKey("name2");
        assertThat(result.asList(), containsInAnyOrder(
                obj("id2")));
    }

    @Test
    public void getMatchesForKeys() throws Exception {
        NavigableMapBasedIndex<String, SampleObject> index = createIndex(SampleObject::getName);
        repository.setUpdateHandler(index);

        addObjects(repository,
                obj("id1", "name1"),
                obj("id2", "name2"),
                obj("id3", "name3"),
                obj("id4", "name4"),
                obj("id5", "name5"));

        IterableResult<SampleObject> result = index.getMatchesForKeys(asList("name2", "name4"));
        assertThat(result.asList(), containsInAnyOrder(
                obj("id2"),
                obj("id4")));
    }

    @Test
    public void getMatchesForRange() throws Exception {
        NavigableMapBasedIndex<String, SampleObject> index = createIndex(SampleObject::getName);
        repository.setUpdateHandler(index);

        addObjects(repository,
                obj("id1", "name1"),
                obj("id2", "name2"),
                obj("id3", "name3"),
                obj("id4", "name4"),
                obj("id5", "name5"));

        IterableResult<SampleObject> result = index.getMatchesForRange(Range.<String>builder()
            .startingFrom("name2", true)
            .endingAt("name5", false)
            .build());
        assertThat(result.asList(), containsInAnyOrder(
                obj("id2"),
                obj("id3"),
                obj("id4")));
    }

    private NavigableMapBasedIndex<String, SampleObject> createIndex(Function<SampleObject, String> keyGetter) {
        return new NavigableMapBasedIndex<>(naturalOrder(), keyGetter);
    }
}
