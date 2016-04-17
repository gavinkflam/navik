package hk.gavin.navik.core.navigation;

import android.graphics.Bitmap;
import hk.gavin.navik.core.graphics.BitmapDataObject;

import java.io.Serializable;

public class NKNavigationState implements Serializable {

    public final BitmapDataObject visualAdviceImage;
    public final String currentStreetName;
    public final String nextStreetName;

    public final int distanceToNextAdvice;
    public final int distanceToDestination;

    public final double currentSpeed;

    public NKNavigationState(BitmapDataObject visualAdviceImage, String currentStreetName, String nextStreetName,
                             int distanceToNextAdvice, int distanceToDestination, double currentSpeed) {
        this.visualAdviceImage = visualAdviceImage;
        this.currentStreetName = currentStreetName;
        this.nextStreetName = nextStreetName;
        this.distanceToNextAdvice = distanceToNextAdvice;
        this.distanceToDestination = distanceToDestination;
        this.currentSpeed = currentSpeed;
    }

    public NKNavigationState(Bitmap visualAdviceImageBitmap, String currentStreetName, String nextStreetName,
                             int distanceToNextAdvice, int distanceToDestination, double currentSpeed) {
        this(
                new BitmapDataObject(visualAdviceImageBitmap), currentStreetName, nextStreetName,
                distanceToNextAdvice, distanceToDestination, currentSpeed
        );
    }
}
