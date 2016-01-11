package hk.gavin.navik.injection;

import dagger.Component;
import hk.gavin.navik.activity.NavigationActivity;
import hk.gavin.navik.fragment.NavigationFragment;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface NavigationComponent extends AbstractActivityComponent {

    void inject(NavigationActivity selectLocationOnMapActivity);
    void inject(NavigationFragment selectLocationOnMapFragment);

}
