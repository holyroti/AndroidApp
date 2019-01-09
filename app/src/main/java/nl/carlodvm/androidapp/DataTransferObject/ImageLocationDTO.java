package nl.carlodvm.androidapp.DataTransferObject;

import android.location.Location;

import java.io.Serializable;

public class ImageLocationDTO implements Serializable {
    public int index;
    public LocationDTO location;

    public ImageLocationDTO(Location location, int index) {
        this.location = new LocationDTO(location);
        this.index = index;
    }

}
