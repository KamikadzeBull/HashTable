import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HashTable<K, V> implements Map<K, V> {

    private HashTable.Node[] table;
    private boolean exists[];

    private int capacity;
    private int size = 0;
    private float loadFactor;

    private static final int DEFAULT_CAPACITY = 8;
    private static final int MAXIMUM_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;

    public HashTable(){
        this(LOAD_FACTOR, DEFAULT_CAPACITY);
    }

    public HashTable(int capacity){
        this(LOAD_FACTOR, capacity);
    }

    public HashTable(float loadFactor, int capacity){
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

        table = new HashTable.Node[this.capacity];
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
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    private int contains(Object key){
        return 0;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
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

    class Node implements Map.Entry<K, V>{
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
            return key + "=>" + value;
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof HashTable.Node) {
                HashTable.Node node = (HashTable.Node) obj;
                return Objects.equals(key, node.key) && Objects.equals(value, node.value);
            }
            return false;
        }
    }
}
