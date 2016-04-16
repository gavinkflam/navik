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

    @Bind(R.id.container) BoxInsetLayout mContainer;
    @Bind(R.id.visualAdvisor) ImageView mVisualAdvisor;
    @Bind(R.id.metersToTurn) TextView mMetersToTurn;
    @Bind(R.id.streetName) TextView mStreesName;
    @Bind(R.id.progress) TextView mProgress;

    @BindColor(R.color.colorSafe) int mColorSafe;
    @BindColor(R.color.colorSoon) int mColorSoon;
    @BindColor(R.color.colorImmediate) int mColorImmediate;

    public void displayNavigationState(NKNavigationState navigationState) {
        mVisualAdvisor.setImageBitmap(navigationState.visualAdviceImage.getBitmap());
        mMetersToTurn.setText(String.format("%d m", navigationState.distanceToNextAdvice));
        mStreesName.setText(navigationState.nextStreetName);

        // Determine background color
        if (navigationState.distanceToNextAdvice < 100) {
            mContainer.setBackgroundColor(mColorImmediate);
        }
        else if (navigationState.distanceToNextAdvice < 200) {
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
            displayNavigationState(
                    (NKNavigationState) SerializationUtils.deserialize(messageEvent.getData())
            );
        }
    }
}
