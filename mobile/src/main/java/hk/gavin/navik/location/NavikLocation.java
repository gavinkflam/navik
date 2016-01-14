package hk.gavin.navik.location;

import com.skobbler.ngx.SKCoordinate;

public class NavikLocation {

    final double latitude;
    final double longitude;

    public NavikLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static NavikLocation fromSKCoordinate(SKCoordinate skCoordinate) {
        return new NavikLocation(skCoordinate.getLatitude(), skCoordinate.getLongitude());
    }

    public SKCoordinate toSKCoordinate() {
        return new SKCoordinate(longitude, latitude);
    }
}
