package hk.gavin.navik.application;

import android.app.Application;
import hk.gavin.navik.injection.ApplicationComponent;
import hk.gavin.navik.injection.ApplicationModule;
import hk.gavin.navik.injection.DaggerApplicationComponent;
import hk.gavin.navik.preference.MainPreferences;
import hk.gavin.navik.util.SkobblerUtility;
import hk.gavin.navik.util.StorageUtility;

import javax.inject.Inject;

public class NavikApplication extends Application {

    private static NavikApplication mInstance;
    private ApplicationComponent mApplicationComponent;

    @Inject MainPreferences mMainPreferences;

    @Override
    public void onCreate() {
        mInstance = this;
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);

        SkobblerUtility.prepareAndInitializeLibrary();
    }

    public static NavikApplication getInstance() {
        return mInstance;
    }

    public String getMapResourcesPath() {
        String mapResourcesPath = mMainPreferences.getMapResourcesPath();
        if (mapResourcesPath.length() == 0) {
            mapResourcesPath = String.format(
                    "%s/%s/", StorageUtility.getInitialStoragePath(this), StorageUtility.mapResourcesDirSuffix
            );
            mMainPreferences.setMapResourcesPath(mapResourcesPath);
        }

        return mapResourcesPath;
    }

    public ApplicationComponent component() {
        return mApplicationComponent;
    }
}
