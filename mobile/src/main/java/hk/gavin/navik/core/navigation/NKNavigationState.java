package hk.gavin.navik.core.navigation;

import android.graphics.Bitmap;
import lombok.Value;

@Value
public class NKNavigationState {

    Bitmap visualAdviceImage;
    String streetName;

    int distanceToNextAdvice;
    int distanceToDestination;

    double currentSpeed;
}
