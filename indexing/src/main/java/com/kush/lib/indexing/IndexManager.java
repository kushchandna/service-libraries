package com.kush.lib.indexing;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.checkerframework.checker.units.qual.K;

import com.kush.lib.indexing.composite.MultiKey;
import com.kush.lib.indexing.factory.IndexFactory;
import com.kush.lib.indexing.factory.IndexGenerator;
import com.kush.lib.indexing.query.IndexOption;
import com.kush.lib.indexing.query.IndexQueryExecutor;

public final class IndexManager<T> {

    private final Map<String, FieldsIndex> indexes = new LinkedHashMap<>();

    private final IndexFactory<T> indexFactory;
    private final UpdateHandlersRegistrar<T> registrar;
    private final MultiKey.Factory multiKeyFactory;

    public IndexManager(IndexFactory<T> indexFactory, UpdateHandlersRegistrar<T> registrar, MultiKey.Factory multiKeyFactory) {
        this.indexFactory = indexFactory;
        this.registrar = registrar;
        this.multiKeyFactory = multiKeyFactory;
    }

    public synchronized void addIndex(String indexName, Object[] fields, IndexGenerator<T> indexGenerator)
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
        return (query, policy) -> policy.execute(query, getOptions(), multiKeyFactory);
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
