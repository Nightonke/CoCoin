package com.nightonke.saver.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.nightonke.saver.R;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.Tag;
import com.nightonke.saver.util.CoCoinUtil;

import java.util.ArrayList;

/**
 * Created by 伟平 on 2015/11/7.
 */

public class TagDraggableItemAdapter
        extends RecyclerView.Adapter<TagDraggableItemAdapter.MyViewHolder>
        implements DraggableItemAdapter<TagDraggableItemAdapter.MyViewHolder> {

    private static final String TAG = "TagDraggableItemAdapter";

    private ArrayList<Tag> tags;
    private boolean changed;

    // NOTE: Make accessible with short name
    private interface Draggable extends DraggableItemConstants {
    }

    public static class MyViewHolder extends AbstractDraggableItemViewHolder {
        public FrameLayout mContainer;
        public ImageView tagImage;
        public TextView tagName;

        public MyViewHolder(View v) {
            super(v);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            tagImage = (ImageView)v.findViewById(R.id.tag_image);
            tagName = (TextView)v.findViewById(R.id.tag_name);
        }
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public static String getTAG() {

        return TAG;
    }

    public TagDraggableItemAdapter() {

        tags = new ArrayList<>();

        for (int i = 2; i < RecordManager.TAGS.size(); i++) {
            Tag tag = RecordManager.TAGS.get(i);
            tag.setDragId(i);
            tags.add(tag);
        }

        changed = false;


        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return tags.get(position).getDragId();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.tag_grid_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();

        holder.tagImage.setImageResource(CoCoinUtil.GetTagIcon(tags.get(position).getId()));
        holder.tagName.setText(CoCoinUtil.GetTagName(tags.get(position).getId()));
        holder.tagName.setTypeface(CoCoinUtil.typefaceLatoLight);

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                CoCoinUtil.clearState(holder.mContainer.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_normal_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }
    }

    @Override
    public int getItemCount() {
        return RecordManager.TAGS.size() - 2;
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {

        if (fromPosition == toPosition) {
            return;
        }

        changed = true;

        Tag tempTag = tags.remove(fromPosition);
        tags.add(toPosition, tempTag);

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanStartDrag(MyViewHolder holder, int position, int x, int y) {
        return true;
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(MyViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
