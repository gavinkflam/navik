package hk.gavin.navik.injection;

import dagger.Component;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.fragment.*;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = HomeModule.class)
public interface HomeComponent extends AbstractActivityComponent {

    void inject(HomeActivity homeActivity);
    void inject(LocationSelectionFragment fragment);
    void inject(NavigationFragment fragment);
    void inject(RouteDisplayFragment fragment);
    void inject(RoutePlannerFragment fragment);
}
