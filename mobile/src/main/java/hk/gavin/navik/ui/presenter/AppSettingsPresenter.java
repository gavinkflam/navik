package hk.gavin.navik.ui.presenter;

import android.widget.Switch;
import butterknife.Bind;
import butterknife.OnCheckedChanged;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;
import hk.gavin.navik.preference.MainPreferences;

import javax.inject.Inject;

public class AppSettingsPresenter extends AbstractPresenter {

    @Inject MainPreferences mMainPreferences;

    @Bind(R.id.simulationMode) Switch simulationMode;

    @Override
    public void invalidate() {
        if (mMainPreferences == null || simulationMode == null) {
            return;
        }

        simulationMode.setChecked(mMainPreferences.getSimulationMode());
    }

    @OnCheckedChanged(R.id.simulationMode)
    void changeSimulationMode(boolean checked) {
        mMainPreferences.setSimulationMode(checked);
    }

    @Override
    public void onResume() {
        NKBus.get().register(this);
    }

    @Override
    public void onPause() {
        NKBus.get().unregister(this);
    }
}
