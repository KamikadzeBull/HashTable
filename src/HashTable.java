import java.util.*;

public class HashTable<K, V> implements Map<K, V> {

    private Node<?, ?>[] table;
    private boolean exists[];

    private int capacity;
    private int size = 0;
    private float loadFactor;

    private static final int DEFAULT_CAPACITY = 8;
    private static final float LOAD_FACTOR = 0.75f;

    public HashTable(){
        this(LOAD_FACTOR, DEFAULT_CAPACITY);
    }

    public HashTable(int capacity){
        this(LOAD_FACTOR, capacity);
    }

    public HashTable(float loadFactor, int capacity){
        int MAXIMUM_CAPACITY = 16;
        if (loadFactor <= LOAD_FACTOR) {
            this.loadFactor = loadFactor;
        } else {
            this.loadFactor = LOAD_FACTOR;
        }

        if (capacity >= DEFAULT_CAPACITY && capacity
                <= MAXIMUM_CAPACITY) {
            this.capacity = capacity;
        } else {
            this.capacity = DEFAULT_CAPACITY;
        }

        table = new Node[this.capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return contains(key) >= 0;
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i <= capacity - 1; i++) {
            if (exists[i] && table[i].getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    private int contains(Object key) {
        return find(key, Type.CONTAINS);
    }

    private int findFree(Object key) {
        return find(key, Type.FREE);
    }

    private int forRemove(Object key) {
        return find(key, Type.DELETE);
    }

    private int getIndex(Object key) {
        return Math.abs(key.hashCode() + size) % (capacity - 1);
    }

    private int find(Object key, Type type) {
        int index = getIndex(key);
        for (int i = index, j = 0; j <= capacity - 1; i = (i + 1) % (capacity - 1), j++) {
            boolean b = false;
            if (table[i] != null) {
                b = table[i].getKey().equals(key);
            }
            if (predicate(type, exists[i], b)) {
                return i;
            }
        }
        return -1;
    }

    private boolean predicate(Type type, boolean b1, boolean b2) {
        boolean bool = false;
        switch (type) {
            case CONTAINS:
                bool = b1 && b2;
                break;
            case FREE:
                bool = !b1;
                break;
            case DELETE:
                bool = b2;
                break;
        }
        return bool;
    }

    @Override
    public V get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        int index = contains(key);
        if (index < 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        Node<K, V> result = (Node<K, V>) table[index];
        return result.getValue();
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        int index = contains(key);
        if (index < 0) {
            putNode(key, value);
            return null;
        }
        @SuppressWarnings("unchecked")
        Node<K, V> result = (Node<K, V>) table[index];
        result.setValue(value);
        return result.getValue();
    }

    private void putNode(K key, V value) {
        if (size / capacity >= loadFactor) {
            rehash();
        }
        int index = findFree(key);
        table[index] = new Node<>(key, value);
        exists[index] = true;
        size++;
    }

    private void rehash() {
        int old = capacity;
        capacity *= 2;
        Node[] between = Arrays.copyOf(table, old);
        boolean[] del = Arrays.copyOf(exists, old);

        table = new Node[capacity];
        exists = new boolean[capacity];
        for (int i = 0; i < old; i++) {
            if (exists[i]) {
                int index = findFree(between[i].getKey());
                table[index] = between[i];
                exists[index] = true;
            }
        }
    }

    @Override
    public V remove(Object key) {
        int index = forRemove(key);
        if (index < 0){
            return null;
        } else if (exists[index]) {
            exists[index] = false;
            size--;
            //noinspection unchecked
            return (V) table[index].getValue();
        } else {
            return null;
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    enum Type {
        CONTAINS, FREE, DELETE
    }

    private static class Node<K, V> implements Map.Entry<K, V>{
        private K key;
        private V value;

        Node(K key, V value) {
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
            if (value == null)
                throw new NullPointerException();

            V old = this.value;
            this.value = value;
            return old;
        }

        public final String toString() {
            return key.toString() + "=" + value.toString();
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> e = (Map.Entry<?, ?>) obj;
            return (key == null ? e.getKey() == null : key.equals(e.getKey())) &&
                    (value == null ? e.getValue() == null : value.equals(e.getValue()));
        }
    }
}
