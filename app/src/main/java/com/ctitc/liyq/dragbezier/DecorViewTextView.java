package com.ctitc.liyq.dragbezier;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 跟随手拖动时绘制的圆和数字
 */
public class DecorViewTextView extends TextView {
    private Context mContext;

    public DecorViewTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public DecorViewTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public DecorViewTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DecorViewTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    private void init() {
        setLayoutParams(new ViewGroup.LayoutParams((int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20,
                mContext.getResources().getDisplayMetrics())), (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20, mContext.getResources().getDisplayMetrics()))));
        setBackgroundResource(R.drawable.bg);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        setText("1");
        setTextColor(Color.WHITE);
        setGravity(Gravity.CENTER);
        setPadding(5, 2, 5, 2);
    }

    @SuppressLint("NewApi")
    public void update(float x, float y) {
        setX(x);
        setY(y);
        invalidate();
    }
}