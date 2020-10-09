package ru.made.flitter.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CollectionTestUtils {

    public static Map<String, Object> castToMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        } else {
            throw new IllegalArgumentException("Object is not a map: " + obj);
        }
    }

    public static List<Map<String, Object>> castToMaps(Object[] arr) {
        List<Map<String, Object>> outputList = new ArrayList<>();

        for (Object obj : arr) {
            Map<String, Object> map = castToMap(obj);
            outputList.add(map);
        }

        return outputList;
    }

    public static <T> T getSingleValue(Collection<T> collection) {
        assertNotEquals("Collection expected to contain single value, but zero", 0, collection.size());
        assertEquals("Collection expected to contain single value, but has more than one: ", 1, collection.size());

        return collection.iterator().next();
    }

    public static <A, B> void assertSetEquals(Collection<A> collectionOne, Collection<B> collectionTwo) {
        assertEquals(
                new HashSet<>(collectionOne),
                new HashSet<>(collectionTwo)
        );
    }

    public static <K, V> Map<K, V> mapOf(K k_1, V v_1) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(k_1, v_1);
        return map;
    }

    public static <K, V> Map<K, V> mapOf(
            K k_1, V v_1,
            K k_2, V v_2
    ) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(k_1, v_1);
        map.put(k_2, v_2);
        return map;
    }

    public static <K, V> Map<K, V> mapOf(
            K k_1, V v_1,
            K k_2, V v_2,
            K k_3, V v_3
    ) {
        Map<K, V> map = new LinkedHashMap<>();
        map.put(k_1, v_1);
        map.put(k_2, v_2);
        map.put(k_3, v_3);
        return map;
    }
}
