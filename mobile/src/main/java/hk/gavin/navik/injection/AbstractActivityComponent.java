package hk.gavin.navik.injection;

import android.app.Activity;
import android.content.Context;
import dagger.Component;
import hk.gavin.navik.map.NavikMapFragment;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface AbstractActivityComponent {

    void inject(NavikMapFragment navikMapFragment);

    Activity activity();
    @PerActivity Context activityContext();

}
