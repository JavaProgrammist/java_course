package ru.otus.cachehw;


import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы

    private final Map<K, V> hashMap = new WeakHashMap<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        hashMap.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = hashMap.remove(key);
        notifyListeners(key, value, "remove");
    }

    @Override
    public V get(K key) {
        V value = hashMap.get(key);
        notifyListeners(key, value, "get");
        return value;
    }

    @Override
    public int size() {
        return hashMap.size();
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        listeners.forEach(item -> item.notify(key, value, action));
    }
}
