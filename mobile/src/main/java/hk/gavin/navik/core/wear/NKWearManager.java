package hk.gavin.navik.core.wear;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import hk.gavin.navik.contract.WearContract;
import hk.gavin.navik.core.navigation.NKNavigationState;
import org.apache.commons.lang3.SerializationUtils;

import java.util.Set;

public class NKWearManager implements GoogleApiClient.ConnectionCallbacks, ResultCallback<CapabilityApi.GetCapabilityResult>, CapabilityApi.CapabilityListener {

    private GoogleApiClient mGoogleApiClient;
    private String transcriptionNodeId = null;

    public NKWearManager(Context context) {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void startWearActivity() {
        if (transcriptionNodeId != null) {
            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, transcriptionNodeId, WearContract.Path.START_WEAR_ACTIVITY, null
            );
        }
    }

    public void broadcastNavigationState(NKNavigationState navigationState) {
        if (transcriptionNodeId != null) {
            Wearable.MessageApi.sendMessage(
                    mGoogleApiClient, transcriptionNodeId, WearContract.Path.NAVIGATION_STATE,
                    SerializationUtils.serialize(navigationState)
            );
        }
    }

    private void setupNavigationStateCapability() {
        // Find reachable devices with navigation state capability
        Wearable.CapabilityApi.getCapability(
                mGoogleApiClient, WearContract.Compatibility.NAVIGATION_STATE, CapabilityApi.FILTER_REACHABLE
        ).setResultCallback(this);
    }

    private void updateNavigationStateCapability(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();
        transcriptionNodeId = pickBestNodeId(connectedNodes);
    }

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }

    public void onDestroy() {
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        setupNavigationStateCapability();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onResult(@NonNull CapabilityApi.GetCapabilityResult capabilityResult) {
        updateNavigationStateCapability(capabilityResult.getCapability());

        // Add listener for future capability changes
        Wearable.CapabilityApi.addCapabilityListener(
                mGoogleApiClient, this, WearContract.Compatibility.NAVIGATION_STATE
        );
    }

    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
        updateNavigationStateCapability(capabilityInfo);
    }
}
