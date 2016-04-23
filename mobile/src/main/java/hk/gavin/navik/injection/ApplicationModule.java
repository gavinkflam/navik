package hk.gavin.navik.injection;

import com.google.common.base.Optional;
import dagger.Module;
import dagger.Provides;
import hk.gavin.navik.application.NKApplication;
import hk.gavin.navik.core.directions.NKDirectionsProvider;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.directions.NKSkobblerDirectionsProvider;
import hk.gavin.navik.core.elevation.NKElevationProvider;
import hk.gavin.navik.core.elevation.NKGoogleElevationProvider;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.geocode.NKSkobblerReverseGeocoder;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.location.NKSkobblerLocationProvider;
import hk.gavin.navik.core.wear.NKWearManager;
import hk.gavin.navik.preference.MainPreferences;

import javax.inject.Singleton;

@Module
public class ApplicationModule {

    private final NKApplication mApplication;
    private Optional<MainPreferences> mMainPreferences = Optional.absent();
    private Optional<? extends NKLocationProvider> mNKLocationProvider = Optional.absent();
    private Optional<? extends NKReverseGeocoder> mNKReverseGeocoder = Optional.absent();
    private Optional<? extends NKElevationProvider> mNKElevationProvider = Optional.absent();
    private Optional<? extends NKWearManager> mNKWearManager = Optional.absent();
    private Optional<? extends NKDirectionsProvider> mNKDirectionsProvider = Optional.absent();
    private Optional<? extends NKInteractiveDirectionsProvider> mNKSkobblerInteractiveDirectionsProvider =
            Optional.absent();

    public ApplicationModule(NKApplication nkApplication) {
        mApplication = nkApplication;
    }

    @Provides @Singleton
    NKApplication application() {
        return mApplication;
    }

    @Provides @Singleton
    MainPreferences mainPreferences() {
        if (!mMainPreferences.isPresent()) {
            mMainPreferences = Optional.of(new MainPreferences(mApplication));
        }
        return mMainPreferences.get();
    }

    @Provides @Singleton
    NKLocationProvider locationProvider() {
        if (!mNKLocationProvider.isPresent()) {
            mNKLocationProvider = Optional.of(new NKSkobblerLocationProvider(mApplication));
        }
        return mNKLocationProvider.get();
    }

    @Provides @Singleton
    NKReverseGeocoder reverseGeocoder() {
        if (!mNKReverseGeocoder.isPresent()) {
            mNKReverseGeocoder = Optional.of(new NKSkobblerReverseGeocoder());
        }
        return mNKReverseGeocoder.get();
    }

    @Provides @Singleton
    NKElevationProvider elevationProvider() {
        if (!mNKElevationProvider.isPresent()) {
            mNKElevationProvider = Optional.of(new NKGoogleElevationProvider());
        }
        return mNKElevationProvider.get();
    }

    @Provides @Singleton
    NKWearManager wearManager() {
        if (!mNKWearManager.isPresent()) {
            mNKWearManager = Optional.of(new NKWearManager(mApplication));
        }
        return mNKWearManager.get();
    }

    @Provides @Singleton
    NKDirectionsProvider directionsProvider() {
        if (!mNKDirectionsProvider.isPresent()) {
            mNKDirectionsProvider = Optional.of(new NKSkobblerDirectionsProvider());
        }
        return mNKDirectionsProvider.get();
    }

    @Provides @Singleton
    NKInteractiveDirectionsProvider interactiveDirectionsProvider() {
        if (!mNKSkobblerInteractiveDirectionsProvider.isPresent()) {
            mNKSkobblerInteractiveDirectionsProvider = Optional.of(
                    new NKInteractiveDirectionsProvider(directionsProvider())
            );
        }
        return mNKSkobblerInteractiveDirectionsProvider.get();
    }
}
