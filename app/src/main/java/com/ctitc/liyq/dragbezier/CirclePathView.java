package com.ctitc.liyq.dragbezier;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * 画拉伸时的圆和贝塞尔曲线及两圆切线的连接线
 */
public class CirclePathView extends View {
    // 默认定点圆半径
    public static final float DEFAULT_RADIUS = 20;
    private Context mContext;
    private Paint paint;
    private float radius = DEFAULT_RADIUS;
    private float startX = 0;
    private float startY = 0;

    private Path path;

    public CirclePathView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CirclePathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CirclePathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CirclePathView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(8);
        paint.setColor(Color.parseColor("#0099cc"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radius > 5) {
            canvas.drawPath(path, paint);
            canvas.drawCircle(startX, startY, radius, paint);
        }
    }

    public void update(float x, float y) {
        // 两个圆心之间的长度
        float distance = (float) Math.sqrt(Math.pow(y - startY, 2) + Math.pow(x - startX, 2));
        radius = -distance / 8 + DEFAULT_RADIUS;

        // 根据角度算出四边形的四个点
        float offsetX = (float) (radius * Math.sin(Math.atan((y - startY) / (x - startX))));
        float offsetY = (float) (radius * Math.cos(Math.atan((y - startY) / (x - startX))));

        float x1 = startX + offsetX;
        float y1 = startY - offsetY;

        float x2 = x + offsetX;
        float y2 = y - offsetY;

        float x3 = x - offsetX;
        float y3 = y + offsetY;

        float x4 = startX - offsetX;
        float y4 = startY + offsetY;

        path.reset();
        path.moveTo(x1, y1);
        path.quadTo((startX + x) / 2, (startY + y) / 2, x2, y2);
        path.lineTo(x3, y3);
        path.quadTo((startX + x) / 2, (startY + y) / 2, x4, y4);
        path.lineTo(x1, y1);

        invalidate();
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setStartY(float startY) {
        this.startY = startY;
    }

    public float getRadius() {
        return radius;
    }
}
