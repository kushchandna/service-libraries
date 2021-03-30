package com.kush.lib.indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.kush.lib.indexing.factory.IndexFactory;
import com.kush.lib.indexing.query.IndexQueryExecutor;

public final class Indexes<T> {

    private final Map<String, Index<Object, T>> indexes = new HashMap<>();
    private final Map<Object[], List<String>> fieldsVsIndexNames = new HashMap<>();
    private final Map<String, Object[]> indexNameVsFields = new HashMap<>();

    private final IndexFactory<T> indexFactory;
    private final UpdateHandlersRegistrar<T> registrar;

    public Indexes(IndexFactory<T> indexFactory, UpdateHandlersRegistrar<T> registrar) {
        this.indexFactory = indexFactory;
        this.registrar = registrar;
    }

    public synchronized <K> void addIndex(String indexName, Object[] fields, IndexGenerator<T> indexGenerator)
            throws IndexException {
        checkNoIndexWithSameNameExist(indexName);
        Index<Object, T> index = indexGenerator.generate(indexFactory);

        List<String> indexNames = fieldsVsIndexNames.get(fields);
        if (indexNames == null) {
            indexNames = new ArrayList<>();
            fieldsVsIndexNames.put(fields, indexNames);
        }
        checkNoIndexWithSameTypeExistForSameField(index, indexNames);

        registrar.register(asUpdateHandler(index));
        indexNames.add(indexName);
        indexes.put(indexName, index);
        indexNameVsFields.put(indexName, fields);
    }

    public IndexQueryExecutor<T> getQueryExecutor() {
        return (query, policy) -> policy.execute(query, getOptions());
    }

    private Iterator<IndexOption<T>> getOptions() {

        Iterator<Map.Entry<String, Index<Object, T>>> indexesIterator = indexes.entrySet().iterator();

        return new Iterator<IndexOption<T>>() {

            @Override
            public boolean hasNext() {
                return indexesIterator.hasNext();
            }

            @Override
            public IndexOption<T> next() {
                Entry<String, Index<Object, T>> entry = indexesIterator.next();
                return new IndexOption<T>() {

                    @Override
                    public Object[] getIndexedFields() {
                        return indexNameVsFields.get(entry.getKey());
                    }

                    @Override
                    public String getIndexName() {
                        return entry.getKey();
                    }

                    @Override
                    public Index<Object, T> getIndex() {
                        return entry.getValue();
                    }
                };
            }
        };
    }

    @SuppressWarnings("unchecked")
    private UpdateHandler<T> asUpdateHandler(Index<?, T> index) {
        return (UpdateHandler<T>) index;
    }

    private void checkNoIndexWithSameTypeExistForSameField(Index<Object, T> index, List<String> indexNames)
            throws IndexException {
        for (String existingIndexName : indexNames) {
            Index<Object, T> existingIndex = indexes.get(existingIndexName);
            if (existingIndex.getClass().equals(index.getClass())) {
                throw new IndexException();
            }
        }
    }

    private void checkNoIndexWithSameNameExist(String indexName) throws IndexException {
        if (indexes.containsKey(indexName)) {
            throw new IndexException();
        }
    }

    public interface IndexOption<T> {

        String getIndexName();

        Object[] getIndexedFields();

        Index<Object, T> getIndex();
    }

    public interface IndexGenerator<T> {

        <I extends Index<?, T> & UpdateHandler<T>> I generate(IndexFactory<T> indexFactory);
    }
}
