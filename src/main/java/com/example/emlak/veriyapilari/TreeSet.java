package com.example.emlak.veriyapilari;

import java.util.Comparator;

public class TreeSet<T> {
    private static class Node<T> {
        T data;
        Node<T> left, right;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> root;
    private final Comparator<T> comparator;
    private int size;

    public TreeSet(Comparator<T> comparator) {
        this.comparator = comparator;
        this.size = 0;
    }

    public void add(T element) {
        if (root == null) {
            root = new Node<>(element);
            size++;
        } else {
            if (addRecursive(root, element)) {
                size++;
            }
        }
    }

    private boolean addRecursive(Node<T> current, T element) {
        int cmp = comparator.compare(element, current.data);
        if (cmp == 0) {
            return false;
        }
        if (cmp < 0) {
            if (current.left == null) {
                current.left = new Node<>(element);
                return true;
            }
            return addRecursive(current.left, element);
        } else {
            if (current.right == null) {
                current.right = new Node<>(element);
                return true;
            }
            return addRecursive(current.right, element);
        }
    }

    public boolean remove(T element) {
        int originalSize = size;
        root = removeRecursive(root, element);
        return size < originalSize;
    }

    private Node<T> removeRecursive(Node<T> current, T element) {
        if (current == null) {
            return null;
        }
        int cmp = comparator.compare(element, current.data);
        if (cmp == 0) {
            size--;
            if (current.left == null) {
                return current.right;
            } else if (current.right == null) {
                return current.left;
            } else {
                Node<T> minNode = findMin(current.right);
                current.data = minNode.data;
                current.right = removeRecursive(current.right, minNode.data);
                return current;
            }
        }
        if (cmp < 0) {
            current.left = removeRecursive(current.left, element);
        } else {
            current.right = removeRecursive(current.right, element);
        }
        return current;
    }

    private Node<T> findMin(Node<T> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public Liste<T> toList() {
        Liste<T> list = new Liste<>();
        inOrderTraversal(root, list);
        return list;
    }

    private void inOrderTraversal(Node<T> node, Liste<T> list) {
        if (node != null) {
            inOrderTraversal(node.left, list);
            list.add(node.data);
            inOrderTraversal(node.right, list);
        }
    }

    public int size() {
        return size;
    }
}