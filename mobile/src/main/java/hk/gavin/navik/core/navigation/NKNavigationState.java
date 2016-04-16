package hk.gavin.navik.core.navigation;

import android.graphics.Bitmap;
import lombok.Value;

import java.io.Serializable;

@Value
public class NKNavigationState implements Serializable {

    Bitmap visualAdviceImage;
    String currentStreetName;
    String nextStreetName;

    int distanceToNextAdvice;
    int distanceToDestination;

    double currentSpeed;
}
