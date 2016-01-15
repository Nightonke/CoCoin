package com.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

/**
 * Created by IntelliJ IDEA.
 * User: keith.
 * Date: 14-10-10.
 * Time: 14:47.
 */
public class Slider extends View {

    private static final String TAG = Slider.class.getSimpleName();
    private static final int MAX = 4;
    private static final int MIN = 0;
    private static final int StateNormal = 0;
    private static final int StateDragging = 1;

    public static interface OnValueChangeListener {
        void onValueChanged(Slider slider, int value, boolean fromUser);
    }

    private Paint mPaint;
    private int mColor;
    private int mTintColor;
    private int mThumbRadius;
    private int mRippleRadius;
    private int mBarHeight;
    private int mMax;
    private int mProgress;
    private int mThumbBorderWidth;
    private RectF mUncoveredBarRectF = new RectF();
    private RectF mCoveredBarRectF = new RectF();
    private Point mThumbCenter = new Point();
    private Canvas mMinCanvas;
    private Paint mClearPaint;
    private PorterDuffXfermode mPorterDuffXFerMode;
    private float mCoordinateX;
    private int mState = StateNormal;
    private OnValueChangeListener mOnValueChangeListener;

    public void setOnValueChangeListener(OnValueChangeListener listener) {
        this.mOnValueChangeListener = listener;
    }

    public Slider(Context context) {
        this(context, null);
    }

