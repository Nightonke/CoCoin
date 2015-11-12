package com.nightonke.saver.adapter;

/**
 * Created by 伟平 on 2015/11/12.
 */

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableSwipeableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.SwipeableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultAction;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionDefault;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionMoveToSwipedDirection;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.action.SwipeResultActionRemoveItem;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.RecyclerViewAdapterUtils;
import com.nightonke.saver.R;
import com.nightonke.saver.util.ExpandableItemIndicator;
import com.nightonke.saver.util.ViewUtil;

import java.util.ArrayList;

public class RecordExpandableSwipeableItemAdapter
        extends AbstractExpandableItemAdapter<RecordExpandableSwipeableItemAdapter.MyGroupViewHolder,
        RecordExpandableSwipeableItemAdapter.MyChildViewHolder>
        implements ExpandableSwipeableItemAdapter<RecordExpandableSwipeableItemAdapter.MyGroupViewHolder,
                RecordExpandableSwipeableItemAdapter.MyChildViewHolder> {

    private ArrayList<ArrayList<Integer>> list;

    // NOTE: Make accessible with short name
    private interface Expandable extends ExpandableItemConstants {
    }

    private interface Swipeable extends SwipeableItemConstants {
    }

    private final RecyclerViewExpandableItemManager mExpandableItemManager;
    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;
    private View.OnClickListener mSwipeableViewContainerOnClickListener;

    public interface EventListener {
        void onGroupItemRemoved(int groupPosition);

        void onChildItemRemoved(int groupPosition, int childPosition);

        void onGroupItemPinned(int groupPosition);

        void onChildItemPinned(int groupPosition, int childPosition);

        void onItemViewClicked(View v, boolean pinned);
    }

    public static abstract class MyBaseViewHolder
            extends AbstractDraggableSwipeableItemViewHolder implements ExpandableItemViewHolder {
        public FrameLayout mContainer;
        public View mDragHandle;
        public TextView mTextView;
        private int mExpandStateFlags;

        public MyBaseViewHolder(View v) {
            super(v);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.drag_handle);
            mTextView = (TextView) v.findViewById(android.R.id.text1);
        }

        @Override
        public int getExpandStateFlags() {
            return mExpandStateFlags;
        }

        @Override
        public void setExpandStateFlags(int flag) {
            mExpandStateFlags = flag;
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
        public ExpandableItemIndicator mIndicator;

        public MyGroupViewHolder(View v) {
            super(v);
            mIndicator = (ExpandableItemIndicator) v.findViewById(R.id.indicator);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        public MyChildViewHolder(View v) {
            super(v);
        }
    }

    public RecordExpandableSwipeableItemAdapter(
            RecyclerViewExpandableItemManager expandableItemManager,
            ArrayList<ArrayList<Integer>> inList) {
        list = inList;
        mExpandableItemManager = expandableItemManager;
        mItemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClick(v);
            }
        };
        mSwipeableViewContainerOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSwipeableViewContainerClick(v);
            }
        };

        // ExpandableItemAdapter, ExpandableDraggableItemAdapter and ExpandableSwipeableItemAdapter
        // require stable ID, and also have to implement the getGroupItemId()/getChildItemId() methods appropriately.
        setHasStableIds(true);
    }

    private void onItemViewClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(v, true);  // true --- pinned
        }
    }

    private void onSwipeableViewContainerClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(RecyclerViewAdapterUtils.getParentViewHolderItemView(v), false);  // false --- not pinned
        }
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return list.get(groupPosition).size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return list.get(groupPosition).get(0);  // get id
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return list.get(groupPosition).get(childPosition);  // get id
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_group_item_draggable, parent, false);
        return new MyGroupViewHolder(v);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_draggable, parent, false);
        return new MyChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, int viewType) {
        // group item
        final ArrayList<Integer> item = list.get(groupPosition);

        // set listeners
        holder.itemView.setOnClickListener(mItemViewOnClickListener);

        // set text
        holder.mTextView.setText(item.toString());

        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();
        final int expandState = holder.getExpandStateFlags();
        final int swipeState = holder.getSwipeStateFlags();

        if (((expandState & Expandable.STATE_FLAG_IS_UPDATED) != 0) ||
                ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;
            boolean isExpanded;
            boolean animateIndicator
                    = ((expandState & Expandable.STATE_FLAG_HAS_EXPANDED_STATE_CHANGED) != 0);

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                isExpanded = true;
            } else {
                isExpanded = false;
            }

//            holder.mContainer.setBackgroundResource(bgResId);
            holder.mIndicator.setExpandedState(isExpanded, animateIndicator);
        }

        // set swiping properties
