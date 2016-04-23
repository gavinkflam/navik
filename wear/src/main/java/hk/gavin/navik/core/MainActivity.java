package hk.gavin.navik.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import butterknife.ButterKnife;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import hk.gavin.navik.R;
import hk.gavin.navik.contract.WearContract;
import hk.gavin.navik.core.navigation.NKNavigationState;
import org.apache.commons.lang3.SerializationUtils;

public class MainActivity extends WearableActivity implements
        GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener {

    private static boolean IS_ACTIVE = false;

    private GoogleApiClient mApiClient;
    private Vibrator mVibrator;

    private NavigationStateDecorator mNavigationState;
    private NavigationStateDecorator mPreviousNavigationState;
    private NavigationStatePresenter mPresenter = new NavigationStatePresenter();

    public void setNavigationState(NKNavigationState navigationState) {
        mPreviousNavigationState = mNavigationState;
        mNavigationState = new NavigationStateDecorator(navigationState);
        vibrateIfNecessary();

        mPresenter.setNavigationState(mNavigationState);
        mPresenter.invalidate();
    }

    private void vibrateIfNecessary() {
        NavigationStateDecorator previousNavigationState = mPreviousNavigationState != null ?
                mPreviousNavigationState : mNavigationState;
        TurnLevel notifyTurnLevel;

        if (
                previousNavigationState.object.currentStreetName.equals(mNavigationState.object.currentStreetName) &&
                previousNavigationState.object.distanceToNextAdvice >= mNavigationState.object.distanceToNextAdvice
        ) {
            if (mNavigationState.turnLevel().equals(previousNavigationState.turnLevel())) {
                // Same road, same turn level
                notifyTurnLevel = TurnLevel.Safe;
            }
            else {
                // Same road, different turn level
                notifyTurnLevel = mNavigationState.turnLevel();
            }
        }
        else {
            // New road
            notifyTurnLevel = mNavigationState.turnLevel();
        }

        // Issue vibration notification according to turn level
        switch (notifyTurnLevel) {
            case Immediate: {
                // 3 vibrations
                mVibrator.vibrate(new long[] {0, 200, 250, 200, 250, 200}, -1);
                break;
            }
            case Soon: {
                // 2 vibrations
                mVibrator.vibrate(new long[] {0, 200, 250, 200}, -1);
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IS_ACTIVE = true;
        registerReceiver(mStopReceiverBroadcastReceiver, new IntentFilter(WearContract.Path.STOP_WEAR_ACTIVITY));
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Do not turn off display
        setAmbientEnabled();

        // Initialize view and presenter
        setContentView(R.layout.activity_main);
        ButterKnife.bind(mPresenter, this);
        mPresenter.invalidate();

        // Connect to Google api client
        mApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Wearable.API)
                .build();
        mApiClient.connect();
    }

    @Override
    protected void onStop() {
        IS_ACTIVE = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mStopReceiverBroadcastReceiver);
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
            setNavigationState((NKNavigationState) SerializationUtils.deserialize(messageEvent.getData()));
        }
    }

    public static boolean isActive() {
        return IS_ACTIVE;
    }

    private BroadcastReceiver mStopReceiverBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case WearContract.Path.STOP_WEAR_ACTIVITY: {
                    finish();
                    break;
                }
            }
        }
    };
}
