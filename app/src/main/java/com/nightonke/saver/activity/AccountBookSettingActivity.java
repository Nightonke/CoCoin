package com.nightonke.saver.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.balysv.materialripple.MaterialRippleLayout;
import com.kyleduo.switchbutton.SwitchButton;
import com.nightonke.saver.R;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.ui.RiseNumberTextView;
import com.nightonke.saver.util.Util;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountBookSettingActivity extends AppCompatActivity
    implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context mContext;

    private MaterialIconView back;

    private CircleImageView logo;

    private MaterialRippleLayout profileLayout;
    private MaterialIconView userNameIcon;
    private MaterialIconView userEmailIcon;
    private TextView userName;
    private TextView userEmail;
    private RiseNumberTextView expense;
    private TextView expenseTV;
    private RiseNumberTextView records;
    private TextView recordsTV;

    private MaterialRippleLayout monthLayout;
    private MaterialIconView monthIcon;
    private MaterialIconView monthMaxExpenseIcon;
    private MaterialIconView monthColorRemindIcon;
    private MaterialIconView monthForbiddenIcon;
    private SwitchButton monthSB;
    private SwitchButton monthColorRemindSB;
    private SwitchButton monthForbiddenSB;
    private TextView monthMaxExpense;
    private TextView monthLimitTV;
    private TextView monthMaxExpenseTV;
    private TextView monthColorRemindTV;
    private TextView monthForbiddenTV;

    private MaterialRippleLayout accountBookNameLayout;
    private TextView accountBookNameTV;
    private TextView accountBookName;

    private MaterialRippleLayout changePasswordLayout;
    private TextView changePasswordTV;

    private MaterialRippleLayout sortTagsLayout;
    private TextView sortTagsTV;

    private MaterialRippleLayout showPictureLayout;
    private MaterialIconView showPictureIcon;
    private SwitchButton showPictureSB;
    private TextView showPictureTV;

    private MaterialRippleLayout hollowLayout;
    private MaterialIconView hollowIcon;
    private SwitchButton hollowSB;
    private TextView hollowTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_account_book_setting);

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
            statusBarView.getLayoutParams().height = Util.getStatusBarHeight();
        }

        init();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image:
                changeLogo();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.month_limit_enable_button:
                SettingManager.getInstance().setIsMonthLimit(isChecked);
                setMonthState();
                break;
            case R.id.month_color_remind_button:
                SettingManager.getInstance().setIsColorRemind(isChecked);
                setIconEnable(monthColorRemindIcon, isChecked
                        && SettingManager.getInstance().getIsMonthLimit());
                break;
            case R.id.month_forbidden_button:
                SettingManager.getInstance().setIsForbidden(isChecked);
                setIconEnable(monthForbiddenIcon, isChecked
                        && SettingManager.getInstance().getIsMonthLimit());
                break;
            case R.id.whether_show_picture_button:
                SettingManager.getInstance().setShowPicture(isChecked);
                setShowPictureState(isChecked);
                break;
            case R.id.whether_show_circle_button:
                SettingManager.getInstance().setIsHollow(isChecked);
                setHollowState(isChecked);
                SettingManager.getInstance().setTodayViewPieShouldChange(Boolean.TRUE);
                break;
            default:
                break;
        }
    }

    private void changeLogo() {
        Toast.makeText(mContext, "Change logo", Toast.LENGTH_SHORT).show();
    }

    private void userOperator() {
        Toast.makeText(mContext, "User operator", Toast.LENGTH_SHORT).show();
    }

    private void changeAccountBookName() {
        new MaterialDialog.Builder(this)
                .theme(Theme.LIGHT)
                .typeface(Util.GetTypeface(), Util.GetTypeface())
                .title(R.string.set_account_book_dialog_title)
                .inputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(1, 16)
                .positiveText(R.string.submit)
                .input(SettingManager.getInstance().getAccountBookName()
                        , null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        SettingManager.getInstance().setAccountBookName(input.toString());
                        SettingManager.getInstance().setTodayViewTitleShouldChange(true);
                        SettingManager.getInstance().setMainViewTitleShouldChange(true);
                        accountBookName.setText(input.toString());
                    }
                }).show();
    }

    private void changePassword() {
        Intent intent = new Intent(mContext, EditPasswordActivity.class);
        startActivity(intent);
    }

    private void sortTags() {
        Intent intent = new Intent(mContext, TagSettingActivity.class);
        startActivity(intent);
    }

    private void init() {
        back = (MaterialIconView)findViewById(R.id.icon_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logo = (CircleImageView)findViewById(R.id.profile_image);
        logo.setOnClickListener(this);
        profileLayout = (MaterialRippleLayout)findViewById(R.id.profile_layout);
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userOperator();
            }
        });
        userNameIcon = (MaterialIconView)findViewById(R.id.user_name_icon);
        userEmailIcon = (MaterialIconView)findViewById(R.id.user_email_icon);
        userName = (TextView)findViewById(R.id.user_name);
        userName.setTypeface(Util.typefaceLatoLight);
        userEmail = (TextView)findViewById(R.id.user_email);
        userEmail.setTypeface(Util.typefaceLatoLight);
        expense = (RiseNumberTextView)findViewById(R.id.expense);
        expense.setTypeface(Util.typefaceLatoLight);
        records = (RiseNumberTextView)findViewById(R.id.records);
        records.setTypeface(Util.typefaceLatoLight);
        expenseTV = (TextView)findViewById(R.id.expense_text);
        expenseTV.setTypeface(Util.GetTypeface());
        recordsTV = (TextView)findViewById(R.id.records_text);
        recordsTV.setTypeface(Util.GetTypeface());

        expense.withNumber(RecordManager.SUM).setDuration(1500).start();
        records.withNumber(RecordManager.RECORDS.size()).setDuration(1500).start();

        monthLayout = (MaterialRippleLayout)findViewById(R.id.month_layout);
        monthIcon = (MaterialIconView)findViewById(R.id.month_limit_icon);
        monthMaxExpenseIcon = (MaterialIconView)findViewById(R.id.month_expense_icon);
        monthColorRemindIcon = (MaterialIconView)findViewById(R.id.month_color_icon);
        monthForbiddenIcon = (MaterialIconView)findViewById(R.id.month_forbidden_icon);
        monthSB = (SwitchButton)findViewById(R.id.month_limit_enable_button);
        monthSB.setOnCheckedChangeListener(this);
        monthColorRemindSB = (SwitchButton)findViewById(R.id.month_color_remind_button);
        monthColorRemindSB.setOnCheckedChangeListener(this);
        monthForbiddenSB = (SwitchButton)findViewById(R.id.month_forbidden_button);
        monthForbiddenSB.setOnCheckedChangeListener(this);
        monthMaxExpense = (TextView)findViewById(R.id.month_expense);
        monthMaxExpense.setTypeface(Util.typefaceLatoLight);
        monthLimitTV = (TextView)findViewById(R.id.month_limit_text);
        monthLimitTV.setTypeface(Util.GetTypeface());
        monthMaxExpenseTV = (TextView)findViewById(R.id.month_expense_text);
        monthMaxExpenseTV.setTypeface(Util.GetTypeface());
        monthColorRemindTV = (TextView)findViewById(R.id.month_color_remind_text);
        monthColorRemindTV.setTypeface(Util.GetTypeface());
        monthForbiddenTV = (TextView)findViewById(R.id.month_forbidden_text);
        monthForbiddenTV.setTypeface(Util.GetTypeface());

        accountBookNameLayout = (MaterialRippleLayout)findViewById(R.id.account_book_name_layout);
        accountBookNameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeAccountBookName();
            }
        });
        accountBookName = (TextView)findViewById(R.id.account_book_name);
        accountBookName.setTypeface(Util.GetTypeface());
        accountBookName.setText(SettingManager.getInstance().getAccountBookName());
        accountBookNameTV = (TextView)findViewById(R.id.account_book_name_text);
        accountBookNameTV.setTypeface(Util.GetTypeface());

        changePasswordLayout = (MaterialRippleLayout)findViewById(R.id.change_password_layout);
        changePasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        changePasswordTV = (TextView)findViewById(R.id.change_password_text);
        changePasswordTV.setTypeface(Util.GetTypeface());

        sortTagsLayout = (MaterialRippleLayout)findViewById(R.id.sort_tags_layout);
        sortTagsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortTags();
            }
        });
        sortTagsTV = (TextView)findViewById(R.id.sort_tags_text);
        sortTagsTV.setTypeface(Util.GetTypeface());

        showPictureLayout = (MaterialRippleLayout)findViewById(R.id.whether_show_picture_layout);
        showPictureIcon = (MaterialIconView)findViewById(R.id.whether_show_picture_icon);
        showPictureSB = (SwitchButton)findViewById(R.id.whether_show_picture_button);
        showPictureSB.setOnCheckedChangeListener(this);
        showPictureTV = (TextView)findViewById(R.id.whether_show_picture_text);
        showPictureTV.setTypeface(Util.GetTypeface());

        hollowLayout = (MaterialRippleLayout)findViewById(R.id.whether_show_circle_layout);
        hollowIcon = (MaterialIconView)findViewById(R.id.whether_show_circle_icon);
        hollowSB = (SwitchButton)findViewById(R.id.whether_show_circle_button);
        hollowSB.setOnCheckedChangeListener(this);
        hollowTV = (TextView)findViewById(R.id.whether_show_circle_text);
        hollowTV.setTypeface(Util.GetTypeface());

        boolean loggenOn = SettingManager.getInstance().getLoggenOn();
        if (loggenOn) {
            // is logged on, set the user name and email
            userName.setText(SettingManager.getInstance().getUserName());
            userEmail.setText(SettingManager.getInstance().getUserEmail());
        } else {
            userName.setText(mContext.getResources().getString(R.string.default_user_name));
            userEmail.setText(mContext.getResources().getString(R.string.default_user_email));
        }
        setIconEnable(userNameIcon, loggenOn);
        setIconEnable(userEmailIcon, loggenOn);

        if (SettingManager.getInstance().getHasLogo()) {
            // is has a logo, just set the logo
            loadLogo();
        } else {
            // load a default image
            // Todo
        }

        monthSB.setCheckedImmediately(SettingManager.getInstance().getIsMonthLimit());
        setMonthState();

        showPictureSB.setCheckedImmediately(SettingManager.getInstance().getShowPicture());
        setShowPictureState(SettingManager.getInstance().getShowPicture());

        hollowSB.setCheckedImmediately(SettingManager.getInstance().getIsHollow());
        setHollowState(SettingManager.getInstance().getIsHollow());
    }

    private void loadLogo() {
        try {
            File f = new File(SettingManager.getInstance().getProfileImageDir(),
                    SettingManager.getInstance().getProfileImageName());
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            logo.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private void setMonthState() {
        boolean isMonthLimit = SettingManager.getInstance().getIsMonthLimit();
        boolean isMonthColorRemind = SettingManager.getInstance().getIsColorRemind();
        boolean isForbidden = SettingManager.getInstance().getIsForbidden();

        setIconEnable(monthIcon, isMonthLimit);
        setIconEnable(monthMaxExpenseIcon, isMonthLimit);
        setTVEnable(monthMaxExpenseTV, isMonthLimit);
        setTVEnable(monthMaxExpense, isMonthLimit);
        setTVEnable(monthColorRemindTV, isMonthLimit);
        setTVEnable(monthForbiddenTV, isMonthLimit);
        monthMaxExpense.setText(SettingManager.getInstance().getMonthLimit() + "");

        setIconEnable(monthColorRemindIcon, isMonthLimit && isMonthColorRemind);
        setIconEnable(monthForbiddenIcon, isMonthLimit && isForbidden);

        monthColorRemindSB.setEnabled(isMonthLimit);
        monthColorRemindSB.setCheckedImmediately(
                SettingManager.getInstance().getIsColorRemind());
        monthForbiddenSB.setEnabled(isMonthLimit);
        monthForbiddenSB.setCheckedImmediately(
                SettingManager.getInstance().getIsForbidden());
    }

    private void setShowPictureState(boolean isChecked) {
        setIconEnable(showPictureIcon, isChecked);
    }

    private void setHollowState(boolean isChecked) {
        setIconEnable(hollowIcon, isChecked);
    }

    private void setIconEnable(MaterialIconView icon, boolean enable) {
        if (enable) icon.setColor(mContext.getResources().getColor(R.color.my_blue));
        else icon.setColor(mContext.getResources().getColor(R.color.my_gray));
    }

    private void setTVEnable(TextView tv, boolean enable) {
        if (enable) tv.setTextColor(mContext.getResources().getColor(R.color.drawer_text));
        else tv.setTextColor(mContext.getResources().getColor(R.color.my_gray));
    }
}
