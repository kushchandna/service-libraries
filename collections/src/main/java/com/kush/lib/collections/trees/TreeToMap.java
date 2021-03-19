package com.kush.lib.collections.trees;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Set;

class TreeToMap<K, V> extends AbstractMap<K, V> {

    private final Set<Entry<K, V>> entries;

    public TreeToMap(Tree<K, V> tree) {
        entries = new HashSet<>();
        for (K key : tree.keys()) {
            entries.add(new MapEntry<>(key, tree.get(key)));
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entries;
    }

    private static class MapEntry<K, V> implements Entry<K, V> {

        private final K key;
        private V value;

        public MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = value;
            this.value = value;
            return oldValue;
        }
    }
}
