package nl.carlodvm.androidapp.PermissionHelper;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionHelper {
    private static int ALL_PERMISSIONS = 1;
    private static String[] mPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private static boolean hasPermissions(Activity activity) {
        for (String perm : mPermissions) {
            if (ContextCompat.checkSelfPermission(activity, perm) == PackageManager.PERMISSION_DENIED)
                return false;
        }
        return true;
    }

    private static void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, mPermissions, ALL_PERMISSIONS);
    }

    public synchronized static boolean checkPermissions(Activity activity) {
        if (!hasPermissions(activity)) {
            requestLocationPermission(activity);
            return false;
        }
        return true;
    }
}
