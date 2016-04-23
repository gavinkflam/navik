package hk.gavin.navik.ui.fragment;

import android.os.Bundle;
import android.view.View;
import hk.gavin.navik.R;
import hk.gavin.navik.ui.presenter.AppSettingsPresenter;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "m")
public class SettingFragment extends AbstractHomeUiFragment {

    AppSettingsPresenter mAppSettingsPresenter = new AppSettingsPresenter();

    @Getter private final int mLayoutResId = R.layout.fragment_setting;

    public SettingFragment() {
        setHasOptionsMenu(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Inject dependencies for presenters
        component().inject(mAppSettingsPresenter);
        mAppSettingsPresenter.invalidate();

        // Update title and back button display
        getController().setActionBarTitle(R.string.settings);
        getController().setDisplayHomeAsUp(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAppSettingsPresenter.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
