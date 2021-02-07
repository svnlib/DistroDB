package com.svnlib.distrodb.node;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores the date of the node.
 */
public class DataStore {

    private static final Map<String, String> store = new HashMap<>();

    /**
     * Puts a value with its associated key into the store.
     *
     * @param key   the key of the value
     * @param value the value itself
     */
    public static void put(final String key, final String value) {
        synchronized (store) {
            store.put(key, value);
        }
    }

    /**
     * Removes the value of the given key from the store.
     *
     * @param key the key to be removed
     */
    public static void remove(final String key) {
        synchronized (store) {
            store.remove(key);
        }
    }

    /**
     * Returns the value of the given key.
     *
     * @param key the key to return
     *
     * @return the value of the key
     */
    public static String get(final String key) {
        synchronized (store) {
            return store.get(key);
        }
    }

    /**
     * Returns the size of the store.
     *
     * @return number of items in the store
     */
    public static int size() {
        synchronized (store) {
            return store.size();
        }
    }

}
