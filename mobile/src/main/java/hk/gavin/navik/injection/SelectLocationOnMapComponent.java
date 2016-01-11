package hk.gavin.navik.injection;

import dagger.Component;
import hk.gavin.navik.activity.SelectLocationOnMapActivity;
import hk.gavin.navik.fragment.SelectLocationOnMapFragment;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface SelectLocationOnMapComponent extends AbstractActivityComponent {

    void inject(SelectLocationOnMapActivity selectLocationOnMapActivity);
    void inject(SelectLocationOnMapFragment selectLocationOnMapFragment);

}
