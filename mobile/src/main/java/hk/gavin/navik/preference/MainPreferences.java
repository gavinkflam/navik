package hk.gavin.navik.preference;

import android.content.Context;

public class MainPreferences extends AbstractPreferences {

    public static final String PREFS_NAME = "MainPreferences";

    public MainPreferences(Context context) {
        super(context, PREFS_NAME);
    }

    public String getMapResourcesPath() {
        return getStringPreference(Key.MAP_RESOURCES_PATH);
    }

    public void setMapResourcesPath(String mapResourcesPath) {
        setStringPreference(Key.MAP_RESOURCES_PATH, mapResourcesPath);
    }

    public boolean getSimulationMode() {
        return getBooleanPreference(Key.SIMULATION_MODE);
    }

    public void setSimulationMode(boolean simulationMode) {
        setBooleanPreference(Key.SIMULATION_MODE, simulationMode);
    }

    public boolean getOfflineMode() {
        return getBooleanPreference(Key.OFFLINE_MODE);
    }

    public void setOfflineMode(boolean offlineMode) {
        setBooleanPreference(Key.OFFLINE_MODE, offlineMode);
    }

    public static final class Key {
        public static final String MAP_RESOURCES_PATH = "MapResourcesPath";
        public static final String SIMULATION_MODE = "SimulationMode";
        public static final String OFFLINE_MODE = "OfflineMode";
    }
}
