package com.kush.lib.indexing.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kush.lib.indexing.Index;
import com.kush.lib.indexing.IndexException;
import com.kush.lib.indexing.UpdateHandlersRegistrar;

public class Indexes<T> {

    private final Map<String, Index<?, T>> indexes = new HashMap<>();
    private final Map<Object, List<String>> fieldsVsIndexNames = new HashMap<>();
    private final Map<String, Object> indexNameVsFields = new HashMap<>();

    private final IndexFactory<T> indexFactory;
    private final UpdateHandlersRegistrar<T> registrar;

    public Indexes(IndexFactory<T> indexFactory, UpdateHandlersRegistrar<T> registrar) {
        this.indexFactory = indexFactory;
        this.registrar = registrar;
    }

    public synchronized void addIndex(String indexName, Object fields, IndexGenerator<T> indexGenerator) throws IndexException {
        checkNoIndexWithSameNameExist(indexName);
        BaseIndex<Object, T> index = indexGenerator.generate(indexFactory);

        List<String> indexNames = fieldsVsIndexNames.get(fields);
        if (indexNames == null) {
            indexNames = new ArrayList<>();
            fieldsVsIndexNames.put(fields, indexNames);
        }
        checkNoIndexWithSameTypeExistForSameField(index, indexNames);

        registrar.register(index);
        indexNames.add(indexName);
        indexes.put(indexName, index);
        indexNameVsFields.put(indexName, fields);
    }

    private void checkNoIndexWithSameTypeExistForSameField(BaseIndex<Object, T> index, List<String> indexNames)
            throws IndexException {
        for (String existingIndexName : indexNames) {
            Index<?, T> existingIndex = indexes.get(existingIndexName);
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

    interface IndexGenerator<T> {

        <K> BaseIndex<K, T> generate(IndexFactory<T> indexFactory);
    }
}
