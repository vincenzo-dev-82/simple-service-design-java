package com.vincenzo.simple.lru;

import java.util.*;

public class LRUCache {

    private final int capacity; // 캐시 용량
    private final LinkedHashMap<Integer, Integer> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;

        // LinkedHashMap 초기화 (accessOrder를 true로 설정하여 접근 순서 유지)
        this.cache = new LinkedHashMap<>(this.capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                // 용량 초과 시 가장 오래된 항목 제거
                return size() > capacity;
            }
        };
    }

    public int get(int key) {
        // 키가 존재하면 값을 반환, 없으면 -1 반환
        // return cache.getOrDefault(key, -1);
        if(cache.containsKey(key)) {
            return cache.get(key);
        }
        return -1;
    }

    public void put(int key, int value) {
        // 키와 값을 삽입 (기존 키가 있으면 업데이트)
        cache.put(key, value);
    }

    public static void main(String[] args) {
        LRUCache lRUCache = new LRUCache(2);
        lRUCache.put(1, 1); // cache is {1=1}
        lRUCache.put(2, 2); // cache is {1=1, 2=2}
        int cache1 = lRUCache.get(1);    // return 1
        System.out.println(cache1);

        lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
        int cache2 = lRUCache.get(2);    // returns -1 (not found)
        System.out.println(cache2);

        lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
        lRUCache.get(1);    // return -1 (not found)
        lRUCache.get(3);    // return 3
        int cache4 = lRUCache.get(4);    // return 4
        System.out.println(cache4);
    }
}
