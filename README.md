

# CityPicker

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-9%2B-yellow.svg?style=flat)](https://android-arsenal.com/api?level=9)

现在使用比较多的类似美团等APP的城市选择界面.

3步即可实现，就是这么简单粗暴！

# Gif

![image](https://github.com/zaaach/CityPicker/raw/master/art/screen.gif)

# APK

下载[demo.apk](https://github.com/zaaach/CityPicker/raw/master/art/demo.apk)体验.

# Install

目前没上传，只能
下载library(citypicker)手动导入.

# Usage

### step1:

创建Activity引入**CityPickerFragment**;

```
 cityPickerFragment = new CityPickerFragment();
 getSupportFragmentManager().beginTransaction()
          .add(R.id.fl_activity_city_picker_container, cityPickerFragment).commit();
  
```

### Step2

定位更新当前位置状态

```
cityPickerFragment.updateLocateState(LocateState.SUCCESS, location.replaceAll("市", ""));
```

### Step3
添加到清单文件中，修改软键盘模式。添加高德地图的key
``` 
 <activity
            android:name=".CityPickerActivity"
            android:theme="@style/CityPicker.NoActionBar"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="stateHidden|adjustPan"/>
		
<meta-data android:name="com.amap.api.v2.apikey" android:value="your key"/>
```


# 例子

```
/**
 * 引用例子
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

```
