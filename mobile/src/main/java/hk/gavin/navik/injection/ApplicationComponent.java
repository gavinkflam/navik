package hk.gavin.navik.injection;

import android.content.Context;
import dagger.Component;
import hk.gavin.navik.application.NavikApplication;
import hk.gavin.navik.location.NavikLocationProvider;
import hk.gavin.navik.preference.MainPreferences;

import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(NavikApplication application);

    NavikApplication application();
    @ForApplication Context applicationContext();
    MainPreferences mainPreferences();
    NavikLocationProvider navikLocationProvider();
}
