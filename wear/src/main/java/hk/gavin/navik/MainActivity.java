package hk.gavin.navik;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import hk.gavin.navik.contract.WearContract;
import hk.gavin.navik.core.navigation.NKNavigationState;
import org.apache.commons.lang3.SerializationUtils;

public class MainActivity extends WearableActivity implements GoogleApiClient.ConnectionCallbacks, MessageApi.MessageListener {

    private GoogleApiClient mApiClient;
    private NKNavigationState mNavigationState;

    @Bind(R.id.container) BoxInsetLayout mContainer;
    @Bind(R.id.visualAdvice) ImageView mVisualAdvice;
    @Bind(R.id.distanceToNextAdvice) TextView mDistanceToNextAdvice;
    @Bind(R.id.nextStreetName) TextView mNextStreetName;
    @Bind(R.id.distanceToDestination) TextView mDistanceToDestination;
    @Bind(R.id.currentSpeed) TextView mCurrentSpeed;

    @BindColor(R.color.colorSafe) int mColorSafe;
    @BindColor(R.color.colorSoon) int mColorSoon;
    @BindColor(R.color.colorImmediate) int mColorImmediate;

    public void refreshDisplay() {
        if (mNavigationState == null) {
            return;
        }

        mVisualAdvice.setImageBitmap(mNavigationState.visualAdviceImage.getBitmap());
        mDistanceToNextAdvice.setText(String.format("%d m", mNavigationState.distanceToNextAdvice));
        mNextStreetName.setText(mNavigationState.nextStreetName);
        mDistanceToDestination.setText(String.format("%.1f km", mNavigationState.distanceToDestination / 1000f));
        mCurrentSpeed.setText(String.format("%d km/h", Math.round(mNavigationState.currentSpeed * 3.6)));

        // Determine background color
        if (mNavigationState.distanceToNextAdvice < 100) {
            mContainer.setBackgroundColor(mColorImmediate);
        }
        else if (mNavigationState.distanceToNextAdvice < 200) {
            mContainer.setBackgroundColor(mColorSoon);
        }
        else {
            mContainer.setBackgroundColor(mColorSafe);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setAmbientEnabled();

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
            refreshDisplay();
        }
    }
}
