package hk.gavin.navik.injection;

import dagger.Component;
import hk.gavin.navik.core.map.NKSkobblerMapFragment;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.fragment.*;
import hk.gavin.navik.ui.presenter.AppSettingsPresenter;
import hk.gavin.navik.ui.presenter.RouteAnalysisPresenter;
import hk.gavin.navik.ui.presenter.RouteDisplayPresenter;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = HomeModule.class)
public interface HomeComponent extends AbstractActivityComponent {

    void inject(HomeActivity homeActivity);

    void inject(LocationSelectionFragment fragment);
    void inject(NavigationFragment fragment);
    void inject(RouteAnalysisFragment fragment);
    void inject(RouteDisplayFragment fragment);
    void inject(RoutePlannerFragment fragment);
    void inject(SettingFragment fragment);
    void inject(NKSkobblerMapFragment fragment);

    void inject(RouteAnalysisPresenter presenter);
    void inject(RouteDisplayPresenter presenter);
    void inject(AppSettingsPresenter presenter);
}
