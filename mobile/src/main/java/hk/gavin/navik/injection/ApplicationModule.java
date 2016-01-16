package hk.gavin.navik.injection;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import hk.gavin.navik.application.NKApplication;
import hk.gavin.navik.core.directions.NKDirectionsProvider;
import hk.gavin.navik.core.directions.NKInteractiveDirectionsProvider;
import hk.gavin.navik.core.directions.NKSkobblerDirectionsProvider;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.geocode.NKSkobblerReverseGeocoder;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.core.location.NKSkobblerLocationProvider;
import hk.gavin.navik.preference.MainPreferences;

import javax.inject.Singleton;

@Module
public class ApplicationModule {

    private final NKApplication mApplication;
    private MainPreferences mMainPreferences;
    private NKLocationProvider mNKLocationProvider;
    private NKReverseGeocoder mNKReverseGeocoder;
    private NKDirectionsProvider mNKDirectionsProvider;
    private NKInteractiveDirectionsProvider mNKSkobblerInteractiveDirectionsProvider;

    public ApplicationModule(NKApplication nkApplication) {
        mApplication = nkApplication;
    }

    @Provides @Singleton
    NKApplication application() {
        return mApplication;
    }

    @Provides @Singleton @ForApplication Context applicationContext() {
        return mApplication;
    }

    @Provides @Singleton MainPreferences mainPreferences() {
        if (mMainPreferences == null) {
            mMainPreferences = new MainPreferences(mApplication);
        }
        return mMainPreferences;
    }

    @Provides @Singleton
    NKLocationProvider locationProvider() {
        if (mNKLocationProvider == null) {
            mNKLocationProvider = new NKSkobblerLocationProvider(mApplication);
        }
        return mNKLocationProvider;
    }

    @Provides @Singleton
    NKReverseGeocoder reverseGeocoder() {
        if (mNKReverseGeocoder == null) {
            mNKReverseGeocoder = new NKSkobblerReverseGeocoder();
        }
        return mNKReverseGeocoder;
    }

    @Provides @Singleton
    NKDirectionsProvider directionsProvider() {
        if (mNKDirectionsProvider == null) {
            mNKDirectionsProvider = new NKSkobblerDirectionsProvider();
        }
        return mNKDirectionsProvider;
    }

    @Provides @Singleton
    NKInteractiveDirectionsProvider interactiveDirectionsProvider() {
        if (mNKSkobblerInteractiveDirectionsProvider == null) {
            mNKSkobblerInteractiveDirectionsProvider = new NKInteractiveDirectionsProvider(directionsProvider());
        }
        return mNKSkobblerInteractiveDirectionsProvider;
    }
}
