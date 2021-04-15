package com.kush.lib.indexing.management;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.IndexException;
import com.kush.lib.indexing.UpdateHandler;
import com.kush.lib.indexing.UpdateHandlersRegistrar;
import com.kush.lib.indexing.factory.IndexFactory;
import com.kush.lib.indexing.query.IndexOption;
import com.kush.lib.indexing.query.IndexQueryExecutor;
import com.kush.lib.indexing.ranges.IndexableRangeSetFinder;

public final class IndexManager<T> {

    private final Map<String, FieldsIndex> indexes = new LinkedHashMap<>();

    private final IndexFactory<T> indexFactory;
    private final UpdateHandlersRegistrar<T> registrar;
    private final IndexableRangeSetFinder<?> rangeSetFinder;

    public IndexManager(IndexFactory<T> indexFactory, UpdateHandlersRegistrar<T> registrar,
            IndexableRangeSetFinder<?> rangeSetFinder) {
        this.indexFactory = indexFactory;
        this.registrar = registrar;
        this.rangeSetFinder = rangeSetFinder;
    }

    public synchronized <K> void addIndex(String indexName, Object[] fields, IndexGenerator<K, T> indexGenerator)
            throws IndexException {
        checkNoIndexWithSameNameExist(indexName);
        Index<K, T> index = indexGenerator.generate(indexFactory);
        indexes.put(indexName, new FieldsIndex(fields, index));
        registrar.register(asUpdateHandler(index));
    }

    public synchronized void dropIndex(String indexName) throws IndexException {
        FieldsIndex fieldsIndex = indexes.remove(indexName);
        if (fieldsIndex == null) {
            throw new IndexException();
        }
        Index<?, T> index = fieldsIndex.getIndex();
        registrar.unregister(asUpdateHandler(index));
    }

    public synchronized IndexQueryExecutor<T> getQueryExecutor() {
        return (query, policy) -> policy.execute(query, getOptions(), rangeSetFinder);
    }

    private Iterator<IndexOption<T>> getOptions() {
        Iterator<Map.Entry<String, FieldsIndex>> indexesIterator = indexes.entrySet().iterator();
        return new Iterator<IndexOption<T>>() {

            @Override
            public boolean hasNext() {
                return indexesIterator.hasNext();
            }

            @Override
            public IndexOption<T> next() {
                Entry<String, FieldsIndex> entry = indexesIterator.next();
                return new FieldIndexOption(entry);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private UpdateHandler<T> asUpdateHandler(Index<?, T> index) {
        return (UpdateHandler<T>) index;
    }

    private void checkNoIndexWithSameNameExist(String indexName) throws IndexException {
        if (indexes.containsKey(indexName)) {
            throw new IndexException();
        }
    }

    private final class FieldIndexOption implements IndexOption<T> {

        private final Entry<String, FieldsIndex> entry;

        private FieldIndexOption(Entry<String, FieldsIndex> entry) {
            this.entry = entry;
        }

        @Override
        public Object[] getIndexedFields() {
            return entry.getValue().getFields();
        }

        @Override
        public String getIndexName() {
            return entry.getKey();
        }

        @Override
        public Index<?, T> getIndex() {
            return entry.getValue().getIndex();
        }
    }

    private class FieldsIndex {

        private final Object[] fields;
        private final Index<?, T> index;

        public FieldsIndex(Object[] fields, Index<?, T> index) {
            this.fields = fields;
            this.index = index;
        }

        public Object[] getFields() {
            return fields;
        }

        public Index<?, T> getIndex() {
            return index;
        }
    }
}
