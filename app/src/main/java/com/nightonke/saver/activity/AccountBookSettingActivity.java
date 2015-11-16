package com.nightonke.saver.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

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
            case R.id.profile_layout:
                userOperator();
                break;


        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.month_limit_enable_button:
                if (isChecked) {
                    SettingManager.getInstance().setIsMonthLimit(Boolean.TRUE);
                } else {
                    SettingManager.getInstance().setIsMonthLimit(Boolean.FALSE);
                }
                setMonthState();
                break;
            case R.id.month_color_remind_button:
                if (!monthSB.isChecked()) return;
                if (isChecked) {
                    SettingManager.getInstance().setIsColorRemind(Boolean.TRUE);
                } else {
                    SettingManager.getInstance().setIsColorRemind(Boolean.FALSE);
                }
                setMonthState();
                break;
            case R.id.month_forbidden_button:
                if (!monthSB.isChecked()) return;
                if (isChecked) {
                    SettingManager.getInstance().setIsForbidden(Boolean.TRUE);
                } else {
                    SettingManager.getInstance().setIsForbidden(Boolean.FALSE);
                }
                setMonthState();
                break;
            case R.id.whether_show_picture_button:
                if (isChecked) {
                    SettingManager.getInstance().setShowPicture(Boolean.TRUE);
                } else {
                    SettingManager.getInstance().setShowPicture(Boolean.FALSE);
                }
                setShowPictureState();
                break;
            case R.id.whether_show_circle_button:
                if (isChecked) {
                    SettingManager.getInstance().setIsHollow(Boolean.TRUE);
                } else {
                    SettingManager.getInstance().setIsHollow(Boolean.FALSE);
                }
                setHollowState();
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

    private void init() {
        back = (MaterialIconView)findViewById(R.id.icon_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        logo = (CircleImageView)findViewById(R.id.profile_image);
        profileLayout = (MaterialRippleLayout)findViewById(R.id.profile_layout);
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
        monthColorRemindSB = (SwitchButton)findViewById(R.id.month_color_remind_button);
        monthForbiddenSB = (SwitchButton)findViewById(R.id.month_forbidden_button);
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
        accountBookName = (TextView)findViewById(R.id.account_book_name);
        accountBookName.setTypeface(Util.GetTypeface());
        accountBookNameTV = (TextView)findViewById(R.id.account_book_name_text);
        accountBookNameTV.setTypeface(Util.GetTypeface());

        changePasswordLayout = (MaterialRippleLayout)findViewById(R.id.change_password_layout);
        changePasswordTV = (TextView)findViewById(R.id.change_password_text);
        changePasswordTV.setTypeface(Util.GetTypeface());

        sortTagsLayout = (MaterialRippleLayout)findViewById(R.id.sort_tags_layout);
        sortTagsTV = (TextView)findViewById(R.id.sort_tags_text);
        sortTagsTV.setTypeface(Util.GetTypeface());

        showPictureLayout = (MaterialRippleLayout)findViewById(R.id.whether_show_picture_layout);
        showPictureIcon = (MaterialIconView)findViewById(R.id.whether_show_picture_icon);
        showPictureSB = (SwitchButton)findViewById(R.id.whether_show_picture_button);
        showPictureTV = (TextView)findViewById(R.id.whether_show_picture_text);
        showPictureTV.setTypeface(Util.GetTypeface());

        hollowLayout = (MaterialRippleLayout)findViewById(R.id.whether_show_circle_layout);
        hollowIcon = (MaterialIconView)findViewById(R.id.whether_show_circle_icon);
        hollowSB = (SwitchButton)findViewById(R.id.whether_show_circle_button);
        hollowTV = (TextView)findViewById(R.id.whether_show_circle_text);
        hollowTV.setTypeface(Util.GetTypeface());

        if (SettingManager.getInstance().getLoggenOn()) {
            // is logged on, set the user name and email
            userName.setText(SettingManager.getInstance().getUserName());
            userNameIcon.setEnabled(true);
            userEmail.setText(SettingManager.getInstance().getUserEmail());
            userEmailIcon.setEnabled(true);
        } else {
            userName.setText(mContext.getResources().getString(R.string.default_user_name));
            userNameIcon.setEnabled(false);
            userEmail.setText(mContext.getResources().getString(R.string.default_user_email));
            userEmailIcon.setEnabled(false);
        }

        if (SettingManager.getInstance().getHasLogo()) {
            // is has a logo, just set the logo
            loadLogo();
        } else {
            // load a default image
            // Todo
        }

        setMonthState();

        setShowPictureState();

        setHollowState();

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
        if (SettingManager.getInstance().getIsMonthLimit()) {
            // is month limit
            monthIcon.setEnabled(true);
            monthSB.setCheckedImmediately(true);
            monthMaxExpenseIcon.setEnabled(true);
            monthMaxExpenseTV.setEnabled(true);
            monthMaxExpense.setEnabled(true);
            monthMaxExpense.setText(SettingManager.getInstance().getMonthLimit() + "");
            monthColorRemindTV.setEnabled(true);
            monthColorRemindSB.setEnabled(true);
            monthForbiddenTV.setEnabled(true);
            monthForbiddenSB.setEnabled(true);
            if (SettingManager.getInstance().getIsColorRemind()) {
                monthColorRemindIcon.setEnabled(true);
            } else {
                monthColorRemindIcon.setEnabled(false);
            }
            if (SettingManager.getInstance().getIsForbidden()) {
                monthForbiddenIcon.setEnabled(true);
            } else {
                monthForbiddenIcon.setEnabled(false);
            }
        } else {
            monthIcon.setEnabled(false);
            monthSB.setCheckedImmediately(false);
            monthMaxExpenseIcon.setEnabled(false);
            monthMaxExpenseTV.setEnabled(false);
            monthMaxExpense.setEnabled(false);
            monthMaxExpense.setText(SettingManager.getInstance().getMonthLimit() + "");
            monthColorRemindIcon.setEnabled(false);
            monthColorRemindTV.setEnabled(false);
            monthColorRemindSB.setEnabled(false);
            monthForbiddenIcon.setEnabled(false);
            monthForbiddenTV.setEnabled(false);
            monthForbiddenSB.setEnabled(false);
            if (SettingManager.getInstance().getIsColorRemind()) {
                monthColorRemindSB.setCheckedImmediately(true);
            } else {
                monthColorRemindSB.setCheckedImmediately(true);
            }
            if (SettingManager.getInstance().getIsForbidden()) {
                monthForbiddenSB.setCheckedImmediately(true);
            } else {
                monthForbiddenSB.setCheckedImmediately(false);
            }
        }
    }

    private void setShowPictureState() {
        if (SettingManager.getInstance().getShowPicture()) {
            showPictureIcon.setEnabled(true);
            showPictureSB.setCheckedImmediately(true);
        } else {
            showPictureIcon.setEnabled(false);
            showPictureSB.setCheckedImmediately(false);
        }
    }

    private void setHollowState() {
        if (SettingManager.getInstance().getIsHollow()) {
            hollowIcon.setEnabled(true);
            hollowSB.setCheckedImmediately(true);
        } else {
            hollowIcon.setEnabled(false);
            hollowSB.setCheckedImmediately(false);
        }
    }
}
