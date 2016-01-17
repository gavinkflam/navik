package hk.gavin.navik.injection;

import dagger.Component;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.fragment.RoutePlannerFragment;
import hk.gavin.navik.ui.fragment.NavigationFragment;
import hk.gavin.navik.ui.fragment.RouteDisplayFragment;
import hk.gavin.navik.ui.fragment.LocationSelectionFragment;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = HomeModule.class)
public interface HomeComponent extends AbstractActivityComponent {

    void inject(HomeActivity homeActivity);
    void inject(RoutePlannerFragment routePlannerFragment);
    void inject(LocationSelectionFragment locationSelectionFragment);
    void inject(RouteDisplayFragment routeDisplayFragment);
    void inject(NavigationFragment navigationFragment);
}
