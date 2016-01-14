package hk.gavin.navik.injection;

import dagger.Component;
import hk.gavin.navik.activity.HomeActivity;
import hk.gavin.navik.fragment.HomeFragment;
import hk.gavin.navik.fragment.RouteDisplayFragment;
import hk.gavin.navik.fragment.SelectLocationOnMapFragment;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = HomeModule.class)
public interface HomeComponent extends AbstractActivityComponent {

    void inject(HomeActivity homeActivity);
    void inject(HomeFragment homeFragment);
    void inject(SelectLocationOnMapFragment selectLocationOnMapFragment);
    void inject(RouteDisplayFragment routeDisplayFragment);

}
