package hk.gavin.navik.ui.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public abstract class AbstractPresenter {

    public void invalidate() {
        // Do nothing
    }

    public void onCreate() {
        // Do nothing
    }

    public void onActivityCreated(Activity activity) {
        // Do nothing
    }

    public void onStart() {
        // Do nothing
    }

    public void onStop() {
        // Do nothing
    }

    public void onResume() {
        // Do nothing
    }

    public void onPause() {
        // Do nothing
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }
}
