//package com.example.yanglh6.myapplication10;

//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.location.LocationProvider;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.widget.Toast;
//
///**
// * Created by lenovo on 2016/12/2.
// */
////
////
//public class SensorUtils extends Activity implements SensorEventListener {
//    private SensorManager mSensorManager;
////    private LocationManager locationManager;
//    private Sensor mMagneticSensor;
//    private Sensor mAccelerometerSensor;
//
////    double latitude;
////    double longitude;
////
////    Criteria criteria = new Criteria();
////    // 获取最佳服务对象
////    String provider = locationManager.getBestProvider(criteria,true);
////
////    /* GPS Constant Permission */
////    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
////    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;
////
////    /* Position */
////    private static final int MINIMUM_TIME = 10000;  // 10s
////    private static final int MINIMUM_DISTANCE = 50; // 50m
////
////    /* GPS */
////    private String mProviderName;
////    private LocationManager mLocationManager;
////
//
//    @Override
//    public final void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//
////        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////
////        // Get the best provider between gps, network and passive
////        mProviderName = mLocationManager.getBestProvider(criteria, true);
////
////        // API 23: we have to check if ACCESS_FINE_LOCATION and/or ACCESS_COARSE_LOCATION permission are granted
////        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
////                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
////
////            // No one provider activated: prompt GPS
////            if (mProviderName == null || mProviderName.equals("")) {
////                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
////            }
////
////            // At least one provider activated. Get the coordinates
//////            switch (mProviderName) {
//////                case "passive":
//////                    mLocationManager.requestLocationUpdates(mProviderName, MINIMUM_TIME, MINIMUM_DISTANCE, this);
//////                    Location location = mLocationManager.getLastKnownLocation(mProviderName);
//////                    break;
//////
//////                case "network":
//////                    break;
//////
//////                case "gps":
//////                    break;
//////
//////            }
////
////            // One or both permissions are denied.
////        } else {
////
////            // The ACCESS_COARSE_LOCATION is denied, then I request it and manage the result in
////            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
////            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
////                ActivityCompat.requestPermissions(this,
////                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
////                        MY_PERMISSION_ACCESS_COARSE_LOCATION);
////            }
////            // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
////            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
////            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
////                ActivityCompat.requestPermissions(this,
////                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
////                        MY_PERMISSION_ACCESS_FINE_LOCATION);
////            }
////
////        }
////
//
//
//
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
////        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//
//        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//
//
//    }
//
//    @Override
//    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
//        // Do something here if sensor accuracy changes.
//    }
//
//    @Override
//    public final void onSensorChanged(SensorEvent event) {
//        // The light sensor returns a single value.
//        // Many sensors return 3 values, one for each axis.
////        float lux = event.values[0];
//        // Do something with this sensor value.
//    }
//
//    // sensor event listener
//    private SensorEventListener mSensorEventListener = new SensorEventListener() {
//        float[] accValues = null;
//        float[] magValues = null;
//        long lastShakeTime = 0;
//        @Override
//        public void onSensorChanged(SensorEvent event) {
//            // 判断当前是加速度传感器还是地磁传感器
//            switch (event.sensor.getType()) {
//                case Sensor.TYPE_ACCELEROMETER:
//                    // do something about values of accelerometer
//                    // 通过clone()获取不同的values引用
//                    accValues = event.values.clone();
//                    break;
//                case Sensor.TYPE_MAGNETIC_FIELD:
//                   // do something about values of magnetic field
//                    magValues = event.values.clone();
//                    break;
//                default:
//                    break;
//            }
//            //获取地磁与加速度传感器组合的旋转矩阵
//            float[] R = new float[9];
//            float[] values = new float[3];
//            SensorManager.getRotationMatrix(R, null, accValues, magValues);
//            SensorManager.getOrientation(R, values);
//            float newRotationDegree = (float)Math.toDegrees(values[0]);
//
//
//            Intent mIntent = new Intent(this,MainActivity.class);
//            Bundle mBundle = new Bundle();
//            mBundle.putSerializable(SER_KEY,mPerson);
//            mIntent.putExtras(mBundle);
//
//            startActivity(mIntent);
//
//        }
//
//        @Override
//        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
//    };
//
////    private LocationListener mLocationListener = new LocationListener() {
////        private boolean isRemove = false;//判断网络监听是否移除
////
////        @Override
////        public void onLocationChanged(Location location) {
////            if (location != null) {
////                latitude = location.getLatitude();
////                longitude= location.getLongitude();
////            } else {
////            }
////// processing new location
////            // TODO Auto-generatedmethod stub
//////            boolean flag = betterLocation.isBetterLocation(location,
//////                    currentBestLocation);
//////
//////            if (flag) {
//////                currentBestLocation = location;
//////                updateLocation(currentBestLocation);
//////            }
//////            // 获得GPS服务后，移除network监听
//////            if (location !=null && !isRemove) {
//////                locationManager.removeUpdates(networkListener);
//////                isRemove = true;
//////            }
////        }
////        public void onStatusChanged(String provider, int status, Bundle extras) {
//////            if (LocationProvider.OUT_OF_SERVICE == status) {
//////                Toast.makeText(SensorUtils.this,"GPS服务丢失,切换至网络定位",
//////                        Toast.LENGTH_SHORT).show();
//////                locationManager
//////                        .requestLocationUpdates(
//////                                LocationManager.NETWORK_PROVIDER, 0, 0,
//////                                networkListener);
//////            }
////        }
////        public void onProviderEnabled(String provider) {}
////        public void onProviderDisabled(String provider) {}
////    };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
////            ActivityCompat.requestPermissions(this,
////                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
////                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
////        }
////        // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
////        // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
////            ActivityCompat.requestPermissions(this,
////                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
////                    MY_PERMISSION_ACCESS_FINE_LOCATION);
////        }
//
//// register magnetic and accelerometer sensor into sensor manager (onResume
//        mSensorManager.registerListener(mSensorEventListener, mMagneticSensor,
//                SensorManager.SENSOR_DELAY_GAME);
//        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor,
//                SensorManager.SENSOR_DELAY_GAME);
//        // register location update listener
////        locationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // unregister sensors (onPause
//        mSensorManager.unregisterListener(mSensorEventListener);
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
////            ActivityCompat.requestPermissions(this,
////                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
////                    MY_PERMISSION_ACCESS_COARSE_LOCATION);
////        }
////        // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
////        // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
////        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
////            ActivityCompat.requestPermissions(this,
////                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
////                    MY_PERMISSION_ACCESS_FINE_LOCATION);
////        }
////        // unregister update listener
////        locationManager.removeUpdates(mLocationListener);
//
//    }
//
////    @Override
////    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
////        switch (requestCode) {
////            case MY_PERMISSION_ACCESS_COARSE_LOCATION: {
////                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                    // permission was granted
////                } else {
////                    // permission denied
////                }
////                break;
////
////                case MY_PERMISSION_ACCESS_FINE_LOCATION: {
////                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                        // permission was granted
////                    } else {
////                        // permission denied
////                    }
////                    break;
////                }
////
////            }
////        }
//    }


