package ch.bbcag.app.bkellj.badiapp;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bmuelb on 06.07.2017.
 * Used to share data between different classes
 */

public class DataHolder {
    Map<String, WeakReference<Object>> data = new HashMap<String, WeakReference<Object>>();

    void save(String key, Object object) {
        data.put(key, new WeakReference<Object>(object));
    }

    Object get(String key) {
        WeakReference<Object> objectWeakReference = data.get(key);
        return objectWeakReference.get();
    }
}
