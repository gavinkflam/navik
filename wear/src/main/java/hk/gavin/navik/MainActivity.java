package hk.gavin.navik;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    private int mColorMode = 0;
    @BindColor(R.color.colorSafe) int mColorSafe;
    @BindColor(R.color.colorSoon) int mColorSoon;
    @BindColor(R.color.colorImmediate) int mColorImmediate;

    @OnClick(R.id.visualAdvisor)
    void changeBackgroundColor() {
        switch (mColorMode) {
            case 0: {
                mContainer.setBackgroundColor(mColorSoon);
                mMetersToTurn.setText("200 m");
                mColorMode = 1;
                break;
            }
            case 1: {
                mContainer.setBackgroundColor(mColorImmediate);
                mMetersToTurn.setText("90 m");
                mColorMode = 2;
                break;
            }
            default: {
                mContainer.setBackgroundColor(mColorSafe);
                mMetersToTurn.setText("300 m");
                mColorMode = 0;
            }
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
            NKNavigationState state = SerializationUtils.deserialize(messageEvent.getData());
        }
    }
}
