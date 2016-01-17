package hk.gavin.navik.injection;

import android.app.Activity;
import com.google.common.base.Optional;
import dagger.Module;
import dagger.Provides;
import hk.gavin.navik.ui.activity.HomeActivity;
import hk.gavin.navik.ui.controller.HomeController;

@Module
public class HomeModule extends ActivityModule {

    private Optional<HomeController> mController = Optional.absent();

    public HomeModule(Activity activity) {
        super(activity);
    }

    @Provides
    public HomeController controller() {
        if (!mController.isPresent()) {
            mController = Optional.of(
                    new HomeController((HomeActivity) activity())
            );
        }
        return mController.get();
    }
}