    public Slider(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Slider(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Slider);
        mColor = attributes.getColor(R.styleable.Slider_slider_color,
                getResources().getColor(R.color.slider_color));
        mTintColor = attributes.getColor(R.styleable.Slider_slider_tint_color,
                getResources().getColor(R.color.slider_tint_color));
        mThumbRadius = attributes.getDimensionPixelSize(R.styleable.Slider_slider_thumb_radius,
                getResources().getDimensionPixelSize(R.dimen.slider_thumb_radius));
        mRippleRadius = attributes.getDimensionPixelSize(R.styleable.Slider_slider_ripple_radius,
                getResources().getDimensionPixelSize(R.dimen.slider_thumb_ripple_radius));
        mBarHeight = attributes.getDimensionPixelSize(R.styleable.Slider_slider_bar_height,
                getResources().getDimensionPixelSize(R.dimen.slider_bar_height));
        mThumbBorderWidth = attributes.getDimensionPixelSize(R.styleable.TrackSlider_slider_thumb_border_width,
                getResources().getDimensionPixelSize(R.dimen.slider_thumb_border_width));
        mMax = attributes.getInteger(R.styleable.Slider_slider_max, MAX);
        mProgress = attributes.getInteger(R.styleable.Slider_slider_progress, MIN);

        mCoordinateX = 0.f;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mClearPaint = new Paint();
        mClearPaint.setAntiAlias(true);

        mMinCanvas = new Canvas();
        mPorterDuffXFerMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
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
        super.onTouchEvent(event);
        ViewParent parent = getParent();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                if (event.getX() >= 0 && event.getX() <= getWidth() &&
                        event.getY() >= 0 && event.getY() <= getHeight()) {
                    mCoordinateX = getCoordinateX(event);
                }
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                mState = StateDragging;
                if (event.getX() >= 0 && event.getX() <= getWidth() &&
                        event.getY() >= 0 && event.getY() <= getHeight()) {
                    mCoordinateX = getCoordinateX(event);
                    Log.v("EventX", event.getX() + ":" + mCoordinateX);
                    invalidate();
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                calculateProgress();
                mState = StateNormal;
                invalidate();
            }
            break;
            case MotionEvent.ACTION_CANCEL: {
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
                calculateProgress();
                mState = StateNormal;
                invalidate();
            }
            break;
        }
        return true;
    }

    private float getCoordinateX(MotionEvent event) {
        return ((getWidth() - getPaddingLeft() - getPaddingRight()) * event.getX()) / getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF barRectF = getBarRect();
        switch (mState) {
            case StateNormal: {
                calculateThumbCenterPoint();
                if (mProgress == MIN) {
                    Bitmap bitmap = getMinBitmap(canvas);
                    mMinCanvas.setBitmap(bitmap);

                    mPaint.setColor(mColor);
                    mMinCanvas.drawRect(barRectF, mPaint);

                    mPaint.setColor(mTintColor);
                    mMinCanvas.drawCircle(mThumbCenter.x, mThumbCenter.y, mThumbRadius, mPaint);

                    mClearPaint.setColor(Color.TRANSPARENT);
                    mClearPaint.setXfermode(mPorterDuffXFerMode);
                    mMinCanvas.drawCircle(mThumbCenter.x, mThumbCenter.y, mThumbRadius - mThumbBorderWidth, mClearPaint);

                    canvas.drawBitmap(bitmap, 0, 0, null);
                    bitmap.recycle();
                } else if (mProgress == MAX) {
                    mPaint.setColor(mColor);
                    canvas.drawRect(barRectF, mPaint);

                    mPaint.setColor(mTintColor);
                    canvas.drawRect(getMaxCoveredBarRect(), mPaint);

                    mPaint.setColor(mTintColor);
                    canvas.drawCircle(mThumbCenter.x, mThumbCenter.y, mThumbRadius, mPaint);
                } else {
                    mPaint.setColor(mColor);
                    canvas.drawRect(barRectF, mPaint);

                    mPaint.setColor(mTintColor);
                    canvas.drawRect(getCoveredRectF(mProgress), mPaint);

                    mThumbCenter.set(getThumbCenterX(mProgress), getHeight() / 2);
                    mPaint.setColor(mTintColor);
                    canvas.drawCircle(mThumbCenter.x, mThumbCenter.y, mThumbRadius, mPaint);
                }
            }
            break;
            case StateDragging: {
                mUncoveredBarRectF.left = getPaddingLeft() + mThumbRadius;
                mUncoveredBarRectF.right = getWidth() - getPaddingRight() - mThumbRadius;
                mUncoveredBarRectF.top = getHeight() / 2.0f + -mBarHeight / 2.0f;
                mUncoveredBarRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;

                float realX = getThumbCenterX(mCoordinateX);

                mPaint.setColor(mColor);
                canvas.drawRect(barRectF, mPaint);

                mPaint.setColor(mTintColor);
                canvas.drawRect(getCoveredRectF(realX), mPaint);

                mPaint.setColor(mTintColor);

                mThumbCenter.set((int) realX, getHeight() / 2);
                canvas.drawCircle(mThumbCenter.x, mThumbCenter.y, mThumbRadius, mPaint);
            }
            break;
        }

        /*
        canvas.save();
        canvas.getClipBounds(mCanvasRect);
        mCanvasRect.inset(-mRippleRadius, -mRippleRadius);
        canvas.clipRect(mCanvasRect, Region.Op.REPLACE);

        canvas.restore();

        calculateBarDrawRect();

        calculateThumbCenterPoint();

        if (mProgress == MIN) {
            mMinBitmap = getMinBitmap(canvas);
            mMinCanvas.setBitmap(mMinBitmap);

            mPaint.setColor(mColor);
            mMinCanvas.drawRect(mUncoveredBarRectF, mPaint);

            mPaint.setColor(mTintColor);
            mMinCanvas.drawRect(mCoveredBarRectF, mPaint);

            mPaint.setColor(mTintColor);
            mMinCanvas.drawCircle(mThumbCenter.x, mThumbCenter.y, mThumbRadius, mPaint);

            mClearPaint.setColor(Color.TRANSPARENT);
            mClearPaint.setXfermode(mPorterDuffXFerMode);
            mMinCanvas.drawCircle(mThumbCenter.x, mThumbCenter.y, mThumbRadius - mThumbBorderWidth, mClearPaint);

            canvas.drawBitmap(mMinBitmap, 0, 0, null);
        } else if (mProgress == MAX) {
            mPaint.setColor(mColor);
            canvas.drawRect(mUncoveredBarRectF, mPaint);

            mPaint.setColor(mTintColor);
            canvas.drawRect(mCoveredBarRectF, mPaint);

            mPaint.setColor(mTintColor);
            canvas.drawCircle(mThumbCenter.x, mThumbCenter.y, mThumbRadius, mPaint);
        } else {

        }
        */
    }

    private void calculateProgress() {
        float width = getMaxThumbCenterX() - getMinThumbCenterX();
        float passed = getThumbCenterX(mCoordinateX) - getMinThumbCenterX();
        mProgress = Math.round(passed * mMax / width);
        Log.v("Progress", mProgress + ":" + width + ":" + passed);
    }

    private float getThumbCenterX(float x) {
        if (x < getPaddingLeft() + mThumbRadius) {
            return getMinThumbCenterX();
        } else if (x > getWidth() - getPaddingRight() - mThumbRadius) {
            return getMaxThumbCenterX();
        } else {
            int width = getWidth() - getPaddingLeft() - getPaddingRight() - mThumbRadius * 2;
            return mThumbRadius + getPaddingLeft() + x * width / getWidth();
        }
    }

    private int getThumbCenterX(int progress) {
        if (progress == MIN) {
            return getPaddingLeft() + mThumbRadius;
        } else if (progress == MAX) {
            return getWidth() - getPaddingRight() - mThumbRadius;
        } else {
            float width = getMaxThumbCenterX() - getMinThumbCenterX();
            float passed = mProgress * width / mMax;
            return Math.round(getPaddingLeft() + mThumbRadius + passed);
        }
    }

    private Bitmap getMinBitmap(Canvas canvas) {
        return Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
    }

    private float getMinThumbCenterX() {
        return getPaddingLeft() + mThumbRadius;
    }

    private float getMaxThumbCenterX() {
        return getWidth() - getPaddingRight() - mThumbRadius;
    }

    private RectF getMinCoveredBarRect() {
        if (mCoveredBarRectF == null) {
            mCoveredBarRectF = new RectF();
        }
        mCoveredBarRectF.left = getPaddingLeft() + mThumbRadius;
        mCoveredBarRectF.right = getPaddingLeft() + mThumbRadius;
        mCoveredBarRectF.top = getHeight() / 2.0f + -mBarHeight / 2.0f;
        mCoveredBarRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;
        return mCoveredBarRectF;
    }

    private RectF getMaxCoveredBarRect() {
        if (mCoveredBarRectF == null) {
            mCoveredBarRectF = new RectF();
        }
        mCoveredBarRectF.left = getPaddingLeft() + mThumbRadius;
        mCoveredBarRectF.right = getWidth() - getPaddingRight() - mThumbRadius;
        mCoveredBarRectF.top = getHeight() / 2.0f + -mBarHeight / 2.0f;
        mCoveredBarRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;
        return mCoveredBarRectF;
    }

    private RectF getBarRect() {
        if (mUncoveredBarRectF == null) {
            mUncoveredBarRectF = new RectF();
        }
        mUncoveredBarRectF.left = getPaddingLeft() + mThumbRadius;
        mUncoveredBarRectF.right = getWidth() - getPaddingRight() - mThumbRadius;
        mUncoveredBarRectF.top = getHeight() / 2.0f + -mBarHeight / 2.0f;
        mUncoveredBarRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;
        return mUncoveredBarRectF;
    }

    private RectF getCoveredRectF(float x) {
        if (mCoveredBarRectF == null) {
            mCoveredBarRectF = new RectF();
        }
        mCoveredBarRectF.left = getPaddingLeft() + mThumbRadius;
        mCoveredBarRectF.right = x;
        mCoveredBarRectF.top = getHeight() / 2.0f + -mBarHeight / 2.0f;
        mCoveredBarRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;
        return mCoveredBarRectF;
    }

    private RectF getCoveredRectF(int progress) {
        if (mCoveredBarRectF == null) {
            mCoveredBarRectF = new RectF();
        }
        mCoveredBarRectF.left = getPaddingLeft() + mThumbRadius;
        mCoveredBarRectF.right = getThumbCenterX(progress);
        mCoveredBarRectF.top = getHeight() / 2.0f + -mBarHeight / 2.0f;
        mCoveredBarRectF.bottom = getHeight() / 2.0f + mBarHeight / 2.0f;
        return mCoveredBarRectF;
    }

    private void calculateThumbCenterPoint() {
        if (mProgress == MIN) {
            mThumbCenter.set(mThumbRadius, getHeight() / 2);
        } else if (mProgress == mMax) {
            mThumbCenter.set(getWidth() - getPaddingRight() - mThumbRadius, getHeight() / 2);
        }
    }
}
