package hk.gavin.navik.core.navigation;

import android.graphics.Bitmap;
import com.skobbler.ngx.navigation.SKNavigationState;
import com.skobbler.ngx.sdktools.navigationui.SKToolsUtils;

public class NKSkobblerNavigationUtil {

    public static NKNavigationState createNavigationState(SKNavigationState skState) {
        Bitmap visualAdviceImage = SKToolsUtils.decodeFileToBitmap(skState.getCurrentAdviceVisualAdviceFile());
        String streetName = skState.getCurrentAdviceCurrentStreetName();
        int distanceToNextAdvice = skState.getCurrentAdviceDistanceToAdvice();
        int distanceToDestination = (int) Math.round(skState.getDistanceToDestination());
        double currentSpeed = skState.getCurrentSpeed();

        return new NKNavigationState(
                visualAdviceImage, streetName, distanceToNextAdvice, distanceToDestination, currentSpeed
        );
    }
}
