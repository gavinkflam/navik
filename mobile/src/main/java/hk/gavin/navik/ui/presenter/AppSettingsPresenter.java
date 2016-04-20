package hk.gavin.navik.ui.presenter;

import android.widget.Switch;
import butterknife.Bind;
import butterknife.OnCheckedChanged;
import hk.gavin.navik.R;
import hk.gavin.navik.application.NKBus;

public class AppSettingsPresenter extends AbstractPresenter {

    @Bind(R.id.simulationMode) Switch simulationMode;
    @Bind(R.id.offlineMode) Switch offlineMode;

    @Override
    public void invalidate() {

    }

    @OnCheckedChanged(R.id.simulationMode)
    void changeSimulationMode(boolean checked) {
        // Do nothing
    }

    @OnCheckedChanged(R.id.offlineMode)
    void changeOfflineMode(boolean checked) {
        // Do nothing
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
