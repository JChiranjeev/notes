package dev.jainchiranjeev.notes.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jain Chiranjeev on 6/12/2019.
 */
public class PersistentDataStorage {
    //Global Variables
    SharedPreferences persistentData = null;
    SharedPreferences.Editor persistentDataEditor = null;
    Context context = null;

    public PersistentDataStorage(Context context) {
        persistentData = context.getSharedPreferences("PersistentData", Context.MODE_PRIVATE);
    }

    public boolean storeValue(String key, String value) {
//        Store Data to SharedPreferences
        persistentDataEditor = persistentData.edit();
        persistentDataEditor.putString(key, value);
        persistentDataEditor.commit();
        return true;
    }

    public String getValue(String key) {
//        Get Data from SharedPreferences
        String value = persistentData.getString(key,null);
        return value;
    }

    public boolean clearAllData() {
        persistentDataEditor = persistentData.edit();
        persistentDataEditor.commit();
        return true;
    }
}
