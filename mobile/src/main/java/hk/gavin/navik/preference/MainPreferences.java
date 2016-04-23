package hk.gavin.navik.preference;

import android.content.Context;
import hk.gavin.navik.core.location.NKLocation;

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
        return getBooleanPreference(Key.OFFLINE_MODE, true);
    }

    public void setOfflineMode(boolean offlineMode) {
        setBooleanPreference(Key.OFFLINE_MODE, offlineMode);
    }

    public NKLocation getLastLocation() {
        double lastLocationLat = getDoublePreference(Key.LAST_LOCATION_LAT);
        double lastLocationLng = getDoublePreference(Key.LAST_LOCATION_LNG);

        if (lastLocationLat == 0 && lastLocationLng == 0) {
            // Return Tai Po Market Station as last location if no previous stored location
            return new NKLocation(22.4447152, 114.1686471);
        }
        else {
            return new NKLocation(lastLocationLat, lastLocationLng);
        }
    }

    public void setLastLocation(NKLocation location) {
        setDoublePreference(Key.LAST_LOCATION_LAT, location.latitude);
        setDoublePreference(Key.LAST_LOCATION_LNG, location.longitude);
    }

    public static final class Key {
        public static final String MAP_RESOURCES_PATH = "MapResourcesPath";
        public static final String SIMULATION_MODE = "SimulationMode";
        public static final String OFFLINE_MODE = "OfflineMode";
        public static final String LAST_LOCATION_LAT = "LastLocationLat";
        public static final String LAST_LOCATION_LNG = "LastLocationLng";
    }
}
