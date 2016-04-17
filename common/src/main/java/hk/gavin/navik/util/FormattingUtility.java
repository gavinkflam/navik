package hk.gavin.navik.util;

public class FormattingUtility {

    public static String formatDistanceReadable(int distanceInMeter) {
        int displayDistance;

        if (distanceInMeter > 999) {
            // Format in km
            return String.format("%.1f km", distanceInMeter / 1000f);
        }
        else if (distanceInMeter > 249) {
            // Round to nearest 50 m for 250 - 999 m
            displayDistance = round(distanceInMeter, 50);
        }
        else if (distanceInMeter > 9) {
            // Round to nearest 10 m for 10 - 249 m
            displayDistance = round(distanceInMeter, 10);
        }
        else {
            // Display 10 m for any values under 10 m
            displayDistance = 10;
        }

        return String.format("%d m", displayDistance);
    }

    public static String formatSpeedReadable(double speedInMeterPerSec) {
        return String.format("%d km/h", Math.round(speedInMeterPerSec * 3.6));
    }

    private static int round(int i, int v) {
        return Math.round(i / v) * v;
    }
}
