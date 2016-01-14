package hk.gavin.navik.injection;

import android.content.Context;
import dagger.Component;
import hk.gavin.navik.application.NKApplication;
import hk.gavin.navik.core.geocode.NKReverseGeocoder;
import hk.gavin.navik.core.location.NKLocationProvider;
import hk.gavin.navik.preference.MainPreferences;

import javax.inject.Singleton;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(NKApplication application);

    NKApplication application();
    @ForApplication Context applicationContext();
    MainPreferences mainPreferences();
    NKLocationProvider locationProvider();
    NKReverseGeocoder reverseGeocoder();
}
