package hk.gavin.navik.core.navigation;

import android.graphics.Bitmap;

import java.io.Serializable;

public class NKNavigationState implements Serializable {

    public final Bitmap visualAdviceImage;
    public final String currentStreetName;
    public final String nextStreetName;

    public final int distanceToNextAdvice;
    public final int distanceToDestination;

    public final double currentSpeed;

    public NKNavigationState(Bitmap visualAdviceImage, String currentStreetName, String nextStreetName,
                             int distanceToNextAdvice, int distanceToDestination, double currentSpeed) {
        this.visualAdviceImage = visualAdviceImage;
        this.currentStreetName = currentStreetName;
        this.nextStreetName = nextStreetName;
        this.distanceToNextAdvice = distanceToNextAdvice;
        this.distanceToDestination = distanceToDestination;
        this.currentSpeed = currentSpeed;
    }
}
