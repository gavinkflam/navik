package hk.gavin.navik.preference;

import android.content.Context;

public class MainPreferences extends AbstractPreferences {

    public static final String PREFS_NAME = "MainPreferences";

    public static final String KEY_MAP_RESOURCES_PATH = "MapResourcesPath";

    public MainPreferences(Context context) {
        super(context, PREFS_NAME);
    }

    public String getMapResourcesPath() {
        return getStringPreference(KEY_MAP_RESOURCES_PATH);
    }

    public void setMapResourcesPath(String mapResourcesPath) {
        setStringPreference(KEY_MAP_RESOURCES_PATH, mapResourcesPath);
    }
}
