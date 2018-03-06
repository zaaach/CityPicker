package com.zaaach.citypicker.adapter;

import com.zaaach.citypicker.model.City;

public interface OnPickListener {
    void onPick(int position, City data);
    void onLocate();
}
