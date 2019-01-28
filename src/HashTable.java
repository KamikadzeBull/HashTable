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
        for (Entry<? extends K, ? extends V> elem : m.entrySet()) {
            put(elem.getKey(), elem.getValue());
        }
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            table[i] = null;
            exists[i] = false;
        }
        size = 0;
    }

    private <T> java.util.Iterator<T> getIterator(TypeIterator type) {
        if (size == 0) {
            return Collections.emptyIterator();
        } else {
            return new HashTableIterator<>(type);
        }
    }

    enum TypeIterator {ENTRY, KEYS, VALUES}

    @Override
    public Set<K> keySet() {
        return keySet;
    }

    private Set<K> keySet = new KeySet();

    private class KeySet extends AbstractSet<K> {
        public java.util.Iterator<K> iterator() {
            return getIterator(TypeIterator.KEYS);
        }
        public int size() {
            return size;
        }
        public boolean contains(Object o) {
            return containsKey(o);
        }
        public boolean remove(Object o) {
            return HashTable.this.remove(o) != null;
        }
        public void clear() {
            HashTable.this.clear();
        }
    }

    @Override
    public Collection<V> values() {
        return values;
    }

    private Collection<V> values = new ValueCollection();

    private class ValueCollection extends AbstractCollection<V> {
        public Iterator<V> iterator() {
            return getIterator(TypeIterator.VALUES);
        }
        public int size() {
            return size;
        }
        public boolean contains(Object o) {
            return containsValue(o);
        }
        public void clear() {
            HashTable.this.clear();
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entrySet;
    }

    private Set<Map.Entry<K, V>> entrySet = new EntrySet();

    class EntrySet extends AbstractSet<Entry<K, V>> {
        public Iterator<Entry<K, V>> iterator() {
            return getIterator(TypeIterator.ENTRY);
        }

        @Override
        public int size() {
            return size;
        }
        public boolean add(Map.Entry<K, V> o) {
            return super.add(o);
        }
        public boolean contains(Object o) {
            Object key = getK(o);
            return HashTable.this.containsKey(key);
        }
        public boolean remove(Object o) {
            Object key = getK(o);
            return HashTable.this.remove(key) != null;
        }
        private Object getK(Object o){
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            return entry.getKey();
        }
        public void clear() {
            HashTable.this.clear();
        }
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        if (((Map) obj).size() != this.size){
            return false;
        }
        for (Object o : ((Map) obj).entrySet()) {
            @SuppressWarnings("unchecked")
            Entry<K, V> elem = (Entry<K, V>) o;
            K key = elem.getKey();
            V value = elem.getValue();
            if (!containsKey(key) || get(key) != value) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (Entry<K, V> entry : this.entrySet()) {
            hash += entry.hashCode();
        }
        return hash;
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

    class HashTableIterator<T> implements java.util.Iterator<T> {

        Node[] table = HashTable.this.table;
        boolean[] exist = HashTable.this.exists;

        int i = -1;
        int count = 0;
        int size1 = HashTable.this.size;
        TypeIterator type;

        HashTableIterator(TypeIterator type) {
            this.type = type;
        }

        @Override
        public boolean hasNext() {
            return count < size1;
        }

        @Override
        public T next() {
            i++;
            while (i < capacity) {
                if (exist[i]) {
                    Map.Entry node = table[i];
                    count++;
                    switch (type) {
                        case KEYS:
                            //noinspection unchecked
                            return (T) node.getKey();
                        case VALUES:
                            //noinspection unchecked
                            return (T) node.getValue();
                        default:
                            //noinspection unchecked
                            return (T) node;
                    }
                } else {
                    i++;
                }
            }
            return null;
        }
    }
}
