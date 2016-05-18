package com.ctitc.liyq.dragbezier;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class DragTextView extends TextView {
    private Context mContext;
    private ViewGroup decorView;
    private ViewParent parentView;
    /**
     * 跟随手拖动时绘制的圆和数字
     */
    private DecorViewTextView decorViewTextView;
    /**
     * 画拉伸时的圆和贝塞尔曲线及两圆切线的连接线
     */
    private CirclePathView circleView;
    /**
     * 手指点击的坐标(相对于view自身左上角的横纵坐标)
     */
    float pX = 0;
    float pY = 0;

    public DragTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public DragTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public DragTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DragTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    private void init() {
        decorView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView();
        decorView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (parentView == null) {
                parentView = getScrollableParent();
            }
            // 不拦截点击事件
            parentView.requestDisallowInterceptTouchEvent(true);
            pX = event.getX();
            pY = event.getY();
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (getVisibility() == VISIBLE) {
                setVisibility(INVISIBLE);
                circleView = new CirclePathView(mContext);
                // getRawX()相对于屏幕左上角的横纵坐标
                /**
                 * 设置绘制圆的圆心坐标
                 */
                circleView.setStartX(event.getRawX() - pX + getWidth() / 2);
                circleView.setStartY(event.getRawY() - pY + getHeight() / 2);
                decorView.addView(circleView);

                decorViewTextView = new DecorViewTextView(mContext);
                decorView.addView(decorViewTextView);
            }
            circleView.update(event.getRawX() - pX + getWidth() / 2, event.getRawY() - pY + getHeight() / 2);
            decorViewTextView.update(event.getRawX() - pX, event.getRawY() - pY);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (circleView != null) {
                if (circleView.getRadius() > 5) {// 拉动距离过小，重新显示未读信息view,并清除其他view
                    setVisibility(VISIBLE);
                    startAnimation(shakeAnimation(3));
                    if (decorView != null) {
                        decorView.removeView(circleView);
                        if (decorViewTextView != null) {
                            decorView.removeView(decorViewTextView);
                        }
                    }
                } else {// 拉扯距离可以触发爆炸效果
                    if (decorViewTextView != null) {// 清除随手拖动的未读信息view
                        decorView.removeView(decorViewTextView);
                    }
                    // 创建爆炸效果的ImageView
                    final ImageView imageView = (ImageView) LayoutInflater.from(mContext).inflate(
                            R.layout.clean_image_anim, null, false);
                    decorView.addView(imageView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setX(event.getX() - imageView.getWidth() / 2);
                            imageView.setY(event.getY() - imageView.getHeight() / 2);
                            imageView.setVisibility(View.VISIBLE);
                            AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
                            animationDrawable.start();
                        }
                    });

                }
            }
            parentView.requestDisallowInterceptTouchEvent(false);
        } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
            parentView.requestDisallowInterceptTouchEvent(false);
        }
        return true;
    }

    private ViewGroup getScrollableParent() {
        View target = this;
        while (true) {
            View parent;
            try {
                parent = (View) target.getParent();
            } catch (Exception e) {
                return null;
            }
            if (parent == null)
                return null;
            if (parent instanceof ListView || parent instanceof ScrollView) {
                return (ViewGroup) parent;
            }
            target = parent;
        }

    }

    /**
     * 晃动动画
     *
     * @param counts
     *            1秒钟晃动多少下
     * @return
     */
    public static Animation shakeAnimation(int counts) {
        TranslateAnimation animation = new TranslateAnimation(0, -10, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setRepeatCount(3);
        animation.setDuration(50);
        animation.setRepeatMode(Animation.REVERSE);
        return animation;
    }
}
