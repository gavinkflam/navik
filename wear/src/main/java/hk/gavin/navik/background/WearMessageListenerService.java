package hk.gavin.navik.background;

import android.content.Intent;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import hk.gavin.navik.contract.WearContract;
import hk.gavin.navik.core.MainActivity;

public class WearMessageListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (
                messageEvent.getPath().equalsIgnoreCase(WearContract.Path.START_WEAR_ACTIVITY) ||
                        messageEvent.getPath().equalsIgnoreCase(WearContract.Path.NAVIGATION_STATE)
        ) {
            if (MainActivity.isActive()) {
                return;
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }
}
