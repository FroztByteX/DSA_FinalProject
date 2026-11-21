package circa.structures;

import java.util.Iterator;

public class MyLinkedList<T> implements Iterable<T> {
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

    public void add(T item) {
        Node<T> node = new Node<>(item);
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            tail = node;
        }
        size++;
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

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public T[] toArray(T[] target) {
        T[] result = target.length < size ? java.util.Arrays.copyOf(target, size) : target;
        int i = 0;
        for (Node<T> n = head; n != null; n = n.next) {
            result[i++] = n.data;
        }
        if (result.length > size) {
            result[size] = null;
        }
        return result;
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
