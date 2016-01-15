package com.material.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CompoundButton;

/**
 * Created by keith on 14-9-29.
 * User keith
 * Date 14-9-29
 * Time 下午10:08
 */
public class Switch extends CompoundButton {

    private static final String TAG = Switch.class.getSimpleName();
    private static final long ANIMATION_DURATION = 200;
    private static final int StateNormal = 1;
    private static final int StateTouchDown = 2;
    private static final int StateTouchUp = 3;

    private int mState = StateNormal;
    private long mStartTime;
    private int mSwitchWidth;
    private int mSwitchHeight;
    private int mTrackColor;
    private int mTrackCheckedColor;
    private int mThumbColor;
    private int mThumbCheckedColor;
    private int mSwitchThumbRadius;
    private int mSwitchRippleMaxRadius;
    private int mStrokeWidth;
    private int mTrackWidth;

    private Paint trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint thumbFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint thumbStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public Switch(Context context) {
        this(context, null);
    }

    public Switch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Switch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mSwitchWidth = getResources().getDimensionPixelSize(R.dimen.switch_width);
        mSwitchHeight = getResources().getDimensionPixelSize(R.dimen.switch_height);
        mTrackColor = getResources().getColor(R.color.switch_track_color);
        mTrackCheckedColor = getResources().getColor(R.color.switch_track_checked_color);
        mThumbColor = getResources().getColor(R.color.switch_thumb_color);
        mThumbCheckedColor = getResources().getColor(R.color.switch_thumb_checked_color);
        mSwitchThumbRadius = getResources().getDimensionPixelSize(R.dimen.switch_thumb_radius);
        mSwitchRippleMaxRadius = getResources().getDimensionPixelSize(R.dimen.switch_ripple_max_radius);
        mStrokeWidth = getResources().getDimensionPixelSize(R.dimen.switch_stroke_width);
        mTrackWidth = getResources().getDimensionPixelSize(R.dimen.switch_track_width);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth(widthMeasureSpec), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight(heightMeasureSpec), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            if (specSize < mSwitchWidth) {
                result = mSwitchWidth;
            } else {
                result = specSize;
            }
        } else {
            result = mSwitchWidth;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            if (specSize < mSwitchHeight) {
                result = mSwitchHeight;
            } else {
                result = specSize;
            }
        } else {
            result = mSwitchHeight;
        }
        return result;
    }

    private boolean contains(MotionEvent event) {
        if (isChecked()) {
            return event.getX() >= getWidth() - (getWidth() - mSwitchWidth) / 2 - mSwitchRippleMaxRadius * 2
                    && event.getX() <= getWidth() - (getWidth() - mSwitchWidth) / 2
                    && event.getY() >= (getHeight() - mSwitchHeight) / 2
                    && event.getY() <= (getHeight() - mSwitchHeight) / 2 + mSwitchRippleMaxRadius * 2;
        } else {
            return event.getX() >= (getWidth() - mSwitchWidth) / 2
                    && event.getX() <= (getWidth() - mSwitchWidth) / 2 + mSwitchRippleMaxRadius * 2
                    && event.getY() >= (getHeight() - mSwitchHeight) / 2
                    && event.getY() <= (getHeight() - mSwitchHeight) / 2 + mSwitchRippleMaxRadius * 2;
        }
    }

    private int rippleColor(int color) {
        int alpha = Math.round(Color.alpha(color) * 0.3f);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isChecked()) {
            if (contains(event)) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mState = StateTouchDown;
                        invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        mState = StateNormal;
                        setChecked(false);
                        invalidate();
                        break;
                }
            } else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
            }
        } else {
            if (contains(event)) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mState = StateTouchDown;
                        mStartTime = System.currentTimeMillis();
                        invalidate();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        mState = StateNormal;
                        setChecked(true);
                        invalidate();
                        break;
                }
            } else {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
            }
        }
        return true;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (isChecked()) {
            trackPaint.setStrokeWidth(mTrackWidth);
            trackPaint.setColor(mTrackCheckedColor);
            canvas.drawLine((getWidth() - mSwitchWidth) / 2 + mSwitchRippleMaxRadius + mSwitchThumbRadius,
                    getHeight() / 2,
                    getWidth() - mSwitchRippleMaxRadius - mSwitchThumbRadius,
                    getHeight() / 2, trackPaint);

            switch (mState) {
                case StateTouchDown:
                    ripplePaint.setColor(rippleColor(mThumbCheckedColor));
                    canvas.drawCircle(getWidth() - (getWidth() - mSwitchWidth) / 2 - mSwitchRippleMaxRadius,
                            getHeight() / 2,
                            mSwitchRippleMaxRadius,
                            ripplePaint);
                    break;
                case StateTouchUp:
                    break;
                case StateNormal:
                    break;
            }

            thumbFillPaint.setColor(mThumbCheckedColor);
            thumbFillPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() - (getWidth() - mSwitchWidth) / 2 - mSwitchRippleMaxRadius,
                    getHeight() / 2,
                    mSwitchThumbRadius,
                    thumbFillPaint);
        } else {
            trackPaint.setStrokeWidth(mTrackWidth);
            trackPaint.setColor(mTrackColor);
            canvas.drawLine((getWidth() - mSwitchWidth) / 2 + mSwitchRippleMaxRadius + mSwitchThumbRadius,
                    getHeight() / 2,
                    getWidth() - mSwitchRippleMaxRadius - mSwitchThumbRadius,
                    getHeight() / 2, trackPaint);

            switch (mState) {
                case StateTouchDown:
                    ripplePaint.setColor(rippleColor(mThumbColor));
                    canvas.drawCircle((getWidth() - mSwitchWidth) / 2 + mSwitchRippleMaxRadius,
                            getHeight() / 2,
                            mSwitchRippleMaxRadius,
                            ripplePaint);
                    break;
                case StateTouchUp:
                    break;
                case StateNormal:
                    break;
            }
            thumbStrokePaint.setColor(mThumbColor);
            thumbStrokePaint.setStyle(Paint.Style.STROKE);
            thumbStrokePaint.setStrokeWidth(mStrokeWidth);
            canvas.drawCircle((getWidth() - mSwitchWidth) / 2 + mSwitchRippleMaxRadius,
                    getHeight() / 2,
                    mSwitchThumbRadius, thumbStrokePaint);
        }
    }
}
