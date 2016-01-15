package com.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by IntelliJ IDEA.
 * User: keith.
 * Date: 14-10-9.
 * Time: 17:05.
 */
public class CircleButton extends View {

    private static final float SHADOW_RADIUS = 10.0f;
    private static final float SHADOW_OFFSET_X = 0.0f;
    private static final float SHADOW_OFFSET_Y = 3.0f;

    private int mButtonWidth;
    private int mButtonHeight;
    private int mColor;
    private Bitmap mIcon;
    private Rect mFingerRect;
    private boolean mMoveOutside;

    private Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint iconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleButton(Context context) {
        this(context, null);
    }

    public CircleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mButtonWidth = getResources().getDimensionPixelSize(R.dimen.circle_button_width);
        mButtonHeight = getResources().getDimensionPixelSize(R.dimen.circle_button_height);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleButton);
        mColor = attributes.getColor(R.styleable.CircleButton_circle_color,
                getResources().getColor(R.color.circle_button_color));
        float shadowRadius = attributes.getFloat(R.styleable.CircleButton_circle_shadow_radius, SHADOW_RADIUS);
        float shadowOffsetX = attributes.getFloat(R.styleable.CircleButton_circle_shadow_offset_x, SHADOW_OFFSET_X);
        float shadowOffsetY = attributes.getFloat(R.styleable.CircleButton_circle_shadow_offset_y, SHADOW_OFFSET_Y);
        int shadowColor = attributes.getColor(R.styleable.CircleButton_circle_shadow_color,
                getResources().getColor(R.color.circle_button_shadow_color));
        Drawable drawable = attributes.getDrawable(R.styleable.CircleButton_circle_icon);
        if (drawable != null) {
            mIcon = ((BitmapDrawable) drawable).getBitmap();
        }
        attributes.recycle();
        circlePaint.setColor(mColor);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setShadowLayer(shadowRadius, shadowOffsetX, shadowOffsetY, shadowColor);
        setWillNotDraw(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            if (widthSpecSize < mButtonWidth) {
                size = mButtonWidth;
            } else {
                size = widthSpecSize;
            }
        } else {
            size = mButtonWidth;
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            if (heightSpecSize < mButtonHeight) {
                size = mButtonHeight;
            } else {
                size = heightSpecSize;
            }
        } else {
            size = mButtonHeight;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMoveOutside = false;
                mFingerRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
                circlePaint.setColor(darkenColor(mColor));
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mFingerRect.contains(getLeft() + (int) event.getX(),
                        getTop() + (int) event.getY())) {
                    mMoveOutside = true;
                    circlePaint.setColor(mColor);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                circlePaint.setColor(mColor);
                invalidate();
                if (!mMoveOutside) {
                    performClick();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                circlePaint.setColor(mColor);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2.6), circlePaint);
        if (mIcon != null) {
            float x = (getWidth() - mIcon.getWidth()) / 2;
            float y = (getHeight() - mIcon.getHeight()) / 2;
            canvas.drawBitmap(mIcon, x, y, iconPaint);
        }
    }
}
