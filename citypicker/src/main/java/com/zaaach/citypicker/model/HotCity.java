package com.zaaach.citypicker.model;

import com.zaaach.citypicker.CityPicker;

public class HotCity extends City {

    public HotCity(String name, String province, String code) {
        super(name, province, CityPicker.mHotCityText, code);
    }
}
