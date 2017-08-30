package com.zaaach.citypicker.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * author zaaach on 2015/1/26.
 */
public class WrapHeightGridView extends GridView {
    public WrapHeightGridView(Context context) {
        this(context, null);
    }

    public WrapHeightGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapHeightGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}
