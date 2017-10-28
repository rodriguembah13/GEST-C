package com.ballack.com.web.rest.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ballack on 26/10/2017.
 */
public class MapValueComparator <K, V> implements Comparator<K> {
    private final HashMap<K, V> mapToSort;
    private final Comparator<V> valueComparator;

    public MapValueComparator(HashMap<K, V> mapToSort, Comparator<V> valueComparator){
        this.mapToSort = mapToSort;
        this.valueComparator = valueComparator;
    }

    public int compare(K key1, K key2) {
        return valueComparator.compare(mapToSort.get(key1), mapToSort.get(key2));
    }
}
