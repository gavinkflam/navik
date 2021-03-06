package hk.gavin.navik.injection;

import dagger.Component;
import hk.gavin.navik.application.NKApplication;
import hk.gavin.navik.core.directions.NKDirectionsProvider;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.elevation.NKElevationProvider;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.wear.NKWearManager;
import hk.gavin.navik.preference.MainPreferences;

import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(NKApplication application);

    NKApplication application();
    MainPreferences mainPreferences();
    NKLocationProvider locationProvider();
    NKReverseGeocoder reverseGeocoder();
    NKElevationProvider elevationProvider();
    NKWearManager wearManager();
    NKDirectionsProvider directionsProvider();
    NKInteractiveDirectionsProvider interactiveDirectionsProvider();
}
