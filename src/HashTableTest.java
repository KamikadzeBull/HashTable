import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void size() {
        HashTable<String, Integer> hashTable = new HashTable<>();

        setDefault(hashTable);
        assertEquals(5, hashTable.size());

        hashTable.remove("three");
        assertEquals(4, hashTable.size());

        hashTable.clear();
        assertEquals(0, hashTable.size());
    }

    private void setDefault(Map<String, Integer> hashTable){
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("three", 3);
        hashTable.put("thirteen", 13);
        hashTable.put("seven", 7);
    }

    @Test
    void isEmpty() {
        HashTable<String, Integer> hashTable = new HashTable<>();

        setDefault(hashTable);
        assertFalse(hashTable.isEmpty());

        hashTable.clear();
        assertTrue(hashTable.isEmpty());
    }

    @Test
    void containsKey() {
        HashTable<String, Integer> hashTable = new HashTable<>();

        setDefault(hashTable);
        assertTrue(hashTable.containsKey("one"));
        assertTrue(hashTable.containsKey("thirteen"));
        assertFalse(hashTable.containsKey("nineteen"));

        hashTable.remove("one");
        assertFalse(hashTable.containsKey("one"));
        assertFalse(hashTable.containsKey("anotherKey"));
    }

    @Test
    void containsValue() {
        HashTable<String, Integer> hashTable = new HashTable<>();

        setDefault(hashTable);
        assertTrue(hashTable.containsValue(1));
        assertTrue(hashTable.containsValue(13));
        assertFalse(hashTable.containsValue(19));

        hashTable.remove("one");
        assertFalse(hashTable.containsValue(1));
        assertFalse(hashTable.containsValue(10));
    }

    @Test
    void get() {
        HashTable<String, Integer> hashTable = new HashTable<>();

        setDefault(hashTable);
        int one = 1;
        assertEquals(one, hashTable.get("one"));

        hashTable.remove("one");
        assertNull(hashTable.get("one"));
    }

    @Test
    void put() {
        HashTable<String, Integer> hashTable = new HashTable<>();

        setDefault(hashTable);
        assertNull(hashTable.put("four", 4));
        assertNotNull(hashTable.put("four", 4));
    }

    @Test
    void remove() {
        HashTable<String, Integer> hashTable = new HashTable<>();

        setDefault(hashTable);
        int one = 1;
        assertEquals(one, hashTable.remove("one"));
        assertNull(hashTable.remove("six"));
    }

    @Test
    void putAll() {
        HashTable<String, Integer> hashTable1 = new HashTable<>();
        HashTable<String, Integer> hashTable2 = new HashTable<>();

        setDefault(hashTable1);

        hashTable2.put("one", 1);
        hashTable2.put("two", 2);
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("three", 3);
        map.put("thirteen", 13);
        map.put("seven", 7);
        hashTable2.putAll(map);

        assertEquals(hashTable1, hashTable2);
    }

    @Test
    void clear() {
        HashTable<String, Integer> hashTable = new HashTable<>();

        setDefault(hashTable);
        hashTable.clear();
        assertTrue(hashTable.isEmpty());
    }

    @Test
    void keySet() {
        HashTable<String, Integer> hashTable = new HashTable<>();

        setDefault(hashTable);
        Map<String, Integer> map = new Hashtable<>();

        setDefault(map);
        assertEquals(map.keySet(), hashTable.keySet());

        hashTable.clear();
        assertEquals(new HashSet(), hashTable.keySet());
    }

    @Test
    void values() {
        HashTable<String, Integer> hashTable = new HashTable<>();

        setDefault(hashTable);
        assertFalse(hashTable.values().isEmpty());
        hashTable.values().clear();
        assertTrue(hashTable.values().isEmpty());
    }

    @Test
    void entrySet() {
        HashTable<String, Integer> hashTable = new HashTable<>();
        setDefault(hashTable);
        Map<String, Integer> map = new Hashtable<>();
        setDefault(map);
        assertEquals(map.entrySet(), hashTable.entrySet());

        hashTable.clear();
        assertTrue(hashTable.entrySet().isEmpty());
    }

    @Test
    void equals() {
        HashTable<String, Integer> ht1 = new HashTable<>();
        setDefault(ht1);
        Map<String, Integer> map1 = new Hashtable<>();
        setDefault(map1);

        HashTable<String, Integer> ht2 = new HashTable<>();
        setDefault(ht2);
        Map<String, Integer> map2 = new Hashtable<>();
        setDefault(map2);

        HashTable<String, Integer> ht3 = new HashTable<>();
        ht3.put("fifty", 50);
        Map<String, Integer> map3 = new Hashtable<>();
        map3.put("fifty", 50);

        HashTable<String, Integer> ht4 = new HashTable<>();
        setDefault(ht4);
        ht4.put("sixty", 60);

        assertEquals(ht1, ht2);
        assertNotEquals(ht1, ht3);
        assertNotEquals(ht1, ht4);
        assertNotEquals(null, ht1);

        assertEquals(map1.hashCode(), ht1.hashCode());
        assertEquals(map2.hashCode(), ht2.hashCode());
        assertEquals(map3.hashCode(), ht3.hashCode());
    }
}