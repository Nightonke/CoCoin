package com.material.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by IntelliJ IDEA.
 * User: keith.
 * Date: 14-10-30.
 * Time: 15:57.
 */
public class FloatingEditText extends EditText {

    private static final long ANIMATION_DURATION = 120;
    private static final int StateHintNormal = 0;
    private static final int StateHintZoomIn = 1;
    private static final int StateHintZoomOut = 2;
    private static final float HINT_SCALE = 0.5f;

    private int mState = StateHintNormal;
    private long mStartTime;
    private int mColor;
    private int mHighlightedColor;
    private int mErrorColor;
    private boolean mVerified = true;
    private String mValidateMessage;
    private int mUnderlineHeight;
    private int mUnderlineHighlightedHeight;
    private boolean mTextEmpty;
    private float mHintScale;
    private Rect lineRect = new Rect();

    private Paint mHintPaint;

    public FloatingEditText(Context context) {
        this(context, null);
    }

    public FloatingEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public FloatingEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, android.R.attr.editTextStyle);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.FloatingEditText);
        mHintScale = attributes.getFloat(R.styleable.FloatingEditText_floating_edit_text_hint_scale,
                HINT_SCALE);
        mColor = attributes.getColor(R.styleable.FloatingEditText_floating_edit_text_color,
                getResources().getColor(R.color.floating_edit_text_color));
        mHighlightedColor = attributes.getColor(R.styleable.FloatingEditText_floating_edit_text_highlighted_color,
                getResources().getColor(R.color.floating_edit_text_highlighted_color));
        mErrorColor = attributes.getColor(R.styleable.FloatingEditText_floating_edit_text_error_color,
                getResources().getColor(R.color.floating_edit_text_error_color));
        mUnderlineHeight = attributes.getDimensionPixelSize(R.styleable.FloatingEditText_floating_edit_text_underline_height,
                getResources().getDimensionPixelSize(R.dimen.floating_edit_text_underline_height));
        mUnderlineHighlightedHeight = attributes.getDimensionPixelSize(R.styleable.FloatingEditText_floating_edit_text_underline_highlighted_height,
                getResources().getDimensionPixelSize(R.dimen.floating_edit_text_underline_highlighted_height));
        setHintTextColor(Color.TRANSPARENT);
        mTextEmpty = TextUtils.isEmpty(getText());
        mHintPaint = new Paint();
        mHintPaint.setAntiAlias(true);

        Drawable drawable = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                if (mVerified) {
                    if (isFocused()) {
                        Rect rect = getThickLineRect(canvas);
                        mHintPaint.setColor(mHighlightedColor);
                        canvas.drawRect(rect, mHintPaint);
                    } else {
                        Rect rect = getThinLineRect(canvas);
                        mHintPaint.setColor(mColor);
                        canvas.drawRect(rect, mHintPaint);
                    }
                } else {
                    Rect rect = getThickLineRect(canvas);
                    mHintPaint.setColor(mErrorColor);
                    canvas.drawRect(rect, mHintPaint);

                    mHintPaint.setColor(mErrorColor);
                    mHintPaint.setTextSize(getTextSize() * 0.6f);
                    float x = getCompoundPaddingLeft();
                    float y = rect.bottom + (dpToPx(16) - mHintPaint.getFontMetricsInt().top) / 2;
                    canvas.drawText(mValidateMessage, x, y, mHintPaint);
                }
            }

            @Override
            public void setAlpha(int alpha) {
                mHintPaint.setAlpha(alpha);
            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {
                mHintPaint.setColorFilter(colorFilter);
            }

            @Override
            public int getOpacity() {
                return PixelFormat.TRANSPARENT;
            }
        };
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(drawable);
        } else {
            setBackground(drawable);
        }
        int paddingTop = dpToPx(12);
        int paddingBottom = dpToPx(20);
        setPadding(0, paddingTop, 0, paddingBottom);
    }

    private Rect getThinLineRect(Canvas canvas) {
        lineRect.left = getPaddingLeft();
        lineRect.top = canvas.getHeight() - mUnderlineHeight - dpToPx(16);
        lineRect.right = getWidth();
        lineRect.bottom = canvas.getHeight() - dpToPx(16);
        return lineRect;
    }

    private Rect getThickLineRect(Canvas canvas) {
        lineRect.left = getPaddingLeft();
        lineRect.top = canvas.getHeight() - mUnderlineHighlightedHeight - dpToPx(16);
        lineRect.right = getWidth();
        lineRect.bottom = canvas.getHeight() - dpToPx(16);
        return lineRect;
    }

    public void setNormalColor(int color) {
        this.mColor = color;
        invalidate();
    }

    public void setHighlightedColor(int color) {
        this.mHighlightedColor = color;
        invalidate();
    }

    public void setValidateResult(boolean verified, String message) {
        if (!verified && message == null) {
            throw new IllegalStateException("Must have a validate result message.");
        }
        this.mVerified = verified;
        this.mValidateMessage = message;
        invalidate();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.mVerified = true;
        this.mValidateMessage = null;
        boolean isEmpty = TextUtils.isEmpty(getText());
        if (mTextEmpty != isEmpty) {
            this.mTextEmpty = isEmpty;
            if (isEmpty && isShown()) {
                mStartTime = System.currentTimeMillis();
                mState = StateHintZoomIn;
            } else {
                mStartTime = System.currentTimeMillis();
                mState = StateHintZoomOut;
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (!TextUtils.isEmpty(getHint())) {
            mHintPaint.set(getPaint());
            float maxTextSize = getTextSize();
            float minTextSize = getTextSize() * mHintScale;
            float maxHintY = getBaseline();
            float minHintY = getBaseline() + getPaint().getFontMetricsInt().top + getScrollY() - dpToPx(4);
            float textSize;
            float hintY;
            float hintX = getCompoundPaddingLeft() + getScrollX();
            long elapsed = System.currentTimeMillis() - mStartTime;
            switch (mState) {
                case StateHintNormal: {
                    textSize = maxTextSize;
                    hintY = maxHintY;
                    mHintPaint.setColor(mColor);
                    mHintPaint.setTextSize(textSize);
                    canvas.drawText(getHint().toString(), hintX, hintY, mHintPaint);
                }
                break;
                case StateHintZoomIn: {
                    if (elapsed < ANIMATION_DURATION) {
                        textSize = ((maxTextSize - minTextSize) * elapsed) / ANIMATION_DURATION + minTextSize;
                        hintY = ((maxHintY - minHintY) * elapsed) / ANIMATION_DURATION + minHintY;
                        mHintPaint.setColor(mHighlightedColor);
                        mHintPaint.setTextSize(textSize);
                        canvas.drawText(getHint().toString(), hintX, hintY, mHintPaint);
                        postInvalidate();
                    } else {
                        textSize = maxTextSize;
                        hintY = maxHintY;
                        mHintPaint.setColor(mColor);
                        mHintPaint.setTextSize(textSize);
                        canvas.drawText(getHint().toString(), hintX, hintY, mHintPaint);
                    }
                }
                break;
                case StateHintZoomOut: {
                    if (elapsed < ANIMATION_DURATION) {
                        textSize = maxTextSize - ((maxTextSize - minTextSize) * elapsed) / ANIMATION_DURATION;
                        hintY = maxHintY - ((maxHintY - minHintY) * elapsed) / ANIMATION_DURATION;
                        mHintPaint.setColor(mHighlightedColor);
                        mHintPaint.setTextSize(textSize);
                        canvas.drawText(getHint().toString(), hintX, hintY, mHintPaint);
                        postInvalidate();
                    } else {
                        textSize = minTextSize;
                        hintY = minHintY;
                        if (isFocused()) {
                            mHintPaint.setColor(mHighlightedColor);
                        } else {
                            mHintPaint.setColor(mColor);
                        }
                        mHintPaint.setTextSize(textSize);
                        canvas.drawText(getHint().toString(), hintX, hintY, mHintPaint);
                    }
                }
                break;
            }
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
