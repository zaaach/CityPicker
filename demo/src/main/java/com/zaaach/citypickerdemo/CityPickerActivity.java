package com.zaaach.citypickerdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.zaaach.citypicker.CityPickerFragment;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.utils.StringUtils;

/**
 * Created by rhm on 2017/8/26.
 */

public class CityPickerActivity extends AppCompatActivity {

    private FrameLayout flCityPickerContainer;
    private CityPickerFragment cityPickerFragment;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);
        initView();
    }

    private void initView() {
        cityPickerFragment = new CityPickerFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_activity_city_container, cityPickerFragment).commit();
        initLocationListener();
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //启动定位
        mLocationClient.startLocation();

    }

    private void initLocationListener() {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    String city = aMapLocation.getCity();
                    String district = aMapLocation.getDistrict();
                    String location = StringUtils.extractLocation(city, district);
                    Toast.makeText(CityPickerActivity.this,city,Toast.LENGTH_SHORT).show();
                    //定位成功，更新状态
                    cityPickerFragment.updateLocateState(LocateState.SUCCESS, location.replaceAll("市", ""));
                } else {
                    cityPickerFragment.updateLocateState(LocateState.FAILED, null);
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁定位
        mLocationClient.stopLocation();
    }
}

