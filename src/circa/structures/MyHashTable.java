package circa.structures;

public class MyHashTable<K, V> {
    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        private Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Entry<K, V>[] buckets;
    private int size;

    @SuppressWarnings("unchecked")
    public MyHashTable() {
        buckets = (Entry<K, V>[]) new Entry[16];
    }

    private int indexFor(K key, int length) {
        return (key.hashCode() & 0x7fffffff) % length;
    }

    public void put(K key, V value) {
        int index = indexFor(key, buckets.length);
        for (Entry<K, V> e = buckets[index]; e != null; e = e.next) {
            if (e.key.equals(key)) {
                e.value = value;
                return;
            }
        }
        buckets[index] = new Entry<>(key, value, buckets[index]);
        size++;
        if (size > buckets.length * 3 / 4) {
            resize();
        }
    }

    public V get(K key) {
        for (Entry<K, V> e = buckets[indexFor(key, buckets.length)]; e != null; e = e.next) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public V remove(K key) {
        int index = indexFor(key, buckets.length);
        Entry<K, V> previous = null;
        for (Entry<K, V> e = buckets[index]; e != null; previous = e, e = e.next) {
            if (e.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = e.next;
                } else {
                    previous.next = e.next;
                }
                size--;
                return e.value;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Entry<K, V>[] old = buckets;
        buckets = (Entry<K, V>[]) new Entry[old.length * 2];
        for (Entry<K, V> head : old) {
            for (Entry<K, V> e = head; e != null; e = e.next) {
                int index = indexFor(e.key, buckets.length);
                buckets[index] = new Entry<>(e.key, e.value, buckets[index]);
            }
        }
    }
}
