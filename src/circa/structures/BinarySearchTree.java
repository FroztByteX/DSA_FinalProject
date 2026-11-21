package circa.structures;

import java.util.Comparator;

public class BinarySearchTree<T> {
    private class Node {
        private final T value;
        private Node left;
        private Node right;

        private Node(T value) {
            this.value = value;
        }
    }

    private final Comparator<T> comparator;
    private Node root;

    public BinarySearchTree(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public void insert(T value) {
        root = insert(root, value);
    }

    private Node insert(Node node, T value) {
        if (node == null) {
            return new Node(value);
        }
        if (comparator.compare(value, node.value) < 0) {
            node.left = insert(node.left, value);
        } else {
            node.right = insert(node.right, value);
        }
        return node;
    }

    public void rebuild(Iterable<T> values) {
        root = null;
        for (T value : values) {
            insert(value);
        }
    }

    public MyLinkedList<T> inOrder() {
        MyLinkedList<T> result = new MyLinkedList<>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(Node node, MyLinkedList<T> result) {
        if (node == null) {
            return;
        }
        inOrder(node.left, result);
        result.add(node.value);
        inOrder(node.right, result);
    }
}
