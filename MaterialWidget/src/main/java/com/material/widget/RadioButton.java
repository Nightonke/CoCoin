package com.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CompoundButton;

/**
 * Created by IntelliJ IDEA.
 * User: keith.
 * Date: 14-10-8.
 * Time: 15:48.
 */
public class RadioButton extends CompoundButton {

    private static final long ANIMATION_DURATION = 200;
    private static final int StateNormal = 1;
    private static final int StateTouchDown = 2;
    private static final int StateTouchUp = 3;

    private int mState = StateNormal;
    private long mStartTime;
    private int mColor;
    private int mCheckedColor;
    private int mRadioWidth;
    private int mRadioHeight;
    private int mBorderRadius;
    private int mThumbRadius;
    private int mRippleRadius;
    private int mStrokeWidth;
    private Rect mFingerRect;
    private boolean mMoveOutside;

    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public RadioButton(Context context) {
        this(context, null);
    }

    public RadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.RadioButton);
        mColor = attributes.getColor(R.styleable.RadioButton_radio_color,
                getResources().getColor(R.color.radio_color));
        mCheckedColor = attributes.getColor(R.styleable.RadioButton_radio_checked_color,
                getResources().getColor(R.color.radio_checked_color));
        attributes.recycle();
        mRadioWidth = getResources().getDimensionPixelSize(R.dimen.radio_width);
        mRadioHeight = getResources().getDimensionPixelSize(R.dimen.radio_height);
        mBorderRadius = getResources().getDimensionPixelSize(R.dimen.radio_border_radius);
        mThumbRadius = getResources().getDimensionPixelSize(R.dimen.radio_thumb_radius);
        mRippleRadius = getResources().getDimensionPixelSize(R.dimen.radio_ripple_radius);
        mStrokeWidth = getResources().getDimensionPixelSize(R.dimen.radio_stroke_width);

        thumbPaint.setColor(mCheckedColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth(widthMeasureSpec),
                MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight(heightMeasureSpec),
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            if (specSize < mRadioWidth) {
                result = mRadioWidth;
            } else {
                result = specSize;
            }
        } else {
            result = mRadioWidth;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            if (specSize < mRadioHeight) {
                result = mRadioHeight;
            } else {
                result = specSize;
            }
        } else {
            result = mRadioHeight;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMoveOutside = false;
                mState = StateTouchDown;
                mFingerRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
                mStartTime = System.currentTimeMillis();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mFingerRect.contains(getLeft() + (int) event.getX(),
                        getTop() + (int) event.getY())) {
                    mMoveOutside = true;
                    mState = StateNormal;
                    mStartTime = System.currentTimeMillis();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!mMoveOutside) {
                    mState = StateTouchUp;
                    setChecked(!isChecked());
                    mStartTime = System.currentTimeMillis();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mState = StateNormal;
                invalidate();
                break;
        }
        return true;
    }

    private int rippleColor(int color) {
        int alpha = Math.round(Color.alpha(color) * 0.2f);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        int rippleRadius = 0;
        int thumbRadius = isChecked() ? mThumbRadius : 0;
        long elapsed = System.currentTimeMillis() - mStartTime;
        switch (mState) {
            case StateNormal:
                break;
            case StateTouchDown:
                ripplePaint.setAlpha(255);
                if (elapsed < ANIMATION_DURATION) {
                    rippleRadius = Math.round(elapsed * mRippleRadius / ANIMATION_DURATION);
                } else {
                    rippleRadius = mRippleRadius;
                }
                postInvalidate();
                break;
            case StateTouchUp:
                if (elapsed < ANIMATION_DURATION) {
                    int alpha = Math.round((ANIMATION_DURATION - elapsed) * 255 / ANIMATION_DURATION);
                    ripplePaint.setAlpha(alpha);
                    rippleRadius = Math.round((ANIMATION_DURATION - elapsed) * mRippleRadius / ANIMATION_DURATION);
                    if (isChecked()) {
                        thumbRadius = Math.round(elapsed * mThumbRadius / ANIMATION_DURATION);
                    } else {
                        thumbRadius = Math.round((ANIMATION_DURATION - elapsed) * mThumbRadius / ANIMATION_DURATION);
                    }
                } else {
                    mState = StateNormal;
                    rippleRadius = 0;
                    ripplePaint.setAlpha(0);
                }
                postInvalidate();
                break;
        }
        if (isChecked()) {
            ripplePaint.setColor(rippleColor(mCheckedColor));

            borderPaint.setColor(mCheckedColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(mStrokeWidth);
        } else {
            ripplePaint.setColor(rippleColor(mColor));

            borderPaint.setColor(mColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(mStrokeWidth);
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, rippleRadius, ripplePaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, borderPaint);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, thumbRadius, thumbPaint);
    }
}
