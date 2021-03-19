package com.kush.lib.collections.trees;

import java.util.Collection;
import java.util.Map;

public interface Tree<K, V> {

    void put(K key, V value);

    V get(K key);

    V remove(K key);

    Collection<K> keys();

    Collection<V> values();

    Tree<K, V> getSubTree(K key);

    Map<K, V> asMap();
}