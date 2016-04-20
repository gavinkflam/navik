package hk.gavin.navik.injection;

import android.app.Activity;
import com.google.common.base.Optional;
import dagger.Module;
import dagger.Provides;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.fragmentcontroller.HomeFragmentController;

@Module
public class HomeModule extends ActivityModule {

    private Optional<HomeFragmentController> mController = Optional.absent();

    public HomeModule(Activity activity) {
        super(activity);
    }

    @Provides
    public HomeFragmentController controller() {
        if (!mController.isPresent()) {
            mController = Optional.of(
                    new HomeFragmentController((HomeActivity) activity())
            );
        }
        return mController.get();
    }
}
