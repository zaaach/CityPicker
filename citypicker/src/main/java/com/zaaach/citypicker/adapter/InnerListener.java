package com.zaaach.citypicker.adapter;

import com.zaaach.citypicker.model.City;

public interface InnerListener {
    void dismiss(int position, City data);
    void locate();
}
