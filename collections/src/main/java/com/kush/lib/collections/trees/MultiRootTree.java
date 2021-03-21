package com.kush.lib.collections.trees;

import static java.util.Collections.unmodifiableCollection;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

class MultiRootTree<K, V> implements Tree<K, V> {

    private final PathSplitter<K> pathSplitter;
    private final Node<V> root;
    private final Set<K> keys = new HashSet<>();

    public MultiRootTree(PathSplitter<K> pathSplitter) {
        this.pathSplitter = pathSplitter;
        this.root = new Node<>();
    }

    @Override
    public void put(K key, V value) {
        Object[] paths = getPaths(key);
        Node<V> node = getOrAddBranch(paths);
        node.setValue(value);
        keys.add(key);
    }

    @Override
    public V get(K key) {
        Node<V> currentNode = getBranch(key);
        return currentNode == null ? null : currentNode.value;
    }

    @Override
    public V remove(K key) {
        Object[] paths = getPaths(key);
        Node<V> node = getBranch(paths);
        if (node == null) {
            return null;
        }
        Node<V> parent = node.parent;
        return parent.removeChild(paths[paths.length - 1]);
    }

    @Override
    public Tree<K, V> getSubTree(K key) {
        Object[] paths = getPaths(key);
        Node<V> node = getBranch(paths);
        if (node == null) {
            return null;
        }
        MultiRootTree<K, V> subTree = new MultiRootTree<>(pathSplitter);
        subTree.root.addChildNode(paths[paths.length - 1], node);
        return subTree;
    }

    @Override
    public Map<K, V> asMap() {
        return new TreeToMap<>(this);
    }

    @Override
    public Collection<K> keys() {
        return unmodifiableCollection(keys);
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        addVChildValues(root, values);
        return values;
    }

    private Node<V> getBranch(K key) {
        Object[] paths = getPaths(key);
        return getBranch(paths);
    }

    private void addVChildValues(Node<V> node, List<V> values) {
        node.children.forEach((path, child) -> {
            values.add(child.value);
            addVChildValues(child, values);
        });
    }

    private Node<V> getBranch(Object[] paths) {
        Node<V> currentNode = root;
        for (int i = 0; i < paths.length; i++) {
            currentNode = currentNode.children.get(paths[i]);
            if (currentNode == null) {
                break;
            }
        }
        return currentNode;
    }

    private Node<V> getOrAddBranch(Object[] paths) {
        Node<V> lastParent = null;
        Node<V> currentNode = root;
        for (int i = 0; i < paths.length; i++) {
            Object path = paths[i];
            lastParent = currentNode;
            currentNode = currentNode.children.get(path);
            if (currentNode == null) {
                currentNode = addNode(lastParent, path);
            }
        }
        return currentNode;
    }

    private Node<V> addNode(Node<V> parent, Object path) {
        return parent.addChild(path);
    }

    private Object[] getPaths(K key) {
        Object[] path = splitPath(key);
        checkPathIsValid(path);
        return path;
    }

    private void checkPathIsValid(Object[] paths) {
        requireNonNull(paths, "path can not be null");
        if (paths.length == 0) {
            throw new NullPointerException();
        }
    }

    private Object[] splitPath(K key) {
        return pathSplitter.split(key);
    }

    private static class Node<V> {

        private final Map<Object, Node<V>> children = new HashMap<>();
        private Node<V> parent;
        private V value;

        public void setValue(V value) {
            this.value = value;
        }

        public Node<V> addChild(Object path) {
            Node<V> node = new Node<>();
            return addChildNode(path, node);
        }

        private Node<V> addChildNode(Object path, Node<V> node) {
            node.parent = this;
            children.put(path, node);
            return node;
        }

        public V removeChild(Object path) {
            Node<V> removedNode = children.remove(path);
            return removedNode == null ? null : removedNode.value;
        }
    }

    public interface PathSplitter<K> {

        Object[] split(K key);
    }
}
