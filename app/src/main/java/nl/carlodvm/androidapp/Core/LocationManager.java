package nl.carlodvm.androidapp.Core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.ar.core.Pose;
import nl.carlodvm.androidapp.AugmentedNode;
import nl.carlodvm.androidapp.PermissionHelper.LocationPermissionHelper;

//All units for distance are in meters
public class LocationManager implements LocationListener {
    private final long EARTH_RADIUS = 6378137;
    private final double HALF_PI = Math.PI / 2;

    private boolean isGPSEnabled = false;
    private boolean isNetEnabled = false;
    private boolean hasPermission = false;

    private Location m_deviceLocation;
    private SensorManager m_sensorManager;
    private android.location.LocationManager m_locationManager;

    private Context context;
    @SuppressLint("MissingPermission")
    public LocationManager(Context context, Activity activity) {
        this.context = context;

        m_locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        m_sensorManager = new SensorManager(context);

        if (!LocationPermissionHelper.hasLocationPermission(activity)) {
            LocationPermissionHelper.requestLocationPermission(activity);
        } else {
            hasPermission = true;
            checkIfAvailable(context, activity);

            m_locationManager.requestLocationUpdates(isGPSEnabled ? android.location.LocationManager.GPS_PROVIDER : android.location.LocationManager.NETWORK_PROVIDER
                    , 500, 2, this);
        }
    }

    public static double getDistanceBetween(Location l1, Location l2) {
        float[] result = new float[1];
        Location.distanceBetween(l1.getLatitude(), l1.getLongitude(), l2.getLatitude(), l2.getLongitude(), result);
        return result[0];
    }

    public Location GetModelGPSLocation(AugmentedNode node) {
        if (m_deviceLocation == null) {
            Log.e(LocationManager.class.getSimpleName(), "Device location could not be found.");
            return null;
        }

        if (!hasPermission) {
            Toast.makeText(context, "App does not have location permission.", Toast.LENGTH_LONG);
            return null;
        }

        m_sensorManager.updateOrientationAngles();
        float angleBetweenDeviceAndNorth = m_sensorManager.getmOrientationAngles()[0];

        Location modelLoc = new Location(android.location.LocationManager.GPS_PROVIDER);
        Pose position = node.getImage().getCenterPose();

        double calculatedDistanceForLatitude = Math.sin(angleBetweenDeviceAndNorth) * Math.abs(position.tz());
        modelLoc.setLatitude(CalculateLatitudeFromOffset(calculatedDistanceForLatitude
                , isFacingNorth(angleBetweenDeviceAndNorth) ? this::add : this::substract));

        double calculatedDistanceForLongitude = Math.cos(angleBetweenDeviceAndNorth) * Math.abs(position.tz());
        modelLoc.setLongitude(CalculateLongitudeFromOffset(calculatedDistanceForLongitude
                , isFacingEast(angleBetweenDeviceAndNorth) ? this::add : this::substract));

        modelLoc.setAltitude(position.ty() + m_deviceLocation.getAltitude());

        return modelLoc;
    }

    private double CalculateLatitudeFromOffset(double offset, Operation<Double> op) {
        return op.apply(m_deviceLocation.getLatitude(),
                (180 / Math.PI) * (offset / EARTH_RADIUS));

    }

    private double CalculateLongitudeFromOffset(double offset, Operation<Double> op) {
        return op.apply(m_deviceLocation.getLongitude(),
                (180 / Math.PI) * (offset / EARTH_RADIUS) / Math.cos(Math.toRadians(m_deviceLocation.getLatitude())));
    }

    private boolean isFacingNorth(double azimuth) {
        return azimuth > -HALF_PI && azimuth < HALF_PI;
    }

    private boolean isFacingEast(double azimuth) {
        return azimuth > 0 && azimuth < Math.PI;
    }

    private double add(double v1, double v2) {
        return v1 + v2;
    }

    private double substract(double v1, double v2) {
        return v1 - v2;
    }

    private interface Operation<PRIM> {
        PRIM apply(PRIM v1, PRIM v2);
    }

    private void checkIfAvailable(Context context, Activity activity) {
        isGPSEnabled = m_locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
        isNetEnabled = m_locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetEnabled) {
            Toast.makeText(context, "Enable GPS to enable functionality.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        m_deviceLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
