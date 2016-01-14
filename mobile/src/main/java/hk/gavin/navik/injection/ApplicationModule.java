package hk.gavin.navik.injection;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import hk.gavin.navik.application.NKApplication;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.preference.MainPreferences;

import javax.inject.Singleton;

@Module
public class ApplicationModule {

    private final NKApplication mApplication;
    private MainPreferences mMainPreferences;
    private NKLocationProvider mNKLocationProvider;

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
    NKLocationProvider navikLocationProvider() {
        if (mNKLocationProvider == null) {
            mNKLocationProvider = new NKLocationProvider(mApplication);
        }
        return mNKLocationProvider;
    }

}
