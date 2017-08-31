package com.zaaach.citypicker;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：Bro0cL
 * @date: 2017/8/31
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CheckPermissionsActivity extends AppCompatActivity {
    //高德定位所需要的权限
    protected final String[] neededPermissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private static final int REQUEST_CODE = 2333;

    private CheckPermissionsListener mListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void requestPermissions(Activity activity, String[] permissions, CheckPermissionsListener listener){
        if (activity == null) return;
        mListener = listener;
        List<String> deniedPermissions = findDeniedPermissions(activity, permissions);
        if (!deniedPermissions.isEmpty()){
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        }else{
            //所有权限都已经同意了
            mListener.onGranted();
        }
    }

    private List<String> findDeniedPermissions(Activity activity, String... permissions){
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions){
            if (ActivityCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED){
                deniedPermissions.add(permission);
            }
        }
        return deniedPermissions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE:
                List<String> deniedPermissions = new ArrayList<>();
                int length = grantResults.length;
                for (int i = 0; i < length; i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        //该权限被拒绝了
                        deniedPermissions.add(permissions[i]);
                    }
                }
                if (deniedPermissions.size() > 0){
                    mListener.onDenied(deniedPermissions);
                }else{
                    mListener.onGranted();
                }
                break;
            default:
                break;
        }
    }
}
