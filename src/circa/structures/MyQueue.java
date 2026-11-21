package circa.structures;

import java.util.Iterator;

public class MyQueue<T> implements Iterable<T> {
    private static class Node<T> {
        private final T data;
        private Node<T> next;

        private Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void enqueue(T item) {
        Node<T> node = new Node<>(item);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
    }

    public void addFirst(T item) {
        Node<T> node = new Node<>(item);
        node.next = head;
        head = node;
        if (tail == null) {
            tail = node;
        }
        size++;
    }

    public T dequeue() {
        if (head == null) {
            return null;
        }
        T data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return data;
    }

    public T peek() {
        return head == null ? null : head.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public boolean contains(T item) {
        return positionOf(item) > 0;
    }

    public int positionOf(T item) {
        int position = 0;
        for (Node<T> n = head; n != null; n = n.next) {
            position++;
            if (n.data.equals(item)) {
                return position;
            }
        }
        return 0;
    }

    public boolean remove(T item) {
        Node<T> previous = null;
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(item)) {
                if (previous == null) {
                    head = current.next;
                } else {
                    previous.next = current.next;
                }
                if (current == tail) {
                    tail = previous;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }
}
