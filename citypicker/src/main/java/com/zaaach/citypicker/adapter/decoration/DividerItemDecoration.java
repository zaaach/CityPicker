package com.zaaach.citypicker.adapter.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.zaaach.citypicker.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private float dividerHeight;
    private Paint mPaint;

    public DividerItemDecoration(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.cpSectionBackground, typedValue, true);
        mPaint.setColor(context.getResources().getColor(typedValue.resourceId));
        dividerHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.5f, context.getResources().getDisplayMetrics());
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = (int) dividerHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount - 1; i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, right, bottom, mPaint);
        }
    }
}
