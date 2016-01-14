package hk.gavin.navik.injection;

import android.app.Activity;
import android.content.Context;
import dagger.Component;
import hk.gavin.navik.core.map.NKMapFragment;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface AbstractActivityComponent {

    void inject(NKMapFragment nkMapFragment);

    Activity activity();
    @PerActivity Context activityContext();

}
