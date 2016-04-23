package hk.gavin.navik.injection;

import dagger.Component;
import hk.gavin.navik.core.map.NKSkobblerMapFragment;
import hk.gavin.navik.ui.activity.NavigationActivity;
import hk.gavin.navik.ui.fragment.NavigationFragment;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = NavigationModule.class)
public interface NavigationComponent extends AbstractActivityComponent {

    void inject(NavigationActivity navigationActivity);

    void inject(NavigationFragment fragment);
    void inject(NKSkobblerMapFragment fragment);
}
