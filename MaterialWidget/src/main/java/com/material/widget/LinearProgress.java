package com.material.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by IntelliJ IDEA.
 * User: keith.
 * Date: 14-10-10.
 * Time: 14:46.
 */
public class LinearProgress extends View {

    private static final int PROGRESS_DURATION = 300;

    private int mColor;
    private int mLineWidth;

    public LinearProgress(Context context) {
        this(context, null);
    }

    private Paint bottomBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint bufferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint topBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public LinearProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.LinearProgress);
        mColor = attributes.getColor(R.styleable.LinearProgress_linear_progress_color,
                getResources().getColor(R.color.linear_progress_color));
        mLineWidth = attributes.getDimensionPixelSize(R.styleable.LinearProgress_linear_progress_line_width,
                getResources().getDimensionPixelSize(R.dimen.circular_progress_border_width));
        attributes.recycle();
        bottomBarPaint.setColor(mColor);
        bottomBarPaint.setAlpha(60);
        bottomBarPaint.setStrokeWidth(mLineWidth);

        bufferPaint.setColor(mColor);
        bufferPaint.setAlpha(60);

        topBarPaint.setColor(mColor);
        topBarPaint.setStrokeWidth(mLineWidth);
    }

    private void setColor(int color) {
        mColor = color;
        bottomBarPaint.setColor(mColor);
        bufferPaint.setColor(mColor);
        topBarPaint.setColor(mColor);
        invalidate();
    }

    private void setLineWidth(int pixel) {
        mLineWidth = pixel;
        bottomBarPaint.setStrokeWidth(mLineWidth);
        topBarPaint.setStrokeWidth(mLineWidth);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, bottomBarPaint);
        canvas.drawLine(0, getHeight() / 2, getWidth() / 2, getHeight() / 2, topBarPaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
