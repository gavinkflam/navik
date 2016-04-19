package hk.gavin.navik.ui.presenter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import com.google.common.base.Optional;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public abstract class AbstractPresenter {

    @Getter private Optional<Context> mContext = Optional.absent();

    public void setContext(Context context) {
        mContext = Optional.of(context);
    }

    public void onCreate() {
        // Do nothing
    }

    public void onStart() {
        // Do nothing
    }

    public void onStop() {
        // Do nothing
    }

    public void onPause() {
        // Do nothing
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }
}
