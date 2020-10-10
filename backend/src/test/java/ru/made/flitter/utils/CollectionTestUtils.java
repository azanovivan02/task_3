package ru.made.flitter.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CollectionTestUtils {

    @SuppressWarnings("unchecked")
    public static Map<String, Object> castToMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        } else {
            throw new IllegalArgumentException("Object is not a map: " + obj);
        }
    }

    public static List<Map<String, Object>> castToMaps(Object[] arr) {
        var outputList = new ArrayList<Map<String, Object>>();

        for (var obj : arr) {
            Map<String, Object> map = castToMap(obj);
            outputList.add(map);
        }

        return outputList;
    }

    public static <A, B> void assertSetEquals(Collection<A> collectionOne, Collection<B> collectionTwo) {
        assertEquals(
                new HashSet<>(collectionOne),
                new HashSet<>(collectionTwo)
        );
    }
}
