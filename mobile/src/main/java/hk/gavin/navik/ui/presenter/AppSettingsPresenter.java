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
    @Bind(R.id.offlineMode) Switch offlineMode;

    @Override
    public void invalidate() {
        if (mMainPreferences == null || simulationMode == null) {
            return;
        }

        simulationMode.setChecked(mMainPreferences.getSimulationMode());
        offlineMode.setChecked(mMainPreferences.getOfflineMode());
    }

    @OnCheckedChanged(R.id.simulationMode)
    void changeSimulationMode(boolean checked) {
        mMainPreferences.setSimulationMode(checked);
    }

    @OnCheckedChanged(R.id.offlineMode)
    void changeOfflineMode(boolean checked) {
        mMainPreferences.setOfflineMode(checked);
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
