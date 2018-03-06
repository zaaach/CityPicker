package com.zaaach.citypicker.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LocateState {
    public static final int LOCATING    = 123;
    public static final int SUCCESS     = 132;
    public static final int FAILURE     = 321;

    @IntDef({SUCCESS, FAILURE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State{}
}
