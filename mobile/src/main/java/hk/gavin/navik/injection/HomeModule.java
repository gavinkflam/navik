package hk.gavin.navik.injection;

import android.app.Activity;
import dagger.Module;
import dagger.Provides;
import hk.gavin.navik.activity.HomeActivity;
import hk.gavin.navik.ui.HomeController;

@Module
public class HomeModule extends ActivityModule {

    private HomeController mController;

    public HomeModule(Activity activity) {
        super(activity);
    }

    @Provides @PerActivity public HomeController controller() {
        if (mController == null) {
            mController = new HomeController((HomeActivity) activity());
        }
        return mController;
    }
}
