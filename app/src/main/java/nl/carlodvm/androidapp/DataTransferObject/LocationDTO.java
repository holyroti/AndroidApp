package nl.carlodvm.androidapp.DataTransferObject;

import android.location.Location;

import java.io.Serializable;

public class LocationDTO implements Serializable {
    public double latitude;
    public double longitude;
    public double altitude;

    public LocationDTO(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
    }
}
