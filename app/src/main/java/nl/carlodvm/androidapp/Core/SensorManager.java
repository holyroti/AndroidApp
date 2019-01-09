package nl.carlodvm.androidapp.Core;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class SensorManager implements SensorEventListener {
    private final float[] m_AccelerometerReading = new float[3];
    private final float[] m_MagnetometerReading = new float[3];
    private final float[] mRotationMatrix = new float[9];
    private final float[] mOrientationAngles = new float[3];
    private android.hardware.SensorManager m_SensorManager;

    public SensorManager(Context context) {
        m_SensorManager = (android.hardware.SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        Sensor accelerometer = m_SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            m_SensorManager.registerListener(this, accelerometer,
                    android.hardware.SensorManager.SENSOR_DELAY_NORMAL, android.hardware.SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = m_SensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            m_SensorManager.registerListener(this, magneticField,
                    android.hardware.SensorManager.SENSOR_DELAY_NORMAL, android.hardware.SensorManager.SENSOR_DELAY_UI);
        }

    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, m_AccelerometerReading,
                    0, m_AccelerometerReading.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, m_MagnetometerReading,
                    0, m_MagnetometerReading.length);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void updateOrientationAngles() {
        android.hardware.SensorManager.getRotationMatrix(mRotationMatrix, null,
                m_AccelerometerReading, m_MagnetometerReading);

        android.hardware.SensorManager.getOrientation(mRotationMatrix, mOrientationAngles);
    }

    public float[] getmOrientationAngles() {
        return mOrientationAngles;
    }

    public float[] getAccelerometerReading() {
        return m_AccelerometerReading;
    }
}
