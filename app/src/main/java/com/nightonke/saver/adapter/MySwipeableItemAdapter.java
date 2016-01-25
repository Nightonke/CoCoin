package com.nightonke.saver.adapter;

/**
 * Created by 伟平 on 2015/11/13.
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import com.nightonke.saver.R;
import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.model.CoCoinRecord;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.ui.SwipeableItemOnClickListener;
import com.nightonke.saver.util.CoCoinUtil;

import java.util.HashMap;
import java.util.List;

public class MySwipeableItemAdapter
        extends RecyclerView.Adapter<MySwipeableItemAdapter.MyViewHolder>
        implements SwipeableItemAdapter<MySwipeableItemAdapter.MyViewHolder> {

    // NOTE: Make accessible with short name
    private interface Swipeable extends SwipeableItemConstants {
    }

    private OnItemDeleteListener onItemDeleteListener;
    private OnItemClickListener onItemClickListener;

    private Context mContext;
    private EventListener mEventListener;
    private static HashMap<Integer, Boolean> pinned;

    private List<CoCoinRecord> records;

    public interface EventListener {
        void onItemRemoved(int position);

        void onItemPinned(int position);

        void onItemViewClicked(View v, boolean pinned);
    }

    public static class MyViewHolder extends AbstractSwipeableItemViewHolder {
        public FrameLayout mContainer;
        public TextView money;
        public TextView remark;
        public TextView date;
        public ImageView tagImage;
        public TextView index;

        public MyViewHolder(View v) {
            super(v);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            money = (TextView) v.findViewById(R.id.money);
            remark = (TextView) v.findViewById(R.id.remark);
            date = (TextView) v.findViewById(R.id.date);
            tagImage = (ImageView) v.findViewById(R.id.image_view);
            index = (TextView)v.findViewById(R.id.index);
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }

    public MySwipeableItemAdapter(Context inContext, List<CoCoinRecord> records, final OnItemDeleteListener onItemDeleteListener, OnItemClickListener onItemClickListener) {
        mContext = inContext;
        this.records = records;
        this.onItemDeleteListener = onItemDeleteListener;
        this.onItemClickListener = onItemClickListener;
        // Todo optimize
        pinned = new HashMap<>();
        for (int i = records.size() - 1; i >= 0; i--) {
            pinned.put((int)records.get(i).getId(), false);
        }

        setHasStableIds(true);
    }

    private void onItemViewClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(v, true);
        }
    }

    private void onSwipeableViewContainerClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(
                    RecyclerViewAdapterUtils.getParentViewHolderItemView(v), false);
        }
    }

    @Override
    public long getItemId(int position) {
        return records.get(records.size() - 1 - position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.account_book_list_view_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set listeners
        // (if the item is *not pinned*, click event comes to the itemView)
        holder.itemView.setOnClickListener(new SwipeableItemOnClickListener(position) {
            @Override
            public void onClick(View v) {
                onItemViewClick(v);
            }
        });
        // (if the item is *pinned*, click event comes to the mContainer)
        holder.mContainer.setOnClickListener(new SwipeableItemOnClickListener(position) {
            @Override
            public void onClick(View v) {
                onSwipeableViewContainerClick(v);
                onItemClickListener.onItemClick(position);
            }
        });

        // set text
        int tPosition = records.size() - 1 - position;
        CoCoinRecord record = records.get(tPosition);
        holder.tagImage.setImageResource(
                CoCoinUtil.GetTagIcon(record.getTag()));
        holder.date.setText(record.getCalendarString());
        holder.money.setText(String.valueOf((int) record.getMoney()));
        holder.date.setTypeface(CoCoinUtil.typefaceLatoLight);
        holder.money.setTypeface(CoCoinUtil.typefaceLatoLight);
        holder.money.setTextColor(ContextCompat.getColor(CoCoinApplication.getAppContext(), R.color.my_blue));
        holder.index.setText((position + 1) + "");
        holder.index.setTypeface(CoCoinUtil.typefaceLatoLight);
        holder.remark.setText(record.getRemark());
        holder.remark.setTypeface(CoCoinUtil.typefaceLatoLight);

        // set background resource (target view ID: container)
        final int swipeState = holder.getSwipeStateFlags();

        if ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;

            if ((swipeState & Swipeable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_swiping_active_state;
            } else if ((swipeState & Swipeable.STATE_FLAG_SWIPING) != 0) {
                bgResId = R.drawable.bg_item_swiping_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }

        holder.setSwipeItemHorizontalSlideAmount(
                pinned.get((int) records.get(
                        records.size() - 1 - position).getId()) ?
                        Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    @Override
    public int onGetSwipeReactionType(MyViewHolder holder, int position, int x, int y) {
        return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public void onSetSwipeBackground(MyViewHolder holder, int position, int type) {
        int bgRes = 0;
        switch (type) {
            case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_neutral;
                break;
            case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_left;
                break;
            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgRes = R.drawable.bg_swipe_item_right;
                break;
        }

        holder.itemView.setBackgroundResource(bgRes);
    }

    @Override
    public SwipeResultAction onSwipeItem(MyViewHolder holder, final int position, int result) {

        switch (result) {
            // swipe right
            case Swipeable.RESULT_SWIPED_RIGHT:
                if (pinned.get((int)records.get(records.size() - 1 - position).getId())) {
                    return new UnpinResultAction(this, position);
                } else {
                    return new SwipeRightResultAction(this, position, onItemDeleteListener);
                }
            case Swipeable.RESULT_SWIPED_LEFT:
                return new SwipeLeftResultAction(this, position);
            case Swipeable.RESULT_CANCELED:
            default:
                if (position != RecyclerView.NO_POSITION) {
                    return new UnpinResultAction(this, position);
                } else {
                    return null;
                }
        }
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }

    private static class SwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private MySwipeableItemAdapter mAdapter;
        private final int mPosition;
        private boolean mSetPinned;

        SwipeLeftResultAction(MySwipeableItemAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            if (!pinned.get((int)RecordManager.SELECTED_RECORDS.get(
                    RecordManager.SELECTED_RECORDS.size() - 1 - mPosition).getId())) {
                pinned.put((int)RecordManager.SELECTED_RECORDS.get(
                        RecordManager.SELECTED_RECORDS.size() - 1 - mPosition).getId(), true);
                mSetPinned = true;
                mAdapter.notifyItemChanged(mPosition);
            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();
            if (mSetPinned && mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onItemPinned(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            mAdapter = null;
        }
    }

    public static class SwipeRightResultAction extends SwipeResultActionRemoveItem {

        private OnItemDeleteListener onItemDeleteListener;
        private MySwipeableItemAdapter mAdapter;
        private final int mPosition;

        SwipeRightResultAction(MySwipeableItemAdapter adapter, int position, OnItemDeleteListener onItemDeleteListener) {
            mAdapter = adapter;
            mPosition = position;
            this.onItemDeleteListener = onItemDeleteListener;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            if (CoCoinUtil.backupCoCoinRecord != null) {
                RecordManager.deleteRecord(CoCoinUtil.backupCoCoinRecord, true);
            }
            CoCoinUtil.backupCoCoinRecord = null;
            CoCoinUtil.backupCoCoinRecord
                    = RecordManager.SELECTED_RECORDS.get(RecordManager.SELECTED_RECORDS.size() - 1 - mPosition);
            RecordManager.SELECTED_RECORDS.remove(RecordManager.SELECTED_RECORDS.size() - 1 - mPosition);
            RecordManager.SELECTED_SUM -= CoCoinUtil.backupCoCoinRecord.getMoney();
            onItemDeleteListener.onSelectSumChanged();
            mAdapter.notifyItemRemoved(mPosition);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onItemRemoved(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class UnpinResultAction extends SwipeResultActionDefault {
        private MySwipeableItemAdapter mAdapter;
        private final int mPosition;

        UnpinResultAction(MySwipeableItemAdapter adapter, int position) {
            mAdapter = adapter;
            mPosition = position;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();
            if (pinned.get((int) RecordManager.SELECTED_RECORDS.get(
                    RecordManager.SELECTED_RECORDS.size() - 1 - mPosition).getId())) {
                pinned.put((int) RecordManager.SELECTED_RECORDS.get(
                        RecordManager.SELECTED_RECORDS.size() - 1 - mPosition).getId(), false);
                mAdapter.notifyItemChanged(mPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    public void setPinned(boolean inPinned, int position) {
        pinned.put((int)RecordManager.SELECTED_RECORDS.get(
                RecordManager.SELECTED_RECORDS.size() - 1 - position).getId(), inPinned);

    }

    public interface OnItemDeleteListener {
        void onSelectSumChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
