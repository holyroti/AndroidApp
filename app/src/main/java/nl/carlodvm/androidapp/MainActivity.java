package nl.carlodvm.androidapp;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.FrameTime;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nl.carlodvm.androidapp.Animation.ScalingNode;
import nl.carlodvm.androidapp.Core.FileManager;
import nl.carlodvm.androidapp.Core.LocationManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    private AugmentedImageFragment arFragment;
    private LocationManager locationManager;
    private FileManager fileManager;

    private ScalingNode arrow;

    private Button captureGPSButton1;
    private Button captureGPSButton2;

    private Location loc1;
    private Location loc2;

    private final Map<AugmentedImage, AugmentedNode> augmentedImageMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this))
            return;

        setContentView(R.layout.activity_ux);

        locationManager = new LocationManager(this, this);
        fileManager = new FileManager(this);

        initButtons();

        arrow = new ScalingNode(this, "arrow.sfb");
        arFragment = (AugmentedImageFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdateFrame);
        Session session = null;
        try {
            session = new Session(this);
        } catch (UnavailableArcoreNotInstalledException e) {
            e.printStackTrace();
        } catch (UnavailableApkTooOldException e) {
            e.printStackTrace();
        } catch (UnavailableSdkTooOldException e) {
            e.printStackTrace();
        }
        // IMPORTANT!!!  ArSceneView requires the `LATEST_CAMERA_IMAGE` non-blocking update mode.
        Config config = new Config(session);
        config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE);
        session.configure(config);
        arFragment.getSessionConfiguration(session);
    }

    private void initButtons() {
        captureGPSButton1 = findViewById(R.id.captureGPSButton1);
        captureGPSButton2 = findViewById(R.id.captureGPSButton2);

        captureGPSButton1.setEnabled(false);
        captureGPSButton2.setEnabled(false);

        captureGPSButton1.setOnClickListener(view -> {
            loc1 = locationManager.GetModelGPSLocation(arrow);
            if (loc1 != null) {
                String locationString = loc1.toString();
                Toast.makeText(this, locationString, Toast.LENGTH_SHORT).show();
                Log.e(TAG, locationString);
            }
        });

        captureGPSButton2.setOnClickListener(view -> {
            loc2 = locationManager.GetModelGPSLocation(arrow);
            if (loc2 != null) {
                String locationString = loc2.toString();
                //Toast.makeText(this, locationString, Toast.LENGTH_LONG).show();
                Log.e(TAG, locationString);
                NumberFormat formatter = new DecimalFormat("#0,00");
                Log.e(TAG, "Distance: " + LocationManager.getDistanceBetween(loc1, loc2) +  "m") ;
                Toast.makeText(this, "Distance: " + LocationManager.getDistanceBetween(loc1, loc2) +  "m", Toast.LENGTH_LONG).show();
                fileManager.getFileOutputStream("");
            }
        });
    }

    private void onUpdateFrame(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();

        if (frame == null || frame.getCamera().getTrackingState() != TrackingState.TRACKING)
            return;

        Collection<AugmentedImage> updatedAugmentedImages =
                frame.getUpdatedTrackables(AugmentedImage.class);

        for (AugmentedImage augmentedImage : updatedAugmentedImages) {
            switch (augmentedImage.getTrackingState()) {
                case PAUSED:
                    String text = "Detected Image " + augmentedImage.getIndex();
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();

                    break;
                case TRACKING:
                    if (!augmentedImageMap.containsKey(augmentedImage)) {
                        if(augmentedImage.getIndex() == 0){
                            captureGPSButton1.setEnabled(true);
                        }

                        if(augmentedImage.getIndex() == 1){
                            captureGPSButton2.setEnabled(true);
                        }

                        augmentedImageMap.put(augmentedImage, arrow);

                        arrow.renderNode(augmentedImage, arFragment);

                    }
                    break;
                case STOPPED:
                    augmentedImageMap.remove(augmentedImage);

                    if(augmentedImage.getIndex() == 0){
                        captureGPSButton1.setEnabled(false);
                    }

                    if(augmentedImage.getIndex() == 1){
                        captureGPSButton2.setEnabled(false);
                    }

                    break;
            }
        }

    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGLVersionString = ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                .getDeviceConfigurationInfo()
                .getGlEsVersion();
        if (Double.parseDouble(openGLVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 or later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        return true;
    }
}
