package org.gosky;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.zaaach.citypicker.CityPickerFragment;

public class MainActivity extends AppCompatActivity {

    protected FrameLayout fl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_test);
        Log.i("tag", "onCreate: ");
        initView();
        CityPickerFragment cityPickerActivity = new CityPickerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl, cityPickerActivity).commit();
        cityPickerActivity.setOnCityChoseListener(new CityPickerFragment.OnCityChoseListener() {
            @Override
            public void Onclick(String city) {
                Log.i("TAG", "Onclick: " + city);
            }
        });
    }

    private void initView() {
        fl = (FrameLayout) findViewById(R.id.fl);
    }
}
