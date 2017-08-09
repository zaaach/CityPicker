import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.qiwei.citypickerlibrary.R;
import com.qiwei.citypickerlibrary.citypicker.model.LocateState;
import com.qiwei.citypickerlibrary.citypicker.utils.StringUtils;


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
