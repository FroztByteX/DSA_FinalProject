package circa.structures;

import java.util.Iterator;

public class MyDoublyLinkedList<T> implements Iterable<T> {
    private static class Node<T> {
        private final T data;
        private Node<T> previous;
        private Node<T> next;

        private Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private Node<T> cursor;
    private int size;

    public void addLast(T item) {
        Node<T> node = new Node<>(item);
        if (tail == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.previous = tail;
            tail = node;
        }
        if (cursor == null) {
            cursor = node;
        }
        size++;
    }

    public boolean remove(T item) {
        for (Node<T> n = head; n != null; n = n.next) {
            if (n.data.equals(item)) {
                if (n.previous == null) {
                    head = n.next;
                } else {
                    n.previous.next = n.next;
                }
                if (n.next == null) {
                    tail = n.previous;
                } else {
                    n.next.previous = n.previous;
                }
                if (cursor == n) {
                    cursor = n.previous != null ? n.previous : n.next;
                }
                size--;
                return true;
            }
        }
        return false;
    }

    public T current() {
        return cursor == null ? null : cursor.data;
    }

    public boolean moveToPrevious() {
        if (cursor != null && cursor.previous != null) {
            cursor = cursor.previous;
            return true;
        }
        return false;
    }

    public boolean moveToNext() {
        if (cursor != null && cursor.next != null) {
            cursor = cursor.next;
            return true;
        }
        return false;
    }

    public void moveToLast() {
        cursor = tail;
    }

    public void moveToFirst() {
        cursor = head;
    }

    public int currentPosition() {
        int position = 0;
        for (Node<T> n = head; n != null; n = n.next) {
            position++;
            if (n == cursor) {
                return position;
            }
        }
        return 0;
    }

    public int size() {
        return size;
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
