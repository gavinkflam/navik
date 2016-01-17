package hk.gavin.navik.preference;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class AbstractPreferences {

    protected final SharedPreferences mPreferences;
    protected final SharedPreferences.Editor mEditor;

    protected AbstractPreferences(Context context, String preferenceName) {
        mPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    protected int getIntPreference(String key) {
        return mPreferences.getInt(key, 0);
    }

    protected String getStringPreference(String key) {
        return mPreferences.getString(key, "");
    }

    protected boolean getBooleanPreference(String key) {
        return mPreferences.getBoolean(key, false);
    }

    protected void setIntPreference(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    protected void setStringPreference(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    protected void setBooleanPreference(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }
}
