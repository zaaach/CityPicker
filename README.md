

# CityPicker

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-9%2B-yellow.svg?style=flat)](https://android-arsenal.com/api?level=9)

现在使用比较多的类似美团等APP的城市选择界面.

2步即可实现，就是这么简单粗暴！

# Gif

![image](https://github.com/zaaach/CityPicker/raw/master/art/screen.gif)

# APK

下载[demo.apk](https://github.com/zaaach/CityPicker/raw/master/art/demo.apk)体验.

# Install

目前没上传，只能
下载library手动导入.

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

# 例子

```
/**
 * 引用例子
 */
public class DemoActivity extends AppCompatActivity {

    Toolbar toolbar;

    FrameLayout flCityPickerContainer;

    private CityPickerFragment cityPickerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);
        initView();
    }


    protected void initView() {
    
    //这里
        cityPickerFragment = new CityPickerFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_activity_city_picker_container, cityPickerFragment).commit();


        //定位
        LocationManager.startSingleLocation(new IMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {

                if (aMapLocation.getErrorCode() == 0) {
                    String city = aMapLocation.getCity();
                    String district = aMapLocation.getDistrict();
                    String location = StringUtils.extractLocation(city, district);

                    //定位成功，更新状态
                    cityPickerFragment.updateLocateState(LocateState.SUCCESS, location.replaceAll("市", ""));
                } else {
                    cityPickerFragment.updateLocateState(LocateState.FAILED, null);

                }


            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //销毁定位
        LocationManager.stopSingleLocation();
    }
}


```
