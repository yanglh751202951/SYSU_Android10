package com.example.yanglh6.myapplication10;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;

//import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity {
    //  获取相应类型的组件
    private Sensor mMagneticSensor;
    private Sensor mAccelerometerSensor;

    private SensorManager mSensorManager;
    private LocationManager mLocationManager;

    private MapView mMapView;
    private ToggleButton toggleButton;

    //  初始化位置服务中的一个位置的数据来源
    private String provider;

    float currentX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //  获取传感器的管理器
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //  手机的方向信息是通过地磁传感器和加速度传感器共同计算出来的(获取地磁传感器和加速度传感器的信息)
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mMapView = (MapView) findViewById(R.id.bmapView);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        //  对Android版本做兼容处理，对于Android 6及以上版本需要向用户请求授权，而低版本的则直接调用
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        //  在判断使用哪个Provider的时候，首先使用isProviderEnabled函数判断该Provider是否已经启用，从而选取一个合适的Provider
        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(MainActivity.this, "检测到网络不可用~", Toast.LENGTH_SHORT).show();
        }

        Location location = mLocationManager.getLastKnownLocation(provider);
        mLocationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);

        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.pointer), 100, 100, true);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap);

        mMapView.getMap().setMyLocationEnabled(true);
        //  使用百度地图里 MyLocationConfiguration 添加当前所在位置的箭头
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptor
        );
        mMapView.getMap().setMyLocationConfigeration(configuration);

        LatLng desLatLng = convertDesLatLng(location);
        //  根据用户方向以及位置构造MyLocationData，相应设置箭头的方向与位置
        MyLocationData.Builder builder = new MyLocationData.Builder();
        builder.latitude(desLatLng.latitude);
        builder.longitude(desLatLng.longitude);
        builder.direction(currentX);
        mMapView.getMap().setMyLocationData(builder.build());

        //  使用MapStatus实现在地图中使某个坐标居中
        MapStatus mapStatus = new MapStatus.Builder().target(desLatLng).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
        mMapView.getMap().setMapStatus(mapStatusUpdate);

        //  实现BaiduMap中的setOnMapTouchListener 函数来实现手动拖动地图平移之后按钮的变化
        mMapView.getMap().setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        toggleButton.setChecked(false);
                        break;
                    default:
                        break;
                }
            }
        });

        //  开关ToggleButton的点击事件
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButton.isChecked()) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    Location location = mLocationManager.getLastKnownLocation(provider);
                    LatLng desLatLng = convertDesLatLng(location);

                    //  根据用户方向以及位置构造MyLocationData，相应设置箭头的方向与位置
                    MyLocationData.Builder builder = new MyLocationData.Builder();
                    builder.latitude(desLatLng.latitude);
                    builder.longitude(desLatLng.longitude);
                    builder.direction(currentX);
                    mMapView.getMap().setMyLocationData(builder.build());

                    //  使用MapStatus实现在地图中使某个坐标居中
                    MapStatus mapStatus = new MapStatus.Builder().target(desLatLng).build();
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                    mMapView.getMap().setMapStatus(mapStatusUpdate);
                } else {

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //  在Activity在前台运行（ onResume ）的时候注册传感器，在注册传感器的时候，需要指定传感器的监听器
        mSensorManager.registerListener(mSensorEventListener, mMagneticSensor,
                SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_UI);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.requestLocationUpdates(provider, 0, 0, mLocationListener);

        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //  在Activity在前台运行（onResume）的时候注册传感器，在离开前台（onPause）的时候取消注册
        mSensorManager.unregisterListener(mSensorEventListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocationManager.removeUpdates(mLocationListener);

        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    // sensor event listener
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        float[] accValues = new float[3];
        float[] magValues = new float[3];
        float Degree;

        //  当传感器数据更新的时候，系统会回调监听器里的onSensorChange函数，便可以在这里对传感器数据进行相应处理
        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    accValues = event.values;
                    //  使用加速度传感器可以实现了检测手机的摇一摇功能，通过摇一摇，弹出是否退出应用的对话框，选择是则退出应用
                    int value = 19;
                    if (Math.abs(accValues[0]) > value || Math.abs(accValues[1]) > value || Math.abs(accValues[2]) > value) {
                        AlertDialog.Builder message = new AlertDialog.Builder(MainActivity.this);
                        message.setTitle("退出当前应用");
                        message.setMessage("确定退出当前应用吗？");
                        message.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MainActivity.this.finish();
                            }
                        });
                        message.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        message.create().show();
                    }
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    magValues = event.values;
                    break;
                default:
                    break;
            }
            Degree = 0;
            if (accValues != null && magValues != null) {
                //  首先通过 getRotationMatrix 得到一个旋转矩阵，然后使用getOrientation 得到手机的朝向
                float[] R = new float[9];
                float[] values = new float[3];
                SensorManager.getRotationMatrix(R, null, accValues, magValues);
                SensorManager.getOrientation(R, values);
                Degree = (float) Math.toDegrees(values[0]);
                currentX = Degree;

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                LatLng desLatLng = convertDesLatLng(mLocationManager.getLastKnownLocation(provider));
                //  根据用户方向以及位置构造MyLocationData，相应设置箭头的方向与位置
                MyLocationData.Builder builder = new MyLocationData.Builder();
                builder.latitude(desLatLng.latitude);
                builder.longitude(desLatLng.longitude);
                builder.direction(currentX);
                mMapView.getMap().setMyLocationData(builder.build());
                if (toggleButton.isChecked()) {
                    //  使用MapStatus实现在地图中使某个坐标居中
                    MapStatus mapStatus = new MapStatus.Builder().target(desLatLng).build();
                    MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                    mMapView.getMap().setMapStatus(mapStatusUpdate);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private LocationListener mLocationListener = new LocationListener() {
        //  注册 LocationListener 监听位置信息的更新并作出相应的处理
        public void onLocationChanged(Location location) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            LatLng desLatLng = convertDesLatLng(mLocationManager.getLastKnownLocation(provider));
            //  根据用户方向以及位置构造MyLocationData，相应设置箭头的方向与位置
            MyLocationData.Builder builder = new MyLocationData.Builder();
            builder.latitude(desLatLng.latitude);
            builder.longitude(desLatLng.longitude);
            builder.direction(currentX);
            mMapView.getMap().setMyLocationData(builder.build());
            if (toggleButton.isChecked()) {
                //  使用MapStatus实现在地图中使某个坐标居中
                MapStatus mapStatus = new MapStatus.Builder().target(desLatLng).build();
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mapStatus);
                mMapView.getMap().setMapStatus(mapStatusUpdate);
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };

    private LatLng convertDesLatLng(Location location) {
        //  进行坐标系的转换,最后LatLng得到的经纬度为百度地图坐标系下的经纬度
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        converter.coord(new LatLng(location.getLatitude(), location.getLongitude()));
        return converter.convert();
    }
}
