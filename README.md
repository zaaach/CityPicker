<p align="center">
<img src="art/header.png">
</p>



# CityPicker

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html) [![API](https://img.shields.io/badge/API-14%2B-yellow.svg?style=flat)](https://android-arsenal.com/api?level=14)

现在使用较多的类似美团、外卖等APP的城市选择界面，**一行代码搞定**，就是这么简单粗暴！！！

#### 主要功能：

-   字母悬浮栏
-   指定热门城市
-   自定义动画效果
-   自定义主题
-   名称或拼音搜索

# Preview

![image](https://github.com/zaaach/CityPicker/raw/master/art/screen1.gif) ![image](https://github.com/zaaach/CityPicker/raw/master/art/screen2.gif) ![image](https://github.com/zaaach/CityPicker/raw/master/art/screen3.gif)

# APK

下载[demo.apk](https://github.com/zaaach/CityPicker/raw/master/art/demo2.0.apk)体验.

# Install

Gradle:

```groovy
implementation 'com.zaaach:citypicker:2.0.0'
```

or Maven:

```xml
<dependency>
  <groupId>com.zaaach</groupId>
  <artifactId>citypicker</artifactId>
  <version>2.0.0</version>
  <type>pom</type>
</dependency>
```

or 下载library手动导入.

# Usage

`CityPicker` 继承于`DialogFragment` ，本身没有定位功能，需要APP自身实现定位。

### 基本使用：

#### Step1:

在`manifest.xml`中给使用`CityPicker` 的`activity`添加主题`android:theme="@style/DefaultCityPickerTheme"`

```xml
<activity android:name=".MainActivity" android:theme="@style/DefaultCityPickerTheme">
  ......
</activity>
```

#### Step2:

```java
new CityPickerBuilder()
        .setFragmentManager(getSupportFragmentManager())	//此方法必须调用
        .setCurrentCity("杭州")		//APP自身已定位的城市
        .setAnimationStyle(anim)	 //自定义动画
        .setHotCities(new String[]{"北京", "上海", "广州", "深圳"})	//指定热门城市
        .setOnPickListener(new OnPickListener() {
              @Override
              public void onPick(int position, String data) {
                      Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
              }})
         .show();
```

### 关于自定义主题：

在`style.xml` 中自定义主题并且继承`DefaultCityPickerTheme` ，别忘了在`manifest.xml` 设置给`activity`。

```xml
<style name="CustomTheme" parent="DefaultCityPickerTheme">
        <item name="cpCancelTextColor">@color/color_green</item>
        <item name="cpSearchCursorDrawable">@color/color_green</item>
        <item name="cpIndexBarNormalTextColor">@color/color_green</item>
        <item name="cpIndexBarSelectedTextColor">@color/color_green</item>
        <item name="cpSectionHeight">@dimen/custom_section_height</item>
        <item name="cpOverlayBackground">@color/color_green</item>
  		......
</style>
```

`CityPicker` 中自定义的所有属性如下，有些属性值必须是引用类型`refrence`，使用时注意。

```xml
<resources>
    <attr name="cpCancelTextSize" format="dimension|reference" />
    <attr name="cpCancelTextColor" format="color|reference" />

    <attr name="cpClearTextIcon" format="reference" />
    <attr name="cpSearchTextSize" format="dimension|reference" />
    <attr name="cpSearchTextColor" format="color|reference" />
    <attr name="cpSearchHintText" format="string|reference" />
    <attr name="cpSearchHintTextColor" format="color|reference" />
    <attr name="cpSearchCursorDrawable" format="reference" />

    <attr name="cpListItemTextSize" format="dimension|reference" />
    <attr name="cpListItemTextColor" format="color|reference" />
    <attr name="cpListItemHeight" format="dimension|reference"/>

    <attr name="cpEmptyIcon" format="reference"/>
    <attr name="cpEmptyIconWidth" format="dimension|reference"/>
    <attr name="cpEmptyIconHeight" format="dimension|reference"/>
    <attr name="cpEmptyText" format="string|reference"/>
    <attr name="cpEmptyTextSize" format="dimension|reference"/>
    <attr name="cpEmptyTextColor" format="color|reference"/>

    <attr name="cpGridItemBackground" format="color|reference"/>
    <attr name="cpGridItemSpace" format="reference"/>
	<!--悬浮栏-->
    <attr name="cpSectionHeight" format="reference"/>
    <attr name="cpSectionTextSize" format="reference" />
    <attr name="cpSectionTextColor" format="reference" />
    <attr name="cpSectionBackground" format="reference" />

    <attr name="cpIndexBarTextSize" format="reference" />
    <attr name="cpIndexBarNormalTextColor" format="reference" />
    <attr name="cpIndexBarSelectedTextColor" format="reference" />
	<!--特写布局-->
    <attr name="cpOverlayWidth" format="dimension|reference"/>
    <attr name="cpOverlayHeight" format="dimension|reference"/>
    <attr name="cpOverlayTextSize" format="dimension|reference"/>
    <attr name="cpOverlayTextColor" format="color|reference"/>
    <attr name="cpOverlayBackground" format="color|reference"/>
</resources>
```

OK，enjoy it~

# Changelog 

#### v2.0

-   项目重构优化，结构更清晰
-   使用RecyclerView