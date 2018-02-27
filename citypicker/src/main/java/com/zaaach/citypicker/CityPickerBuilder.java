package com.zaaach.citypicker;

import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.zaaach.citypicker.adapter.OnPickListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Author: Bro0cL
 * @Date: 2018/2/6 17:52
 */
public class CityPickerBuilder {
    private static final String TAG = "CityPicker";

    private FragmentManager mFragmentManager;
    private Fragment mTargetFragment;

    private int mAnimStyle;
    private String mCurrent;
    private ArrayList<String> mHots;
    private OnPickListener mOnPickListener;

    public CityPickerBuilder setFragmentManager(FragmentManager fm) {
        this.mFragmentManager = fm;
        return this;
    }

    public CityPickerBuilder setTargetFragment(Fragment targetFragment) {
        this.mTargetFragment = targetFragment;
        return this;
    }

    /**
     * 设置动画效果
     * @param animStyle
     * @return
     */
    public CityPickerBuilder setAnimationStyle(@StyleRes int animStyle) {
        this.mAnimStyle = animStyle;
        return this;
    }

    /**
     * 设置当前已经定位的城市
     * @param current
     * @return
     */
    public CityPickerBuilder setCurrentCity(String current) {
        this.mCurrent = current;
        return this;
    }

    public CityPickerBuilder setHotCities(ArrayList<String> data){
        this.mHots = data;
        return this;
    }

    public CityPickerBuilder setHotCities(String[] data){
        if (data == null) return this;
        mHots = new ArrayList<>();
        mHots.addAll(Arrays.asList(data));
        return this;
    }

    /**
     * 设置选择结果的监听器
     * @param listener
     * @return
     */
    public CityPickerBuilder setOnPickListener(OnPickListener listener){
        this.mOnPickListener = listener;
        return this;
    }

    public void show(){
        if (mFragmentManager == null){
           throw new UnsupportedOperationException("CityPickerBuilder：method setFragmentManager() must be called.");
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        final Fragment prev = mFragmentManager.findFragmentByTag(TAG);
        if (prev != null){
            ft.remove(prev).commit();
            ft = mFragmentManager.beginTransaction();
        }
        ft.addToBackStack(null);
        final CityPickerDialogFragment cityPickerFragment =
                CityPickerDialogFragment.newInstance(mAnimStyle, mCurrent, mHots);
        cityPickerFragment.setOnPickListener(mOnPickListener);
        if (mTargetFragment != null){
            cityPickerFragment.setTargetFragment(mTargetFragment, 0);
        }
        cityPickerFragment.show(ft, TAG);
    }
}
