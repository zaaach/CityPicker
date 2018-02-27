package com.zaaach.citypickerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zaaach.citypicker.CityPickerBuilder;
import com.zaaach.citypicker.adapter.OnPickListener;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private TextView currentTV;
    private CheckBox hotCB;
    private CheckBox animCB;
    private Button themeBtn;

    private static final String KEY = "current_theme";

    private String[] hots;
    private int anim;
    private int theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            theme = savedInstanceState.getInt(KEY);
            setTheme(theme > 0 ? theme : R.style.DefaultCityPickerTheme);
        }

        setContentView(R.layout.activity_main);

        currentTV = findViewById(R.id.tv_current);
        hotCB = findViewById(R.id.cb_hot);
        animCB = findViewById(R.id.cb_anim);
        themeBtn = findViewById(R.id.btn_style);

        if (theme == R.style.DefaultCityPickerTheme){
            themeBtn.setText("默认主题");
        }else if (theme == R.style.CustomTheme){
            themeBtn.setText("自定义主题");
        }

        hotCB.setOnCheckedChangeListener(this);
        animCB.setOnCheckedChangeListener(this);

        themeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (themeBtn.getText().toString().startsWith("自定义")){
                    themeBtn.setText("默认主题");
                    theme = R.style.DefaultCityPickerTheme;
                }else if (themeBtn.getText().toString().startsWith("默认")){
                    themeBtn.setText("自定义主题");
                    theme = R.style.CustomTheme;
                }
                recreate();
            }
        });

        findViewById(R.id.btn_pick).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CityPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setCurrentCity("杭州")
                        .setAnimationStyle(anim)
                        .setHotCities(hots)
                        .setOnPickListener(new OnPickListener() {
                            @Override
                            public void onPick(int position, String data) {
                                currentTV.setText(String.format("当前城市：%s", TextUtils.isEmpty(data) ? "杭州" : data));
                                Toast.makeText(
                                        getApplicationContext(),
                                        "点击的城市：" + data, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.cb_hot:
                hots = isChecked ? new String[]{"北京", "上海", "广州", "深圳", "杭州"} : null;
                break;
            case R.id.cb_anim:
                anim = isChecked ? R.style.CustomAnim : R.style.DefaultCityPickerAnimation;
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY, theme);
    }
}
