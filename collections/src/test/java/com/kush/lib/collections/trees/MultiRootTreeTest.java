package com.kush.lib.collections.trees;

import org.junit.Test;

public class MultiRootTreeTest {

    @Test
    public void addAndGet() throws Exception {
        MultiRootTree<String, Integer> tree = new MultiRootTree<>(path -> path.split("/"));
        tree.put("Asia/India/Delhi", 1);
        tree.put("Asia/India/Mumbai", 5);
        tree.put("Asia/Pakistan/Karachi", 4);
        tree.put("Asia/Japan/Tokyp", 2);
        tree.put("Europe/Italy/Rome", 3);
        tree.put("Europe/Italy/Pisa", 7);
        tree.put("America/USA/New York", 6);

        Tree<String, Integer> subTree = tree.getSubTree("Asia/India");
        System.out.println(subTree);
    }
}
