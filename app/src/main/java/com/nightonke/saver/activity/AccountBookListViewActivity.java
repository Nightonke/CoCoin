package com.nightonke.saver.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.NinePatchDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.github.johnpersano.supertoasts.SuperToast;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.MySwipeableItemAdapter;
import com.nightonke.saver.model.CoCoin;
import com.nightonke.saver.model.CoCoinRecord;
import com.nightonke.saver.model.Logo;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.model.User;
import com.nightonke.saver.ui.CustomSliderView;
import com.nightonke.saver.ui.CustomTitleSliderView;
import com.nightonke.saver.util.CoCoinUtil;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

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

    private int lastPosition;
    private boolean undid = false;

    private final int EDITTING_RECORD = 0;

    private VerticalRecyclerViewFastScroller verticalRecyclerViewFastScroller;

    private double originalSum;

    private CircleImageView profileImage;
    private SliderLayout mDemoSlider;

    private SliderLayout titleSlider;

    private TextView userName;
    private TextView userEmail;

    private double LEFT_MONEY = 0;
    private double RIGHT_MONEY = 99999;
    private int TAG_ID = -1;
    private Calendar LEFT_CALENDAR = null;
    private Calendar RIGHT_CALENDAR = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_book_list_view);

        mContext = this;

        userName = (TextView)findViewById(R.id.user_name);
        userEmail = (TextView)findViewById(R.id.user_email);
        userName.setTypeface(CoCoinUtil.typefaceLatoRegular);
        userEmail.setTypeface(CoCoinUtil.typefaceLatoLight);

        User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
        if (user != null) {
            userName.setText(user.getUsername());
            userEmail.setText(user.getEmail());
        }

        originalSum = RecordManager.getInstance(mContext).SUM;

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
            View statusBarView = (View)findViewById(R.id.status_bar_view);
            statusBarView.getLayoutParams().height = CoCoinUtil.getStatusBarHeight();
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
                // Todo
                if (newText.length() == 1) {
                    if (CoCoinUtil.isNumber(newText.charAt(0))) {

                    }
                } else if (newText.length() == 2) {

                }

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
        emptyTip.setTypeface(CoCoinUtil.GetTypeface());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);

        recyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        recyclerViewTouchActionGuardManager.
                setInterceptVerticalScrollingWhileAnimationRunning(true);
        recyclerViewTouchActionGuardManager.setEnabled(true);

        recyclerViewSwipeManager = new RecyclerViewSwipeManager();

        RecordManager.SELECTED_RECORDS = RecordManager.RECORDS;

        mAdapter = new MySwipeableItemAdapter(mContext, RecordManager.SELECTED_RECORDS);
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

        verticalRecyclerViewFastScroller
                = (VerticalRecyclerViewFastScroller)findViewById(R.id.fast_scroller);

        // Connect the recycler to the scroller (to let the scroller scroll the list)
        verticalRecyclerViewFastScroller.setRecyclerView(recyclerView);

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
        recyclerView.setOnScrollListener(
                verticalRecyclerViewFastScroller.getOnScrollListener());

        CoCoinUtil.backupCoCoinRecord = null;

        if (RecordManager.RECORDS.size() == 0) {
            emptyTip.setVisibility(View.VISIBLE);
            verticalRecyclerViewFastScroller.setVisibility(View.INVISIBLE);
        } else {
            emptyTip.setVisibility(View.GONE);
            verticalRecyclerViewFastScroller.setVisibility(View.VISIBLE);
        }

        profileImage= (CircleImageView)mDrawer.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SettingManager.getInstance().getLoggenOn()) {
                    CoCoinUtil.showToast(mContext, R.string.change_logo_tip);
                } else {
                    CoCoinUtil.showToast(mContext, R.string.login_tip);
                }
            }
        });

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String, Integer> urls = CoCoinUtil.GetDrawerTopUrl();

        for(String name : urls.keySet()){
            CustomSliderView customSliderView = new CustomSliderView(this);
            // initialize a SliderLayout
            customSliderView
                    .image(urls.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.addSlider(customSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

        titleSlider = (SliderLayout)findViewById(R.id.title_slider);
        titleSlider.getLayoutParams().height = CoCoinUtil.getToolBarHeight(mContext);
        titleSlider.getLayoutParams().width = CoCoinUtil.GetScreenWidth(mContext) - CoCoinUtil.dpToPx(60 * 2);

        HashMap<String, Integer> urls2 = CoCoinUtil.getTransparentUrls();

        CustomTitleSliderView customTitleSliderView = new CustomTitleSliderView(mContext, RecordManager.getInstance(mContext).SELECTED_RECORDS.size() + "'s");
        customTitleSliderView
                .image(urls2.get("0"))
                .setScaleType(BaseSliderView.ScaleType.Fit);
        titleSlider.addSlider(customTitleSliderView);

        customTitleSliderView = new CustomTitleSliderView(mContext, CoCoinUtil.GetInMoney((int)(double)RecordManager.getInstance(mContext).SUM));
        customTitleSliderView
                .image(urls2.get("1"))
                .setScaleType(BaseSliderView.ScaleType.Fit);
        titleSlider.addSlider(customTitleSliderView);

        titleSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        titleSlider.setCustomAnimation(new DescriptionAnimation());
        titleSlider.setDuration(4000);
        titleSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

        loadLogo();
    }

    private void selectRecords() {
        RecordManager.getInstance(mContext).SELECTED_SUM = 0d;
        if (RecordManager.getInstance(mContext).SELECTED_RECORDS == null) {
            RecordManager.getInstance(mContext).SELECTED_RECORDS = new LinkedList<>();
        } else {
            RecordManager.getInstance(mContext).SELECTED_RECORDS.clear();
        }
        for (int i = RecordManager.getInstance(mContext).RECORDS.size() - 1; i >= 0; i--) {
            CoCoinRecord record = RecordManager.getInstance(mContext).RECORDS.get(i);
            if (inMoney(record) && inTag(record) && inTime(record)) {
                RecordManager.getInstance(mContext).SELECTED_SUM += record.getMoney();
                RecordManager.getInstance(mContext).SELECTED_RECORDS.add(record);
            }
        }

        originalSum = RecordManager.getInstance(mContext).SELECTED_SUM;
        mAdapter.notifyDataSetChanged();

        titleSlider.removeAllSliders();

        HashMap<String, Integer> urls2 = CoCoinUtil.getTransparentUrls();

        CustomTitleSliderView customTitleSliderView = new CustomTitleSliderView(mContext, RecordManager.getInstance(mContext).SELECTED_RECORDS.size() + "'s");
        customTitleSliderView
                .image(urls2.get("0"))
                .setScaleType(BaseSliderView.ScaleType.Fit);
        titleSlider.addSlider(customTitleSliderView);

        customTitleSliderView = new CustomTitleSliderView(mContext, CoCoinUtil.GetInMoney((int)(double)RecordManager.getInstance(mContext).SUM));
        customTitleSliderView
                .image(urls2.get("1"))
                .setScaleType(BaseSliderView.ScaleType.Fit);
        titleSlider.addSlider(customTitleSliderView);

        titleSlider.startAutoCycle();
    }

    private boolean inMoney(CoCoinRecord record) {
        return LEFT_MONEY <= record.getMoney() && record.getMoney() <= RIGHT_MONEY;
    }

    private boolean inTag(CoCoinRecord record) {
        if (TAG_ID == -1) return true;
        else return record.getTag() == TAG_ID;
    }

    private boolean inTime(CoCoinRecord record) {
        if (LEFT_CALENDAR == null || RIGHT_CALENDAR == null) return true;
        else return !record.getCalendar().before(LEFT_CALENDAR) && !record.getCalendar().after(RIGHT_CALENDAR);
    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        if (titleSlider != null) titleSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onResume() {
        if (mDemoSlider != null) mDemoSlider.startAutoCycle();
        if (RecordManager.SELECTED_RECORDS == null) selectRecords();
        else {
            if (titleSlider != null) titleSlider.startAutoCycle();
        }
        super.onResume();
    }

    private void activityOnItemRemoved(int position) {

        if (RecordManager.SELECTED_RECORDS.size() == 0) {
            emptyTip.setVisibility(View.VISIBLE);
            verticalRecyclerViewFastScroller.setVisibility(View.INVISIBLE);
        }

        lastPosition = RecordManager.SELECTED_RECORDS.size() - position;
        undid = false;
        Snackbar snackbar =
                Snackbar
                        .with(mContext)
                        .type(SnackbarType.MULTI_LINE)
                        .duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .position(Snackbar.SnackbarPosition.BOTTOM)
                        .margin(15, 15)
                        .backgroundDrawable(CoCoinUtil.GetSnackBarBackground(-3))
                        .text(mContext.getResources().getString(R.string.deleting))
                        .textTypeface(CoCoinUtil.GetTypeface())
                        .textColor(Color.WHITE)
                        .actionLabelTypeface(CoCoinUtil.GetTypeface())
                        .actionLabel(mContext.getResources()
                                .getString(R.string.undo))
                        .actionColor(Color.WHITE)
                        .actionListener(new ActionClickListener() {
                            @Override
                            public void onActionClicked(Snackbar snackbar) {
                                RecordManager.SELECTED_RECORDS.add(lastPosition, CoCoinUtil.backupCoCoinRecord);
                                CoCoinUtil.backupCoCoinRecord = null;
                                LinearLayoutManager linearLayoutManager
                                        = (LinearLayoutManager) recyclerView.getLayoutManager();
                                int firstVisiblePosition = linearLayoutManager
                                        .findFirstCompletelyVisibleItemPosition();
                                int lastVisiblePosition = linearLayoutManager
                                        .findLastCompletelyVisibleItemPosition();
                                final int insertPosition
                                        = RecordManager.SELECTED_RECORDS.size() - 1 - lastPosition;
                                if (firstVisiblePosition < insertPosition
                                        && insertPosition <= lastVisiblePosition) {

                                } else {
                                    recyclerView.scrollToPosition(insertPosition);
                                }
                                mAdapter.notifyItemInserted(insertPosition);
                                mAdapter.notifyDataSetChanged();

                                if (RecordManager.SELECTED_RECORDS.size() != 0) {
                                    emptyTip.setVisibility(View.GONE);
                                    verticalRecyclerViewFastScroller.setVisibility(View.VISIBLE);
                                }
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
                                if (CoCoinUtil.backupCoCoinRecord != null) {
                                    RecordManager.deleteRecord(CoCoinUtil.backupCoCoinRecord, false);
                                }
                                CoCoinUtil.backupCoCoinRecord = null;
                            }

                            @Override
                            public void onDismissByReplace(Snackbar snackbar) {
                                if (CoCoinUtil.backupCoCoinRecord != null) {
                                    RecordManager.deleteRecord(CoCoinUtil.backupCoCoinRecord, false);
                                }
                                CoCoinUtil.backupCoCoinRecord = null;
                            }

                            @Override
                            public void onDismissed(Snackbar snackbar) {
                                if (CoCoinUtil.backupCoCoinRecord != null) {
                                    RecordManager.deleteRecord(CoCoinUtil.backupCoCoinRecord, false);
                                }
                                CoCoinUtil.backupCoCoinRecord = null;
                            }
                        });
        SnackbarManager.show(snackbar);
    }

    private void activityOnItemPinned(int position) {
        mAdapter.notifyItemChanged(position);
        Intent intent = new Intent(mContext, EditRecordActivity.class);
        intent.putExtra("POSITION", position);
        startActivityForResult(intent, EDITTING_RECORD);
    }

    private void activityOnItemClicked(int position) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDITTING_RECORD:
                if (resultCode == RESULT_OK) {
                    final int position = data.getIntExtra("POSITION", -1);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.setPinned(false, position);
                            mAdapter.notifyItemChanged(position);
                        }
                    }, 500);
                }
                break;
            default:
                break;
        }
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

        SettingManager.getInstance().setRecordIsUpdated(true);

        if (RecordManager.SELECTED_SUM != originalSum) {
            SettingManager.getInstance().setTodayViewMonthExpenseShouldChange(true);
        }

        if (CoCoinUtil.backupCoCoinRecord != null) {
            RecordManager.deleteRecord(CoCoinUtil.backupCoCoinRecord, false);
        }
        CoCoinUtil.backupCoCoinRecord = null;

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

        SuperToast.cancelAllSuperToasts();

        super.onDestroy();
    }

    private void loadLogo() {
        User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
        if (user != null) {
            try {
                File logoFile = new File(CoCoinApplication.getAppContext().getFilesDir() + CoCoinUtil.LOGO_NAME);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(logoFile));
                if (b == null) {
                    // the local logo file is missed
                    // try to get from the server
                    BmobQuery<Logo> bmobQuery = new BmobQuery();
                    bmobQuery.addWhereEqualTo("objectId", user.getLogoObjectId());
                    bmobQuery.findObjects(CoCoinApplication.getAppContext()
                            , new FindListener<Logo>() {
                        @Override
                        public void onSuccess(List<Logo> object) {
                            // there has been an old logo in the server/////////////////////////////////////////////////////////
                            Log.d("Saver", "There is an old logo");
                            String url = object.get(0).getFile().getUrl();
                            Ion.with(CoCoinApplication.getAppContext()).load(url)
                                    .write(new File(CoCoinApplication.getAppContext().getFilesDir()
                                            + CoCoinUtil.LOGO_NAME))
                                    .setCallback(new FutureCallback<File>() {
                                        @Override
                                        public void onCompleted(Exception e, File file) {
                                            profileImage.setImageBitmap(BitmapFactory.decodeFile(
                                                    CoCoinApplication.getAppContext().getFilesDir()
                                                            + CoCoinUtil.LOGO_NAME));
                                        }
                                    });
                        }
                        @Override
                        public void onError(int code, String msg) {
                            // the picture is lost
                            Log.d("Saver", "Can't find the old logo in server.");
                        }
                    });
                } else {
                    // the user logo is in the storage
                    profileImage.setImageBitmap(b);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            // use the default logo
            profileImage.setImageResource(R.drawable.default_user_logo);
        }
    }

}
