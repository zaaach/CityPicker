# CityPicker

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)
[![API](https://img.shields.io/badge/API-9%2B-yellow.svg?style=flat)](https://android-arsenal.com/api?level=14)

现在使用比较多的类似美团等APP的城市选择界面.

2步即可实现，就是这么简单粗暴！

# Gif

![image](https://github.com/zaaach/CityPicker/raw/master/art/screen.gif)

# APK

下载[demo.apk](https://github.com/zaaach/CityPicker/raw/master/art/demo.apk)体验.

# Install

Gradle:

```groovy
compile 'com.zaaach:citypicker:1.2'
```

or Maven:

```xml
<dependency>
  <groupId>com.zaaach</groupId>
  <artifactId>citypicker</artifactId>
  <version>1.2</version>
  <type>pom</type>
</dependency>
```

or 下载library手动导入.

# Usage

`CityPicker`本身已经引入了高德地图定位sdk.

### Step1:

在你项目的`manifest.xml`中添加开发平台申请的key

```xml
<meta-data android:name="com.amap.api.v2.apikey"
           android:value="your key"/>
```
还需要添加`CityPickerActivity`

```xml
<activity
            android:name="com.zaaach.citypicker.CityPickerActivity"
            android:theme="@style/CityPicker.NoActionBar"
            android:screenOrientation="portrait"
            android:windowSoftInputMode="stateHidden|adjustPan"/>
```

### Step2:

```java
private static final int REQUEST_CODE_PICK_CITY = 0;
//启动
startActivityForResult(new Intent(MainActivity.this, CityPickerActivity.class),
                        REQUEST_CODE_PICK_CITY);

//重写onActivityResult方法
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE_PICK_CITY && resultCode == RESULT_OK){
        if (data != null){
            String city = data.getStringExtra(CityPickerActivity.KEY_PICKED_CITY);
            resultTV.setText("当前选择：" + city);
        }
    }
}
```

### Step3:

enjoy it.

# Proguard

注意混淆

```java
//定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
```
# Changelog 

###### v1.2

-   `CityPicker` 已处理6.0运行时权限，具体查看[CheckPermissionsActivity](https://github.com/zaaach/CityPicker/blob/city-picker/citypicker/src/main/java/com/zaaach/citypicker/CheckPermissionsActivity.java)

-   标题栏移除
-   高德定位sdk更新
-   demo可正常定位