package hk.gavin.navik.core.location;

import android.content.Context;
import com.orhanobut.logger.Logger;
import com.skobbler.ngx.positioner.SKCurrentPositionListener;
import com.skobbler.ngx.positioner.SKCurrentPositionProvider;
import com.skobbler.ngx.positioner.SKPosition;

public class NKSkobblerLocationProvider extends NKLocationProvider implements SKCurrentPositionListener {

    private final SKCurrentPositionProvider mProvider;

    public NKSkobblerLocationProvider(Context context) {
        super(context);

        mProvider = new SKCurrentPositionProvider(getContext());
        mProvider.setCurrentPositionListener(this);
        mProvider.requestLocationUpdates(true, true, true);
    }

    @Override
    public void onCurrentPositionUpdate(SKPosition skPosition) {
        NKLocation newLocation = NKLocation.fromSKCoordinate(skPosition.getCoordinate());
        double newAccuracy = skPosition.getHorizontalAccuracy();
        Logger.v("location: (%s), accuracy: %f", newLocation, newAccuracy);

        if (isLastLocationAvailable() && newLocation.equals(getLastLocation())) {
            if (newAccuracy != getLastLocationAccuracy()) {
                setLastLocationAccuracy(newAccuracy);
                notifyAccuracyUpdate();
            }
        }
        else {
            setLastLocation(NKLocation.fromSKCoordinate(skPosition.getCoordinate()));
            setLastLocationAccuracy(newAccuracy);
            notifyLocationUpdate();
        }
    }
}
