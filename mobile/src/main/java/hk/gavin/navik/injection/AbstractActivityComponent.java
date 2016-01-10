package hk.gavin.navik.injection;

import android.app.Activity;
import android.content.Context;
import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface AbstractActivityComponent {

    Activity activity();
    @PerActivity Context activityContext();

}
