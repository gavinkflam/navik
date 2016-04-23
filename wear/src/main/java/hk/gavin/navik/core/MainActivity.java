package hk.gavin.navik.core;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import butterknife.ButterKnife;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import hk.gavin.navik.R;
import hk.gavin.navik.background.NavigationStatePresenter;
import hk.gavin.navik.contract.WearContract;
import hk.gavin.navik.core.navigation.NKNavigationState;
import org.apache.commons.lang3.SerializationUtils;

public class MainActivity extends WearableActivity implements GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener {

    private NavigationStatePresenter mPresenter = new NavigationStatePresenter();

    private GoogleApiClient mApiClient;
    private NKNavigationState mNavigationState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(mPresenter, this);

        setAmbientEnabled();
        mPresenter.invalidate();

        // Connect to Google api client
        mApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Wearable.API)
                .build();
        mApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        mApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.MessageApi.addListener(mApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Do nothing
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getPath().equalsIgnoreCase(WearContract.Path.NAVIGATION_STATE)) {
            mNavigationState = SerializationUtils.deserialize(messageEvent.getData());
            mPresenter.setNavigationState(mNavigationState);
            mPresenter.invalidate();
        }
    }
}
