package com.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CompoundButton;

/**
 * Created by IntelliJ IDEA.
 * User: keith.
 * Date: 14-10-8.
 * Time: 15:49.
 */
public class CheckBox extends CompoundButton {

    private static final long ANIMATION_DURATION = 200;
    private static final float MarkRatio = 0.3f;
    private static final int StateNormal = 1;
    private static final int StateTouchDown = 2;
    private static final int StateTouchUp = 3;

    private int mState = StateNormal;
    private long mStartTime;
    private int mColor;
    private int mCheckedColor;
    private int mMarkColor;
    private int mCheckBoxWidth;
    private int mCheckBoxHeight;
    private int mCornerRadius;
    private int mThumbSize;
    private int mBorderWidth;
    private int mMarkWidth;
    private int mRippleRadius;
    private int mMarkLeftRightPadding;
    private int mMarkTopPadding;
    private int mMarkBottomPadding;
    private Rect mFingerRect;
    private boolean mMoveOutside;

    private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CheckBox(Context context) {
        this(context, null);
    }

    public CheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CheckBox);
        mColor = attributes.getColor(R.styleable.CheckBox_checkbox_color,
                getResources().getColor(R.color.checkbox_color));
        mCheckedColor = attributes.getColor(R.styleable.CheckBox_checkbox_checked_color,
                getResources().getColor(R.color.checkbox_checked_color));
        attributes.recycle();
        mMarkColor = getResources().getColor(R.color.checkbox_mark_color);
        mCheckBoxWidth = getResources().getDimensionPixelSize(R.dimen.checkbox_width);
        mCheckBoxHeight = getResources().getDimensionPixelSize(R.dimen.checkbox_height);
        mCornerRadius = getResources().getDimensionPixelSize(R.dimen.checkbox_corner_radius);
        mThumbSize = getResources().getDimensionPixelSize(R.dimen.checkbox_thumb_size);
        mBorderWidth = getResources().getDimensionPixelSize(R.dimen.checkbox_border_width);
        mMarkWidth = getResources().getDimensionPixelSize(R.dimen.checkbox_mark_width);
        mRippleRadius = getResources().getDimensionPixelSize(R.dimen.checkbox_ripple_radius);
        mMarkLeftRightPadding = getResources().getDimensionPixelSize(R.dimen.checkbox_mark_left_right_padding);
        mMarkTopPadding = getResources().getDimensionPixelSize(R.dimen.checkbox_mark_top_padding);
        mMarkBottomPadding = getResources().getDimensionPixelSize(R.dimen.checkbox_mark_bottom_padding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            if (widthSpecSize < mCheckBoxWidth) {
                size = mCheckBoxWidth;
            } else {
                size = widthSpecSize;
            }
        } else {
            size = mCheckBoxWidth;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            if (heightSpecSize < mCheckBoxHeight) {
                size = mCheckBoxHeight;
            } else {
                size = heightSpecSize;
            }
        } else {
            size = mCheckBoxHeight;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int rippleColor(int color) {
        int alpha = Math.round(Color.alpha(color) * 0.3f);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private RectF getRectFrame() {
        RectF rectF = new RectF();
        rectF.left = (getWidth() - mThumbSize) / 2;
        rectF.top = (getHeight() - mThumbSize) / 2;
        rectF.right = (getWidth() - mThumbSize) / 2 + mThumbSize;
        rectF.bottom = (getHeight() - mThumbSize) / 2 + mThumbSize;
        return rectF;
    }

    private Path getMarkPath() {
        Path path = new Path();
        float x1 = (getWidth() - mThumbSize) / 2 + mMarkLeftRightPadding;
        float y1 = getHeight() / 2;
        float x2 = x1 + (mThumbSize - mMarkLeftRightPadding * 2) * MarkRatio;
        float y2 = y1 + mThumbSize / 2 - mMarkBottomPadding;
        float x3 = x1 + mThumbSize - mMarkLeftRightPadding * 2;
        float y3 = (getHeight() - mThumbSize) / 2 + mMarkTopPadding;
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        return path;
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

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        int rippleRadius = 0;
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
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, rippleRadius, ripplePaint);

            thumbPaint.setColor(mCheckedColor);
            thumbPaint.setStyle(Paint.Style.FILL);
            canvas.drawRoundRect(getRectFrame(), mCornerRadius, mCornerRadius, thumbPaint);

            thumbPaint.setColor(mCheckedColor);
            thumbPaint.setStyle(Paint.Style.STROKE);
            thumbPaint.setStrokeWidth(mBorderWidth);
            canvas.drawRoundRect(getRectFrame(), mCornerRadius, mCornerRadius, thumbPaint);

            markPaint.setColor(mMarkColor);
            markPaint.setStrokeWidth(mMarkWidth);
            markPaint.setStyle(Paint.Style.STROKE);
            markPaint.setStrokeJoin(Paint.Join.ROUND);
            canvas.drawPath(getMarkPath(), markPaint);
        } else {
            ripplePaint.setColor(rippleColor(mColor));
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, rippleRadius, ripplePaint);

            borderPaint.setColor(mColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(mBorderWidth);
            canvas.drawRoundRect(getRectFrame(), mCornerRadius, mCornerRadius, borderPaint);
        }
    }
}
