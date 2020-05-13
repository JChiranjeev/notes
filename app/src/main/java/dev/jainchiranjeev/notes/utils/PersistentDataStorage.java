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
    static PersistentDataStorage instance;

    private PersistentDataStorage(Context context) {
        persistentData = context.getSharedPreferences("PersistentData", Context.MODE_PRIVATE);
    }

    public static PersistentDataStorage getInstance(Context context) {
        if(instance == null) {
            instance = new PersistentDataStorage(context);
        }
        return instance;
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

    public boolean storeBoolean(String key, Boolean value) {
//        Store Data to SharedPreferences
        persistentDataEditor = persistentData.edit();
        persistentDataEditor.putBoolean(key, value);
        persistentDataEditor.commit();
        return true;
    }

    public Boolean getBoolean(String key) {
//        Get Data from SharedPreferences
        Boolean value = persistentData.getBoolean(key, true);
        return value;
    }

    public boolean clearAllData() {
        persistentDataEditor = persistentData.edit();
        persistentDataEditor.commit();
        return true;
    }
}
