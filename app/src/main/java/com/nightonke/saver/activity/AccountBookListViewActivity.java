package com.nightonke.saver.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.MySwipeableItemAdapter;
import com.nightonke.saver.model.Record;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.util.Util;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;

public class AccountBookListViewActivity extends AppCompatActivity {

    private MaterialSearchView searchView;

    private Context mContext;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter wrappedAdapter;
    private RecyclerViewSwipeManager recyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager;

    private MySwipeableItemAdapter mAdapter;

    private TextView emptyTip;

    private Record lastRecord;
    private int lastPosition;
    private boolean undid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_book_list_view);

        mContext = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("");

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

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

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                Log.d("Saver", "onQueryTextSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                Log.d("Saver", "onQueryTextChange");
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
                Log.d("Saver", "onSearchViewShown");
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                Log.d("Saver", "onSearchViewClosed");
            }
        });

        emptyTip = (TextView)findViewById(R.id.empty_tip);
        emptyTip.setTypeface(Util.GetTypeface());

        if (RecordManager.RECORDS.size() == 0) {
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        } else {

            emptyTip.setVisibility(View.GONE);

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

            recyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
            recyclerViewTouchActionGuardManager.
                    setInterceptVerticalScrollingWhileAnimationRunning(true);
            recyclerViewTouchActionGuardManager.setEnabled(true);

            recyclerViewSwipeManager = new RecyclerViewSwipeManager();

            mAdapter = new MySwipeableItemAdapter(mContext);
            mAdapter.setEventListener(new MySwipeableItemAdapter.EventListener() {

                @Override
                public void onItemRemoved(int position) {
                    activityOnItemRemoved(position);
                }

                @Override
                public void onItemPinned(int position) {
                    activityOnItemPinned(position);
                }

                @Override
                public void onItemViewClicked(View v, boolean pinned) {
                    int position = recyclerView.getChildAdapterPosition(v);
                    if (position != RecyclerView.NO_POSITION) {
                        activityOnItemClicked(position);
                    }
                }
            });

            adapter = mAdapter;
            wrappedAdapter = recyclerViewSwipeManager.createWrappedAdapter(mAdapter);
            final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

            animator.setSupportsChangeAnimations(false);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(wrappedAdapter);
            recyclerView.setItemAnimator(animator);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
            } else {
                recyclerView.addItemDecoration(
                        new ItemShadowDecorator(
                                (NinePatchDrawable) ContextCompat.getDrawable(
                                        mContext, R.drawable.material_shadow_z1)));
            }
            recyclerView.addItemDecoration(new SimpleListDividerDecorator(
                    ContextCompat.getDrawable(mContext, R.drawable.list_divider_h), true));

            // NOTE:
            // The initialization order is very important! This order determines the priority of touch event handling.
            //
            // priority: TouchActionGuard > Swipe > DragAndDrop
            recyclerViewTouchActionGuardManager.attachRecyclerView(recyclerView);
            recyclerViewSwipeManager.attachRecyclerView(recyclerView);

        }

    }

    private void activityOnItemRemoved(int position) {
        Log.d("Saver", "recording");
        lastPosition = RecordManager.RECORDS.size() - position;
        undid = false;
        Snackbar snackbar =
                Snackbar
                        .with(mContext)
                        .type(SnackbarType.MULTI_LINE)
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .position(Snackbar.SnackbarPosition.BOTTOM)
                        .margin(15, 15)
                        .backgroundDrawable(Util.GetSnackBarBackground(-3))
                        .text(mContext.getResources().getString(R.string.deleting))
                        .textTypeface(Util.GetTypeface())
                        .textColor(Color.WHITE)
                        .actionLabelTypeface(Util.GetTypeface())
                        .actionLabel(mContext.getResources()
                                .getString(R.string.undo))
                        .actionColor(Color.WHITE)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                RecordManager.RECORDS.add(lastPosition, Util.backupRecord);
                                mAdapter.notifyItemInserted(
                                        RecordManager.RECORDS.size() - 1 - lastPosition);
                                recyclerView.scrollToPosition(
                                        RecordManager.RECORDS.size() - 1 - lastPosition);
                            }
                        })
                        .eventListener(new EventListener() {
                            @Override
                            public void onShow(Snackbar snackbar) {

                            }

                            @Override
                            public void onShowByReplace(Snackbar snackbar) {

                            }

                            @Override
                            public void onShown(Snackbar snackbar) {

                            }

                            @Override
                            public void onDismiss(Snackbar snackbar) {

                            }

                            @Override
                            public void onDismissByReplace(Snackbar snackbar) {

                            }

                            @Override
                            public void onDismissed(Snackbar snackbar) {
                                RecordManager.deleteRecord(Util.backupRecord.getId(), false);
                            }
                        });
        SnackbarManager.show(snackbar);
    }

    private void activityOnItemPinned(int position) {
        Log.d("Saver", "Edittttt " + position);
        mAdapter.notifyItemChanged(position);
    }

    private void activityOnItemClicked(int position) {
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_book_list_view, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) ||
                super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
            return;
        }
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {

        Intent intent = new Intent();
        intent.putExtra("IS_CHANGED", true);
        setResult(RESULT_OK, intent);

        super.finish();
    }

    @Override
    public void onDestroy() {
        if (recyclerViewSwipeManager != null) {
            recyclerViewSwipeManager.release();
            recyclerViewSwipeManager = null;
        }

        if (recyclerViewTouchActionGuardManager != null) {
            recyclerViewTouchActionGuardManager.release();
            recyclerViewTouchActionGuardManager = null;
        }

        if (recyclerView != null) {
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(null);
            recyclerView = null;
        }

        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter);
            wrappedAdapter = null;
        }
        mAdapter = null;
        layoutManager = null;

        super.onDestroy();
    }
}
