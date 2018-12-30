package com.nightonke.saver.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.johnpersano.supertoasts.SuperToast;
import com.nightonke.saver.R;
import com.nightonke.saver.adapter.PasswordChangeButtonGridViewAdapter;
import com.nightonke.saver.adapter.PasswordChangeFragmentAdapter;
import com.nightonke.saver.fragment.CoCoinFragmentManager;
import com.nightonke.saver.fragment.PasswordChangeFragment;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.model.User;
import com.nightonke.saver.ui.FixedSpeedScroller;
import com.nightonke.saver.ui.MyGridView;
import com.nightonke.saver.util.CoCoinUtil;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.lang.reflect.Field;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

public class SetPasswordActivity extends AppCompatActivity {

    private Context mContext;

    private MyGridView myGridView;
    private PasswordChangeButtonGridViewAdapter myGridViewAdapter;

    private MaterialIconView back;

    private static final int NEW_PASSWORD = 0;
    private static final int PASSWORD_AGAIN = 1;

    private int CURRENT_STATE = NEW_PASSWORD;

    private String newPassword = "";
    private String againPassword = "";

    private ViewPager viewPager;
    private PasswordChangeFragmentAdapter passwordAdapter;

    private SuperToast superToast;

    private float x1, y1, x2, y2;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        mContext = this;

