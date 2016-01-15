package com.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by IntelliJ IDEA.
 * User: keith.
 * Date: 14-10-13.
 * Time: 14:30.
 */
public class TrackSlider extends View {

    private static final String TAG = TrackSlider.class.getSimpleName();
    private static final int MAX = 100;

    private Paint mPaint;
    private int mColor;
    private int mTintColor;
    private int mThumbRadius;
    private int mRippleRadius;
    private int mBarHeight;
    private int mMax = MAX;
    private int mProgress = 0;
    private RectF mUncoveredRectF = new RectF();
    private RectF mCoveredRectF = new RectF();

    public TrackSlider(Context context) {
        this(context, null);
    }

    public TrackSlider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrackSlider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TrackSlider);
        mColor = attributes.getColor(R.styleable.TrackSlider_track_slider_color,
                getResources().getColor(R.color.track_slider_color));
        mTintColor = attributes.getColor(R.styleable.TrackSlider_track_slider_tint_color,
                getResources().getColor(R.color.track_slider_tint_color));
        mThumbRadius = attributes.getDimensionPixelSize(R.styleable.TrackSlider_track_slider_thumb_radius,
                getResources().getDimensionPixelSize(R.dimen.track_slider_thumb_radius));
        mRippleRadius = attributes.getDimensionPixelSize(R.styleable.TrackSlider_track_slider_ripple_radius,
                getResources().getDimensionPixelSize(R.dimen.track_slider_ripple_radius));
        mBarHeight = attributes.getDimensionPixelSize(R.styleable.TrackSlider_track_slider_bar_height,
                getResources().getDimensionPixelSize(R.dimen.track_slider_bar_height));

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return mRippleRadius * 4;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return mRippleRadius * 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingTop() + getPaddingBottom();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    protected synchronized void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        calculateBarDrawRect();

        mPaint.setColor(mColor);
        canvas.drawRect(mUncoveredRectF, mPaint);

        mPaint.setColor(mTintColor);
        canvas.drawRect(mCoveredRectF, mPaint);
    }

    private void calculateBarDrawRect() {
        if (mProgress == 0) {
            mUncoveredRectF.left = getPaddingLeft() + mRippleRadius + mThumbRadius;
            mUncoveredRectF.right = getWidth() - getPaddingRight() - mRippleRadius;
            mUncoveredRectF.top = getHeight() / 2.0f + -mBarHeight / 2.0f;
            mUncoveredRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;

            mCoveredRectF = new RectF();
        }
    }
}
