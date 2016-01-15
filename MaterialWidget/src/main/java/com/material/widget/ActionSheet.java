package com.material.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: keith.
 * Date: 14-10-9.
 * Time: 16:02.
 */
public class ActionSheet extends Dialog {
    private static final String TAG = ActionSheet.class.getSimpleName();

    private static final int LIST_STYLE = 1;
    private static final int GRID_STYLE = 2;

    private static final int NUMBER_COLUMNS = 3;

    private CharSequence mTitle;
    private OnItemClickListener mListener;
    private List<Item> itemList = new ArrayList<Item>();

    private ItemAdapter adapter;
    private View mBackgroundView;
    private int mStyle;

    public ActionSheet(Context context) {
        super(context, android.R.style.Theme_NoTitleBar);
        createView();
    }

    private void createView() {
        FrameLayout container = new FrameLayout(getContext());
        FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        container.setLayoutParams(containerParams);

        mBackgroundView = new View(getContext());
        ViewGroup.LayoutParams backgroundViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mBackgroundView.setBackgroundColor(Color.RED);
        mBackgroundView.setLayoutParams(backgroundViewParams);

        container.addView(mBackgroundView);

        setContentView(container);
        /*
        GridView gridView = new GridView(getContext());
        adapter = new ItemAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            }
        });
        parent.addView(gridView);
        */
    }

    public void setStyle(int style) {
        this.mStyle = style;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void setTitle(CharSequence title) {
        this.mTitle = title;
    }

    @Override
    public void setTitle(int titleId) {
        this.mTitle = getContext().getResources().getString(titleId);
    }

    public void showSheet() {
        if (!isShowing()) {
            show();
        }
    }

    public ActionSheet addItems(List<Item> items) {
        if (items == null || items.size() == 0) {
            return this;
        } else {
            itemList.addAll(items);
        }
        return this;
    }

    private class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Item getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LinearLayout parent = new LinearLayout(getContext());

            TextView textView = new TextView(getContext());
            parent.addView(textView);

            ImageView imageView = new ImageView(getContext());
            parent.addView(imageView);

            return parent;
        }
    }

    public class Item {
        public int mIcon;
        public String mText;

        public Item(int icon, String text) {
            super();
            this.mIcon = icon;
            this.mText = text;
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(int position);
    }
}
