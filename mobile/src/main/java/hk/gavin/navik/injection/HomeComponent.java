package hk.gavin.navik.injection;

import dagger.Component;
import hk.gavin.navik.activity.HomeActivity;
import hk.gavin.navik.fragment.HomeFragment;
import hk.gavin.navik.fragment.RouteDisplayFragment;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface HomeComponent extends AbstractActivityComponent {

    void inject(HomeActivity homeActivity);
    void inject(HomeFragment homeFragment);
    void inject(RouteDisplayFragment routeDisplayFragment);

}
