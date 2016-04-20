package hk.gavin.navik.core.location;

import com.skobbler.ngx.SKCoordinate;

import java.io.Serializable;
import java.util.Locale;

public class NKLocation implements Serializable {

    final double latitude;
    final double longitude;
    final double elevation;

    public NKLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = 0;
    }

    public NKLocation(double latitude, double longitude, double elevation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.elevation = elevation;
    }

    public static NKLocation fromSKCoordinate(SKCoordinate skCoordinate) {
        return new NKLocation(skCoordinate.getLatitude(), skCoordinate.getLongitude());
    }

    public SKCoordinate toSKCoordinate() {
        return new SKCoordinate(longitude, latitude);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }

        NKLocation l2 = (NKLocation) o;
        return (this.latitude == l2.latitude) && (this.longitude == l2.longitude);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "lat: %f, lng: %f", this.latitude, this.longitude);
    }
}
