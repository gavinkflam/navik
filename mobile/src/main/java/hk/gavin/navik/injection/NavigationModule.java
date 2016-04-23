package hk.gavin.navik.injection;

import android.app.Activity;
import com.google.common.base.Optional;
import dagger.Module;
import dagger.Provides;
import hk.gavin.navik.ui.activity.NavigationActivity;
import hk.gavin.navik.ui.fragmentcontroller.NavigationFragmentController;

@Module
public class NavigationModule extends ActivityModule {

    private Optional<NavigationFragmentController> mController = Optional.absent();

    public NavigationModule(Activity activity) {
        super(activity);
    }

    @Provides
    public NavigationFragmentController controller() {
        if (!mController.isPresent()) {
            mController = Optional.of(
                    new NavigationFragmentController((NavigationActivity) activity())
            );
        }
        return mController.get();
    }
}
