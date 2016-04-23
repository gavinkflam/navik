package hk.gavin.navik.application;

import android.app.Application;
import com.skobbler.ngx.sdktools.navigationui.NKSKToolsLogicManager;
import com.skobbler.ngx.sdktools.navigationui.SKToolsLogicManager;
import hk.gavin.navik.injection.ApplicationComponent;
import hk.gavin.navik.injection.ApplicationModule;
import hk.gavin.navik.injection.DaggerApplicationComponent;
import hk.gavin.navik.preference.MainPreferences;
import hk.gavin.navik.util.LoggerUtility;
import hk.gavin.navik.util.StorageUtility;

import javax.inject.Inject;

public class NKApplication extends Application {

    static {
        // Hook navigation state update event
        SKToolsLogicManager.CLASS = NKSKToolsLogicManager.class;
    }

    private static NKApplication mInstance;
    private ApplicationComponent mApplicationComponent;

    @Inject MainPreferences mMainPreferences;

    public static NKApplication getInstance() {
        return mInstance;
    }

    public NKApplication() {
        super();
        mInstance = this;
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

    @Override
    public void onCreate() {
        super.onCreate();
        LoggerUtility.initializeLogger(this);

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
    }
}
