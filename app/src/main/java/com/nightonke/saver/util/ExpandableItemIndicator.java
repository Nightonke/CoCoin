package com.nightonke.saver.util;

/**
 * Created by 伟平 on 2015/11/12.
 */

import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.widget.FrameLayout;

public class ExpandableItemIndicator extends FrameLayout {
    static abstract class Impl {
        public abstract void onInit(Context context, AttributeSet attrs, int defStyleAttr, ExpandableItemIndicator thiz);

        public abstract void setExpandedState(boolean isExpanded, boolean animate);
    }

    private Impl mImpl;

    public ExpandableItemIndicator(Context context) {
        super(context);
        onInit(context, null, 0);
    }

    public ExpandableItemIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInit(context, attrs, 0);
    }

    public ExpandableItemIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInit(context, attrs, defStyleAttr);
    }

    protected void onInit(Context context, AttributeSet attrs, int defStyleAttr) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // NOTE: MorphButton only supports API level 14 or later
            mImpl = new ExpandableItemIndicatorImplAnim();
        } else {
            mImpl = new ExpandableItemIndicatorImplNoAnim();
        }
        mImpl.onInit(context, attrs, defStyleAttr, this);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container) {
        super.dispatchThawSelfOnly(container);
    }

    public void setExpandedState(boolean isExpanded, boolean animate) {
        mImpl.setExpandedState(isExpanded, animate);
    }
}
