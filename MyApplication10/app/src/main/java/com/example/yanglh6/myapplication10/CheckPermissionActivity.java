package com.example.yanglh6.myapplication10;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Created by lenovo on 2016/12/2.
 */

//  新建一个启动的Activity，在这个单独的Activity的onCreate方法里检查是否获取需要的权限，如果没有则直接退出程序
public class CheckPermissionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);
        final Handler mHandler = new Handler();

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                            public void call(Boolean granted){
                if(granted) {
                    Toast.makeText(CheckPermissionActivity.this,"Granted",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CheckPermissionActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CheckPermissionActivity.this,"App will finish in 3 secs...",Toast.LENGTH_SHORT).show();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 3000);
                }
            }
        });
    }
}
