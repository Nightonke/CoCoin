package com.material.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: keith.
 * Date: 14-9-26.
 * Time: 13:39.
 * Thanks for https://github.com/astuetz/PagerSlidingTabStrip
 */
public class TabIndicator extends HorizontalScrollView implements Animator.AnimatorListener {

    private static final String TAG = TabIndicator.class.getSimpleName();

    private static final long ANIMATION_DURATION = 150;
    private static final int StateNormal = 1;
    private static final int StateTouchDown = 2;
    private static final int StateTouchUp = 3;
    private final PageListener mPageListener = new PageListener();
    private int mMaxColumn;
    private int mTextSize;
    private int mTextColor;
    private int mTextSelectedColor;
    private int mTextDisabledColor;
    private int mRippleColor;
    private int mUnderLineColor;
    private int mUnderLineHeight;
    private int mNavButtonWidth;
    private int mCurrentIndex;
    private OnPageChangeListener mOnPageChangeListener;
    private TabContainer tabsContainer;
    private ViewPager pager;
    private TabView mCurrentTab;
    private NavButton mCurrentNavButton;
    private int[] firstIndexSet = new int[]{};
    private int tabCount;
    private Rect lineRect = new Rect();
    private List<NavButton> mNavButtonList = new ArrayList<NavButton>();
    private List<TabView> mTabList = new ArrayList<TabView>();
    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ActionMode mActionMode;

    public TabIndicator(Context context) {
        this(context, null);
    }

    public TabIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFillViewport(true);
        setHorizontalScrollBarEnabled(false);

        tabsContainer = new TabContainer(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.TabIndicator);
        mTextSize = attributes.getDimensionPixelSize(R.styleable.TabIndicator_tab_text_size,
                getResources().getDimensionPixelSize(R.dimen.tab_text_size));

        mTextColor = attributes.getColor(R.styleable.TabIndicator_tab_text_color,
                getResources().getColor(R.color.tab_text_color));
        mTextSelectedColor = attributes.getColor(R.styleable.TabIndicator_tab_text_selected_color,
                getResources().getColor(R.color.tab_text_selected_color));
        mTextDisabledColor = attributes.getColor(R.styleable.TabIndicator_tab_text_disabled_color,
                getResources().getColor(R.color.tab_text_disabled_color));
        mRippleColor = attributes.getColor(R.styleable.TabIndicator_tab_ripple_color,
                getResources().getColor(R.color.tab_ripple_color));

        mUnderLineColor = attributes.getColor(R.styleable.TabIndicator_tab_underline_color,
                getResources().getColor(R.color.tab_underline_color));
        mUnderLineHeight = attributes.getDimensionPixelSize(R.styleable.TabIndicator_tab_underline_height,
                getResources().getDimensionPixelSize(R.dimen.tab_underline_height));

        mMaxColumn = attributes.getInteger(R.styleable.TabIndicator_tab_max_column,
                getResources().getInteger(R.integer.tab_max_column));
        mNavButtonWidth = getResources().getDimensionPixelSize(R.dimen.tab_nav_button_width);
        attributes.recycle();

        linePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (pager != null) {
            notifyDataSetChanged();

            final int mTmpIndex = pager.getCurrentItem();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TabIndicator.this.animatedSelectCurrentTab(mTmpIndex);
                }
            }, 100);
        }

    }

    // TODO programmatically change max column
    public void setMaxColumn(int column) {
        this.mMaxColumn = column;
    }

    // TODO programmatically set current tab index
    public void setCurrentIndex(int index) {

    }

    // TODO programmatically set underline height
    public void setUnderLineHeight(int pixel) {
        this.mUnderLineHeight = pixel;
    }

    public void setTextColor(int color) {
        this.mTextColor = color;
        invalidate();
        for (TabView tabView : mTabList) {
            tabView.setTextColor(mTextColor);
        }
        setTextSelectedColor(mTextSelectedColor);
        invalidate();
    }

    public void setTextSelectedColor(int color) {
        this.mTextSelectedColor = color;
        if (mCurrentTab != null) {
            mCurrentTab.setTextColor(mTextSelectedColor);
        }
        invalidate();
    }

    // TODO disable tab indicator
    public void setTextDisabledColor(int color) {
        this.mTextDisabledColor = color;
    }

    public void setRippleColor(int color) {
        this.mRippleColor = color;
        invalidate();
    }

    public void setUnderLineColor(int color) {
        this.mUnderLineColor = color;
        invalidate();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mOnPageChangeListener = listener;
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.setOnPageChangeListener(mPageListener);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mCurrentIndex = 0;
        tabsContainer.removeAllViews();
        mNavButtonList = new ArrayList<NavButton>();
        mTabList = new ArrayList<TabView>();
        mCurrentIndex = 0;
        if (pager.getAdapter() instanceof TabTextProvider) {
            layoutTabItem();
        }
    }

    private void layoutTabItem() {
        tabCount = pager.getAdapter().getCount();
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                TabTextProvider provider = (TabTextProvider) pager.getAdapter();
                int itemWidth = getMeasuredWidth() / mMaxColumn;
                if (tabCount <= mMaxColumn) {
                    firstIndexSet = new int[]{0};
                    if (tabCount < mMaxColumn) {
                        itemWidth = Math.round(getMeasuredWidth() / tabCount);
                    }
                    for (int i = 0; i < tabCount; i++) {
                        addTab(i, itemWidth, provider.getText(i));
                    }
                } else {
                    int remain = tabCount % mMaxColumn;
                    int segment = (remain == 0 ? 0 : 1) + (tabCount - remain) / mMaxColumn;
                    List<Integer> tempSet = new ArrayList<Integer>();
                    for (int m = 0; m < segment; m++) {
                        tempSet.add(m * mMaxColumn);
                        if (m == 0) {
                            itemWidth = (getMeasuredWidth() - mNavButtonWidth) / mMaxColumn;
                            for (int n = 0; n < mMaxColumn; n++) {
                                addTab(n, itemWidth, provider.getText(n));
                                if (n == mMaxColumn - 1) {
                                    addNavButton(n, NavButton.FORWARD);
                                }
                            }
                        } else if (m == segment - 1) {
                            int size = remain == 0 ? mMaxColumn : remain;
                            itemWidth = (getMeasuredWidth() - mNavButtonWidth) / size;
                            for (int n = 0; n < remain; n++) {
                                int index = m * mMaxColumn + n;
                                if (n == 0) {
                                    addNavButton(index, NavButton.BACKWARD);
                                }
                                addTab(index, itemWidth, provider.getText(index));
                            }
                        } else {
                            itemWidth = (getMeasuredWidth() - mNavButtonWidth * 2) / mMaxColumn;
                            for (int n = 0; n < mMaxColumn; n++) {
                                int index = m * mMaxColumn + n;
                                if (n == 0) {
                                    addNavButton(index, NavButton.BACKWARD);
                                }
                                addTab(index, itemWidth, provider.getText(index));
                                if (n == mMaxColumn - 1) {
                                    addNavButton(index, NavButton.FORWARD);
                                }
                            }
                        }
                    }
                    firstIndexSet = new int[tempSet.size()];
                    for (int i = 0; i < tempSet.size(); ++i) {
                        firstIndexSet[i] = tempSet.get(i);
                    }
                }

            }
        });
    }

    private void addTab(int index, int width, String title) {
        TabView tabView = new TabView(getContext());
        tabView.setIndex(index);
        tabView.setText(title);
        tabView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        tabView.setTextColor(mTextColor);
        tabView.setWidth(width);
        tabView.setTabWidth(width);
        tabView.setOnSelectTabListener(new OnSelectTabListener() {
            @Override
            public void onSelect(TabView tabView) {
                mCurrentIndex = tabView.getIndex();
                mCurrentTab = tabView;
                tabsContainer.postInvalidate();
                pager.setCurrentItem(mCurrentIndex);
            }
        });
        // Default select the first tab
        if (index == 0) {
            mCurrentTab = tabView;
        }
        mTabList.add(tabView);
        tabsContainer.addView(tabView, new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
    }

    private void addNavButton(int index, int direction) {
        NavButton navButton = new NavButton(getContext());
        navButton.setIndex(index);
        navButton.setType(direction);
        navButton.setMaxWidth(mNavButtonWidth);
        navButton.setMinimumWidth(mNavButtonWidth);
        navButton.setOnNavListener(new OnNavListener() {
            @Override
            public void onNav(NavButton button) {
                mCurrentNavButton = button;
                int count;
                int distance = getMeasuredWidth();
                switch (button.getType()) {
                    case NavButton.FORWARD: {
                        count = (mCurrentNavButton.getIndex() + 1) / mMaxColumn;
                        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(TabIndicator.this,
                                "scrollX",
                                distance * count);
                        objectAnimator.addListener(TabIndicator.this);
                        objectAnimator.setDuration(ANIMATION_DURATION);
                        objectAnimator.start();
                    }
                    break;
                    case NavButton.BACKWARD: {
                        count = mCurrentNavButton.getIndex() / mMaxColumn - 1;
                        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(TabIndicator.this,
                                "scrollX",
                                distance * count);
                        objectAnimator.addListener(TabIndicator.this);
                        objectAnimator.setDuration(ANIMATION_DURATION);
                        objectAnimator.start();
                    }
                    break;
                }
            }
        });
        mNavButtonList.add(navButton);
        tabsContainer.addView(navButton, new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
    }

    private void animatedSelectCurrentTab(int index) {
        if (index != mCurrentIndex) {
            if (mCurrentIndex % mMaxColumn == 0 && index < mCurrentIndex) {
                for (NavButton button : mNavButtonList) {
                    if (button.getIndex() == mCurrentIndex) {
                        button.performNavAction();
                        break;
                    }
                }
            } else if (index % mMaxColumn == 0 && index > mCurrentIndex) {
                for (NavButton button : mNavButtonList) {
                    if (button.getIndex() == mCurrentIndex) {
                        button.performNavAction();
                        break;
                    }
                }
            } else {
                for (TabView tabView : mTabList) {
                    if (tabView.getIndex() == index) {
                        tabView.performSelectAction();
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent event) {
        return false;
    }

    @Override
    public void onAnimationStart(Animator animator) {
        if (mCurrentNavButton != null) {
            switch (mCurrentNavButton.getType()) {
                case NavButton.FORWARD: {
                    mCurrentIndex = mCurrentNavButton.getIndex() + 1;
                    if (mCurrentIndex < mTabList.size()) {
                        mCurrentTab = mTabList.get(mCurrentIndex);
                        tabsContainer.postInvalidate();
                    }
                }
                break;
                case NavButton.BACKWARD: {
                    mCurrentIndex = mCurrentNavButton.getIndex() - 1;
                    if (mCurrentIndex < mTabList.size()) {
                        mCurrentTab = mTabList.get(mCurrentIndex);
                        tabsContainer.postInvalidate();
                    }
                }
                break;
            }
        }
    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (mCurrentNavButton != null) {
            switch (mCurrentNavButton.getType()) {
                case NavButton.FORWARD:
                    pager.setCurrentItem(mCurrentIndex);
                    break;
                case NavButton.BACKWARD:
                    pager.setCurrentItem(mCurrentIndex);
                    break;
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    public interface OnSelectTabListener {
        void onSelect(TabView tabView);
    }

    public interface OnNavListener {
        void onNav(NavButton button);
    }

    public interface TabTextProvider {

        public String getText(int position);
    }

    // ================================================== Tab container =========================================== //

    private class TabContainer extends LinearLayout {

        private long mStartTime;

        public TabContainer(Context context) {
            this(context, null);
        }

        public TabContainer(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TabContainer(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            setWillNotDraw(false);
        }

        // TODO animate underline to selected tab
        public void animateToSelectedTab(int lastIndex, int currentIndex) {

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            linePaint.setColor(mUnderLineColor);
            // Animate translation underline
            if (mCurrentTab != null) {
                // Clear text select status
                for (TabView tabView : mTabList) {
                    tabView.setTextColor(mTextColor);
                }
                mCurrentTab.setTextColor(mTextSelectedColor);
                int x;
                if (mCurrentTab.getIndex() < mMaxColumn) {
                    x = mCurrentTab.getIndex() * mCurrentTab.getTabWidth();
                    lineRect.left = x;
                    lineRect.top = getHeight() - mUnderLineHeight;
                    lineRect.right = x + mCurrentTab.getWidth();
                    lineRect.bottom = getHeight();
                } else {
                    int remain = mCurrentTab.getIndex() % mMaxColumn;
                    int segment = (mCurrentTab.getIndex() - remain) / mMaxColumn;
                    x = mNavButtonWidth
                            + segment * TabIndicator.this.getMeasuredWidth()
                            + remain * mCurrentTab.getTabWidth();
                    lineRect.left = x;
                    lineRect.top = getHeight() - mUnderLineHeight;
                    lineRect.right = x + mCurrentTab.getWidth();
                    lineRect.bottom = getHeight();
                }
                canvas.drawRect(lineRect, linePaint);
            }
        }
    }

    // ================================================== Tab item view =========================================== //

    private class TabView extends Button {

        private static final int StateRippleNormal = 0;
        private static final int StateRippleTriggerStart = 1;
        private static final int StateRippleTriggerEnd = 2;
        private static final int StateRippleProliferationStart = 3;
        private static final int StateRippleProliferationEnd = 4;
        private int mRippleState = StateRippleNormal;
        private int mState = StateNormal;
        private int index;
        private int mTabWidth;
        private int mEndRadius;
        private long mStartTime;
        private Rect mFingerRect;
        private boolean mMoveOutside;
        private Point mTouchPoint = new Point();
        private OnSelectTabListener mOnSelectTabListener;

        public TabView(Context context) {
            this(context, null);
        }

        public TabView(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public TabView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(null);
            } else {
                setBackground(null);
            }
            setGravity(Gravity.CENTER);
            setBackgroundColor(Color.TRANSPARENT);
        }

        public void performSelectAction() {
            if (mOnSelectTabListener != null) {
                mOnSelectTabListener.onSelect(TabView.this);
            }
        }

        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMoveOutside = false;
                    mFingerRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
                    mTouchPoint.set(Math.round(event.getX()), Math.round(event.getY()));
                    mState = StateTouchDown;
                    mRippleState = StateRippleTriggerStart;
                    mStartTime = System.currentTimeMillis();
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mFingerRect.contains(getLeft() + (int) event.getX(),
                            getTop() + (int) event.getY())) {
                        mMoveOutside = true;
                        mState = StateNormal;
                        mRippleState = StateRippleNormal;
                        mStartTime = System.currentTimeMillis();
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mMoveOutside) {
                        performSelectAction();
                        mState = StateTouchUp;
                        if (mRippleState == StateRippleTriggerEnd) {
                            mRippleState = StateRippleProliferationStart;
                            mStartTime = System.currentTimeMillis();
                            invalidate();
                        }
                    }
                    break;
            }
            return true;
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            super.onDraw(canvas);
            ripplePaint.setColor(mRippleColor);
            int radius = 0;
            long elapsed = System.currentTimeMillis() - mStartTime;
            switch (mRippleState) {
                case StateRippleTriggerStart: {
                    ripplePaint.setAlpha(120);
                    if (elapsed < ANIMATION_DURATION) {
                        radius = Math.round(elapsed * getWidth() / 2 / ANIMATION_DURATION);
                        postInvalidate();
                    } else {
                        radius = getWidth() / 2;
                        mRippleState = StateRippleTriggerEnd;
                    }
                    mEndRadius = radius;
                }
                break;
                case StateRippleProliferationStart: {
                    if (elapsed < ANIMATION_DURATION) {
                        int alpha = Math.round((ANIMATION_DURATION - elapsed) * 120 / ANIMATION_DURATION);
                        ripplePaint.setAlpha(alpha);
                        radius = mEndRadius + Math.round(elapsed * getWidth() / 2 / ANIMATION_DURATION);
                        postInvalidate();
                    } else {
                        ripplePaint.setAlpha(0);
                        radius = 0;
                        mState = StateNormal;
                        postInvalidate();
                        mRippleState = StateRippleProliferationEnd;
                    }
                }
                break;
                case StateRippleNormal:
                    radius = 0;
                    break;
            }
            canvas.drawCircle(mTouchPoint.x, mTouchPoint.y, radius, ripplePaint);
            switch (mRippleState) {
                case StateRippleTriggerEnd:
                    if (mState == StateTouchUp) {
                        mRippleState = StateRippleProliferationStart;
                        mStartTime = System.currentTimeMillis();
                        invalidate();
                    }
                    break;
                case StateRippleProliferationEnd:
                    mState = StateNormal;
                    mRippleState = StateRippleNormal;
                    break;
            }
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public void setOnSelectTabListener(OnSelectTabListener listener) {
            this.mOnSelectTabListener = listener;
        }

        public int getTabWidth() {
            return mTabWidth;
        }

        public void setTabWidth(int width) {
            this.mTabWidth = width;
        }

    }

    // =============================================== Navigation button ========================================== //

    private class NavButton extends ImageButton {

        private static final int StateRippleNormal = 0;
        private static final int StateRippleTriggerStart = 1;
        private static final int StateRippleTriggerEnd = 2;
        private static final int StateRippleProliferationStart = 3;
        private static final int StateRippleProliferationEnd = 4;
        private int mRippleState = StateRippleNormal;

        public static final int FORWARD = 1;
        public static final int BACKWARD = 2;
        private int mState = StateNormal;
        private int index;
        private int mType;
        private int mEndRadius;
        private long mStartTime;
        private Rect mFingerRect;
        private boolean mMoveOutside;
        private OnNavListener mOnNavListener;

        public NavButton(Context context) {
            this(context, null);
        }

        public NavButton(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public NavButton(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(null);
            } else {
                setBackground(null);
            }
            setScaleType(ScaleType.CENTER);
            setBackgroundColor(Color.TRANSPARENT);
        }

        public void performNavAction() {
            if (mOnNavListener != null) {
                mOnNavListener.onNav(NavButton.this);
            }
        }

        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMoveOutside = false;
                    mFingerRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
                    mState = StateTouchDown;
                    mRippleState = StateRippleTriggerStart;
                    mStartTime = System.currentTimeMillis();
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!mFingerRect.contains(getLeft() + (int) event.getX(),
                            getTop() + (int) event.getY())) {
                        mMoveOutside = true;
                        mState = StateNormal;
                        mRippleState = StateRippleNormal;
                        mStartTime = System.currentTimeMillis();
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (!mMoveOutside) {
                        performNavAction();
                        mState = StateTouchUp;
                        if (mRippleState == StateRippleTriggerEnd) {
                            mRippleState = StateRippleProliferationStart;
                            mStartTime = System.currentTimeMillis();
                            invalidate();
                        }
                    }
                    break;
            }
            return true;
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            super.onDraw(canvas);
            ripplePaint.setColor(mRippleColor);
            int radius = 0;
            long elapsed = System.currentTimeMillis() - mStartTime;
            switch (mRippleState) {
                case StateRippleTriggerStart: {
                    ripplePaint.setAlpha(120);
                    if (elapsed < ANIMATION_DURATION) {
                        radius = Math.round(elapsed * getWidth() / 2 / ANIMATION_DURATION);
                        postInvalidate();
                    } else {
                        radius = getWidth() / 2;
                        mRippleState = StateRippleTriggerEnd;
                    }
                    mEndRadius = radius;
                }
                break;
                case StateRippleProliferationStart: {
                    if (elapsed < ANIMATION_DURATION) {
                        int alpha = Math.round((ANIMATION_DURATION - elapsed) * 120 / ANIMATION_DURATION);
                        ripplePaint.setAlpha(alpha);
                        radius = mEndRadius + Math.round(elapsed * getWidth() / 2 / ANIMATION_DURATION);
                        postInvalidate();
                    } else {
                        ripplePaint.setAlpha(0);
                        radius = 0;
                        mState = StateNormal;
                        postInvalidate();
                        mRippleState = StateRippleProliferationEnd;
                    }
                }
                break;
                case StateRippleNormal:
                    radius = 0;
                    break;
            }
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, ripplePaint);
            switch (mRippleState) {
                case StateRippleTriggerEnd:
                    if (mState == StateTouchUp) {
                        mRippleState = StateRippleProliferationStart;
                        mStartTime = System.currentTimeMillis();
                        invalidate();
                    }
                    break;
                case StateRippleProliferationEnd:
                    mState = StateNormal;
                    mRippleState = StateRippleNormal;
                    break;
            }
        }

        public void setOnNavListener(OnNavListener listener) {
            this.mOnNavListener = listener;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getType() {
            return mType;
        }

        public void setType(int type) {
            this.mType = type;
            switch (this.mType) {
                case FORWARD:
                    setImageResource(R.drawable.ic_forward);
                    break;
                case BACKWARD:
                    setImageResource(R.drawable.ic_backward);
                    break;
            }
        }

    }

    public void setActionMode(ActionMode mActionMode) {
        this.mActionMode = mActionMode;
        pager.setOnPageChangeListener(mPageListener);
    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            // TODO Scroll indicator animated
        }

        @Override
        public void onPageSelected(int position) {
            if (mActionMode != null) mActionMode.finish();
            TabIndicator.this.animatedSelectCurrentTab(position);
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mOnPageChangeListener != null) {
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }
}
