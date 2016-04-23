package hk.gavin.navik.background;

import android.content.Intent;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import hk.gavin.navik.contract.WearContract;
import hk.gavin.navik.core.MainActivity;

public class WearMessageListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        switch (messageEvent.getPath()) {
            case WearContract.Path.START_WEAR_ACTIVITY:
            case WearContract.Path.NAVIGATION_STATE: {
                if (MainActivity.isActive()) {
                    return;
                }

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
            }
            case WearContract.Path.STOP_WEAR_ACTIVITY: {
                if (!MainActivity.isActive()) {
                    return;
                }

                sendBroadcast(new Intent(WearContract.Path.STOP_WEAR_ACTIVITY));
            }
            default: {
                super.onMessageReceived(messageEvent);
            }
        }
    }
}
