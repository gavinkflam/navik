package hk.gavin.navik.util;

public class FormattingUtility {

    public static String formatDistanceReadable(int distanceInMeter) {
        if (distanceInMeter > 999) {
            // Format in km
            return String.format("%.1f km", distanceInMeter / 1000f);
        }

        return String.format("%d m", distanceInMeter);
    }

    public static String formatDistanceReadableRounded(int distanceInMeter) {
        if (distanceInMeter > 999) {
            return formatDistanceReadable(distanceInMeter);
        }
        else if (distanceInMeter > 249) {
            // Round to nearest 50 m for 250 - 999 m
            return formatDistanceReadable(round(distanceInMeter, 50));
        }
        else if (distanceInMeter > 9) {
            // Round to nearest 10 m for 10 - 249 m
            return formatDistanceReadable(round(distanceInMeter, 10));
        }
        else {
            // Display 10 m for any values under 10 m
            return formatDistanceReadable(10);
        }
    }

    public static String formatTime(float hours) {
        if (hours > 1) {
            // Format in hours
            return String.format("%.1f hours", hours);
        }

        return String.format("%d minutes", (int) (hours * 60));
    }

    public static String formatSpeedReadable(double speedInMeterPerSec) {
        return String.format("%d km/h", Math.round(speedInMeterPerSec * 3.6));
    }

    private static int round(int i, int v) {
        return Math.round(i / v) * v;
    }
}
