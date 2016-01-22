package com.nightonke.saver.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.NinePatchDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.TagDraggableItemAdapter;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;

import net.steamcrafted.materialiconlib.MaterialIconView;

public class TagSettingActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;

    private TagDraggableItemAdapter myItemAdapter;

    private Context mContext;

    private MaterialIconView back;
    private MaterialIconView check;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_setting);

        mContext = this;

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.statusBarColor));
        } else{
            // do something for phones running an SDK before lollipop
        }

        //noinspection ConstantConditions
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);

        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(this, R.drawable.material_shadow_z3));

        // Start dragging after long press
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setLongPressTimeout(750);

        //adapter
        myItemAdapter = new TagDraggableItemAdapter();
        mAdapter = myItemAdapter;

        // wrap for dragging
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(myItemAdapter);

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        mRecyclerView.addItemDecoration(
                new ItemShadowDecorator((NinePatchDrawable) ContextCompat.
                        getDrawable(this, R.drawable.material_shadow_z1)));

        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);

        back = (MaterialIconView)findViewById(R.id.icon_left);
        check = (MaterialIconView)findViewById(R.id.check);
        title = (TextView)findViewById(R.id.title);
        title.setText((RecordManager.TAGS.size() - 2) + mContext.getResources().getString(R.string.tag_number));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges(true);
            }
        });
    }

    private void whetherQuit() {
        if (!myItemAdapter.equals(null)) {
            if (myItemAdapter.isChanged()) {
                new MaterialDialog.Builder(this)
                        .title(R.string.whether_save)
                        .content(R.string.whether_save_tag)
                        .positiveText(R.string.save_y)
                        .negativeText(R.string.save_n)
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (which == DialogAction.POSITIVE) {
                                    saveChanges(true);
                                } else {
                                    finish();
                                }
                            }
                        })
                        .show();
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    MaterialDialog progressDialog;
    private void saveChanges(boolean quit) {
        if (!myItemAdapter.equals(null)) {
            if (myItemAdapter.isChanged()) {
                progressDialog = new MaterialDialog.Builder(this)
                        .title(R.string.saving_tags_title)
                        .content(R.string.saving_tags_content)
                        .progress(true, 0)
                        .cancelable(false)
                        .show();
                new SaveTags(quit).execute();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        // tell others that the tags' order should be changed
        if (myItemAdapter.isChanged()) {
            SettingManager.getInstance().setMainActivityTagShouldChange(true);
        }
    }

    public class SaveTags extends AsyncTask<String, Void, String> {

        private boolean quit;

        public SaveTags(boolean quit) {
            this.quit = quit;
        }

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < myItemAdapter.getTags().size(); i++) {
                RecordManager.TAGS.set(i + 2, myItemAdapter.getTags().get(i));
                RecordManager.TAGS.get(i + 2).setWeight(i);
            }
            for (int i = 2; i < RecordManager.TAGS.size(); i++) {
                RecordManager.updateTag(RecordManager.TAGS.get(i));
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            if (progressDialog != null) progressDialog.cancel();
            if (quit) ((Activity)mContext).finish();
        }
    }

    @Override
    public void onBackPressed() {
        whetherQuit();
    }

}
