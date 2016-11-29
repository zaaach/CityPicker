# CityPicker
仿美团等选择城市列表demo

###效果图.gif
![效果图.gif](https://github.com/zaaach/CityPicker/raw/master/screenshot/screenshot.gif)

###使用方法
#### 1. Add the JitPack repository to your build file
```java
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
  ```
#### 2. Add the dependency

```java
dependencies {
	        compile 'com.github.GoSkyer:CityPicker:0.4'
	}
  ```
#### 3. 修改高德地图apikey 为自己的
```java
defaultConfig {
       .....
        manifestPlaceholders = [
                // 这里需要换成:你的AppId
                "amapkey": "****",
        ]
    }

```

#### 4.界面为CityPickerFragment,引入即可。

#### 5.城市选择事件回调

```java
cityPickerfragment.setOnCityChoseListener(new CityPickerFragment.OnCityChoseListener() {
            @Override
            public void Onclick(String city) {
                Log.i("TAG", "Onclick: " + city);
            }
        });
```

