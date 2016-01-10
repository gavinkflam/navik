package hk.gavin.navik.injection;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import hk.gavin.navik.application.NavikApplication;
import hk.gavin.navik.preference.MainPreferences;

import javax.inject.Singleton;

@Module
public class ApplicationModule {

    private final NavikApplication mApplication;
    private MainPreferences mMainPreferences;

    public ApplicationModule(NavikApplication navikApplication) {
        mApplication = navikApplication;
    }

    @Provides @Singleton
    NavikApplication application() {
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

}