        int currentapiVersion = Build.VERSION.SDK_INT;

        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.statusBarColor));
        } else{
            // do something for phones running an SDK before lollipop
        }

        viewPager = (ViewPager)findViewById(R.id.viewpager);

        try {
            Interpolator sInterpolator = new AccelerateInterpolator();
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller
                    = new FixedSpeedScroller(viewPager.getContext(), sInterpolator);
            scroller.setmDuration(1000);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        passwordAdapter = new PasswordChangeFragmentAdapter(
                getSupportFragmentManager());

        viewPager.setOffscreenPageLimit(2);
        viewPager.setScrollBarFadeDuration(1000);

        viewPager.setAdapter(passwordAdapter);

        myGridView = (MyGridView)findViewById(R.id.gridview);
        myGridViewAdapter = new PasswordChangeButtonGridViewAdapter(this);
        myGridView.setAdapter(myGridViewAdapter);

        myGridView.setOnItemClickListener(gridViewClickListener);
        myGridView.setOnItemLongClickListener(gridViewLongClickListener);

        myGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        myGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        View lastChild = myGridView.getChildAt(myGridView.getChildCount() - 1);
                        myGridView.setLayoutParams(
                                new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.FILL_PARENT, lastChild.getBottom()));
                    }
                });

        back = (MaterialIconView)findViewById(R.id.back);
        back.setVisibility(View.INVISIBLE);

        superToast = new SuperToast(this);

        title = (TextView)findViewById(R.id.title);
        title.setTypeface(CoCoinUtil.typefaceLatoLight);
        if (SettingManager.getInstance().getFirstTime()) {
            title.setText(mContext.getResources().getString(R.string.app_name));
        } else {
            title.setText(mContext.getResources().getString(R.string.change_password));
        }

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void finish() {
        SuperToast.cancelAllSuperToasts();
        super.finish();
    }

    private AdapterView.OnItemClickListener gridViewClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            buttonClickOperation(false, position);
        }
    };

    private AdapterView.OnItemLongClickListener gridViewLongClickListener
            = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            buttonClickOperation(true, position);
            return true;
        }
    };

    private void buttonClickOperation(boolean longClick, int position) {
        switch (CURRENT_STATE) {
            case NEW_PASSWORD:
                if (CoCoinUtil.ClickButtonDelete(position)) {
                    if (longClick) {
                        CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE].init();
                        newPassword = "";
                    } else {
                        CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE]
                                .clear(newPassword.length() - 1);
                        if (newPassword.length() != 0)
                            newPassword = newPassword.substring(0, newPassword.length() - 1);
                    }
                } else if (CoCoinUtil.ClickButtonCommit(position)) {

                } else {
                    CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE]
                            .set(newPassword.length());
                    newPassword += CoCoinUtil.BUTTONS[position];
                    if (newPassword.length() == 4) {
                        // finish the new password input
                        CURRENT_STATE = PASSWORD_AGAIN;
                        viewPager.setCurrentItem(PASSWORD_AGAIN, true);
                    }
                }
                break;
            case PASSWORD_AGAIN:
                if (CoCoinUtil.ClickButtonDelete(position)) {
                    if (longClick) {
                        CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE].init();
                        againPassword = "";
                    } else {
                        CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE]
                                .clear(againPassword.length() - 1);
                        if (againPassword.length() != 0)
                            againPassword = againPassword.substring(0, againPassword.length() - 1);
                    }
                } else if (CoCoinUtil.ClickButtonCommit(position)) {

                } else {
                    CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE]
                            .set(againPassword.length());
                    againPassword += CoCoinUtil.BUTTONS[position];
                    if (againPassword.length() == 4) {
                        // if the password again is equal to the new password
                        if (againPassword.equals(newPassword)) {
                            CURRENT_STATE = -1;
                            showToast(2);
                            SettingManager.getInstance().setPassword(newPassword);
                            SettingManager.getInstance().setFirstTime(false);
                            if (SettingManager.getInstance().getLoggenOn()) {
                                User currentUser = BmobUser.getCurrentUser(
                                        CoCoinApplication.getAppContext(), User.class);
                                currentUser.setAccountBookPassword(newPassword);
                                currentUser.update(CoCoinApplication.getAppContext(),
                                        currentUser.getObjectId(), new UpdateListener() {
                                            @Override
                                            public void onSuccess() {
                                                Log.d("Saver", "Set password successfully.");
                                            }

                                            @Override
                                            public void onFailure(int code, String msg) {
                                                Log.d("Saver", "Set password failed.");
                                            }
                                        });
                            }
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                        } else {
                            CoCoinFragmentManager.passwordChangeFragment[CURRENT_STATE].clear(4);
                            ((PasswordChangeFragment)passwordAdapter.getItem(CURRENT_STATE - 1)).init();
                            CURRENT_STATE = NEW_PASSWORD;
                            viewPager.setCurrentItem(NEW_PASSWORD, true);
                            newPassword = "";
                            againPassword = "";
                            showToast(1);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showToast(int toastType) {
        SuperToast.cancelAllSuperToasts();

        superToast.setAnimations(CoCoinUtil.TOAST_ANIMATION);
        superToast.setDuration(SuperToast.Duration.SHORT);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);

        switch (toastType) {
            // old password wrong
            case 0:

                superToast.setText(
                        mContext.getResources().getString(R.string.toast_password_wrong));
                superToast.setBackground(SuperToast.Background.RED);
                superToast.getTextView().setTypeface(CoCoinUtil.typefaceLatoLight);

                break;
            // password is different
            case 1:

                superToast.setText(
                        mContext.getResources().getString(R.string.different_password));
                superToast.setBackground(SuperToast.Background.RED);
                superToast.getTextView().setTypeface(CoCoinUtil.typefaceLatoLight);

                break;
            // success
            case 2:

                superToast.setText(
                        mContext.getResources().getString(R.string.set_password_successfully));
                superToast.setBackground(SuperToast.Background.GREEN);
                superToast.getTextView().setTypeface(CoCoinUtil.typefaceLatoLight);

                break;
            default:
                break;
        }

        superToast.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = ev.getX();
                y1 = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = ev.getX();
                y2 = ev.getY();
                if (Math.abs(x1 - x2) > 20) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                x2 = ev.getX();
                y2 = ev.getY();
                if (Math.abs(x1 - x2) > 20) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < 3; i++) {
            CoCoinFragmentManager.passwordChangeFragment[i].onDestroy();
            CoCoinFragmentManager.passwordChangeFragment[i] = null;
        }
        super.onDestroy();
    }

}