//        holder.setSwipeItemHorizontalSlideAmount(
//                item.isPinned() ? Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
    }

    @Override
    public void onBindChildViewHolder(MyChildViewHolder holder,
                                      int groupPosition, int childPosition, int viewType) {
        // child item
        final Integer item = list.get(groupPosition).get(childPosition);

        // set listeners
        // (if the item is *not pinned*, click event comes to the itemView)
        holder.itemView.setOnClickListener(mItemViewOnClickListener);
        // (if the item is *pinned*, click event comes to the mContainer)
        holder.mContainer.setOnClickListener(mSwipeableViewContainerOnClickListener);

        // set text
        holder.mTextView.setText(item + "");

        final int dragState = holder.getDragStateFlags();
        final int swipeState = holder.getSwipeStateFlags();

        if ((swipeState & Swipeable.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;

//            holder.mContainer.setBackgroundResource(bgResId);
        }

        // set swiping properties
//        holder.setSwipeItemHorizontalSlideAmount(
//                item.isPinned() ? Swipeable.OUTSIDE_OF_THE_WINDOW_LEFT : 0);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(
            MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // check the item is *not* pinned
//        if (mProvider.getGroupItem(groupPosition).isPinned()) {
//            // return false to raise View.OnClickListener#onClick() event
//            return false;
//        }

        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft()
                + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop()
                + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return !ViewUtil.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public int onGetGroupItemSwipeReactionType(
            MyGroupViewHolder holder, int groupPosition, int x, int y) {
//        if (onCheckGroupCanStartDrag(holder, groupPosition, x, y)) {
//            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
//        }

        return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public int onGetChildItemSwipeReactionType(
            MyChildViewHolder holder, int groupPosition, int childPosition, int x, int y) {
//        if (onCheckChildCanStartDrag(holder, groupPosition, childPosition, x, y)) {
//            return Swipeable.REACTION_CAN_NOT_SWIPE_BOTH_H;
//        }

        return Swipeable.REACTION_CAN_SWIPE_BOTH_H;
    }

    @Override
    public void onSetGroupItemSwipeBackground(
            MyGroupViewHolder holder, int groupPosition, int type) {
        int bgResId = 0;
        switch (type) {
            case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_neutral;
                break;
            case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_group_item_left;
                break;
            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_group_item_right;
                break;
        }

        holder.itemView.setBackgroundResource(bgResId);
    }

    @Override
    public void onSetChildItemSwipeBackground(
            MyChildViewHolder holder, int groupPosition, int childPosition, int type) {
        int bgResId = 0;
        switch (type) {
            case Swipeable.DRAWABLE_SWIPE_NEUTRAL_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_neutral;
                break;
            case Swipeable.DRAWABLE_SWIPE_LEFT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_left;
                break;
            case Swipeable.DRAWABLE_SWIPE_RIGHT_BACKGROUND:
                bgResId = R.drawable.bg_swipe_item_right;
                break;
        }

        holder.itemView.setBackgroundResource(bgResId);
    }

    @Override
    public SwipeResultAction onSwipeGroupItem(
            MyGroupViewHolder holder, int groupPosition, int result) {
        switch (result) {
            // swipe right
            case Swipeable.RESULT_SWIPED_RIGHT:
//                if (mProvider.getGroupItem(groupPosition).isPinned()) {
//                    // pinned --- back to default position
//                    return new GroupUnpinResultAction(this, groupPosition);
//                } else {
//                    // not pinned --- remove
//                    return new GroupSwipeRightResultAction(this, groupPosition);
//                }
                // swipe left -- pin
            case Swipeable.RESULT_SWIPED_LEFT:
                return new GroupSwipeLeftResultAction(this, groupPosition);
            // other --- do nothing
            case Swipeable.RESULT_CANCELED:
            default:
                if (groupPosition != RecyclerView.NO_POSITION) {
                    return new GroupUnpinResultAction(this, groupPosition);
                } else {
                    return null;
                }
        }
    }

    @Override
    public SwipeResultAction onSwipeChildItem(
            MyChildViewHolder holder, int groupPosition, int childPosition, int result) {
        switch (result) {
            // swipe right
            case Swipeable.RESULT_SWIPED_RIGHT:
//                if (mProvider.getChildItem(groupPosition, childPosition).isPinned()) {
//                    // pinned --- back to default position
//                    return new ChildUnpinResultAction(this, groupPosition, childPosition);
//                } else {
//                    // not pinned --- remove
//                    return new ChildSwipeRightResultAction(this, groupPosition, childPosition);
//                }
                // swipe left -- pin
            case Swipeable.RESULT_SWIPED_LEFT:
                return new ChildSwipeLeftResultAction(this, groupPosition, childPosition);
            // other --- do nothing
            case Swipeable.RESULT_CANCELED:
            default:
                if (groupPosition != RecyclerView.NO_POSITION) {
                    return new ChildUnpinResultAction(this, groupPosition, childPosition);
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

    private static class GroupSwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private RecordExpandableSwipeableItemAdapter mAdapter;
        private final int mGroupPosition;
        private boolean mSetPinned;

        GroupSwipeLeftResultAction(
                RecordExpandableSwipeableItemAdapter adapter, int groupPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

//            AbstractExpandableDataProvider.GroupData item =
//                    mAdapter.mProvider.getGroupItem(mGroupPosition);
//
//            if (!item.isPinned()) {
//                item.setPinned(true);
//                mAdapter.mExpandableItemManager.notifyGroupItemChanged(mGroupPosition);
//                mSetPinned = true;
//            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mSetPinned && mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onGroupItemPinned(mGroupPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class GroupSwipeRightResultAction extends SwipeResultActionRemoveItem {
        private RecordExpandableSwipeableItemAdapter mAdapter;
        private final int mGroupPosition;

        GroupSwipeRightResultAction(
                RecordExpandableSwipeableItemAdapter adapter, int groupPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

//            mAdapter.mProvider.removeGroupItem(mGroupPosition);
            mAdapter.mExpandableItemManager.notifyGroupItemRemoved(mGroupPosition);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onGroupItemRemoved(mGroupPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class GroupUnpinResultAction extends SwipeResultActionDefault {
        private RecordExpandableSwipeableItemAdapter mAdapter;
        private final int mGroupPosition;

        GroupUnpinResultAction(RecordExpandableSwipeableItemAdapter adapter, int groupPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

//            AbstractExpandableDataProvider.GroupData item = mAdapter.mProvider.getGroupItem(mGroupPosition);
//            if (item.isPinned()) {
//                item.setPinned(false);
//                mAdapter.mExpandableItemManager.notifyGroupItemChanged(mGroupPosition);
//            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }


    private static class ChildSwipeLeftResultAction extends SwipeResultActionMoveToSwipedDirection {
        private RecordExpandableSwipeableItemAdapter mAdapter;
        private final int mGroupPosition;
        private final int mChildPosition;
        private boolean mSetPinned;

        ChildSwipeLeftResultAction(RecordExpandableSwipeableItemAdapter adapter,
                                   int groupPosition, int childPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

//            AbstractExpandableDataProvider.ChildData item =
//                    mAdapter.mProvider.getChildItem(mGroupPosition, mChildPosition);
//
//            if (!item.isPinned()) {
//                item.setPinned(true);
//                mAdapter.mExpandableItemManager.notifyChildItemChanged(mGroupPosition, mChildPosition);
//                mSetPinned = true;
//            }
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mSetPinned && mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onChildItemPinned(mGroupPosition, mChildPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class ChildSwipeRightResultAction extends SwipeResultActionRemoveItem {
        private RecordExpandableSwipeableItemAdapter mAdapter;
        private final int mGroupPosition;
        private final int mChildPosition;

        ChildSwipeRightResultAction(RecordExpandableSwipeableItemAdapter adapter,
                                    int groupPosition, int childPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

//            mAdapter.mProvider.removeChildItem(mGroupPosition, mChildPosition);
            mAdapter.mExpandableItemManager.notifyChildItemRemoved(mGroupPosition, mChildPosition);
        }

        @Override
        protected void onSlideAnimationEnd() {
            super.onSlideAnimationEnd();

            if (mAdapter.mEventListener != null) {
                mAdapter.mEventListener.onChildItemRemoved(mGroupPosition, mChildPosition);
            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }

    private static class ChildUnpinResultAction extends SwipeResultActionDefault {
        private RecordExpandableSwipeableItemAdapter mAdapter;
        private final int mGroupPosition;
        private final int mChildPosition;

        ChildUnpinResultAction(RecordExpandableSwipeableItemAdapter adapter,
                               int groupPosition, int childPosition) {
            mAdapter = adapter;
            mGroupPosition = groupPosition;
            mChildPosition = childPosition;
        }

        @Override
        protected void onPerformAction() {
            super.onPerformAction();

//            AbstractExpandableDataProvider.ChildData item = mAdapter.mProvider.getChildItem(mGroupPosition, mChildPosition);
//            if (item.isPinned()) {
//                item.setPinned(false);
//                mAdapter.mExpandableItemManager.notifyChildItemChanged(mGroupPosition, mChildPosition);
//            }
        }

        @Override
        protected void onCleanUp() {
            super.onCleanUp();
            // clear the references
            mAdapter = null;
        }
    }
}
