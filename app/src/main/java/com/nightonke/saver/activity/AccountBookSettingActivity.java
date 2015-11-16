package com.nightonke.saver.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.kyleduo.switchbutton.SwitchButton;
import com.nightonke.saver.R;
import com.nightonke.saver.ui.RiseNumberTextView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountBookSettingActivity extends AppCompatActivity
    implements View.OnClickListener {

    private Context mContext;

    private MaterialIconView back;

    private CircleImageView logo;

    private MaterialRippleLayout profileLayout;
    private MaterialIconView userNameIcon;
    private MaterialIconView userEmailIcon;
    private TextView userName;
    private TextView userEmail;
    private RiseNumberTextView expense;
    private RiseNumberTextView records;

    private MaterialRippleLayout monthLayout;
    private MaterialIconView monthIcon;
    private MaterialIconView monthMaxExpenseIcon;
    private MaterialIconView monthColorRemindIcon;
    private MaterialIconView monthForbiddenIcon;
    private SwitchButton monthSB;
    private SwitchButton monthColorRemindSB;
    private SwitchButton monthForbidderSB;
    private TextView monthMaxExpenseTV;

    private MaterialRippleLayout accountBookNameLayout;
    private TextView accountBookNameTV;

    private MaterialRippleLayout changePasswordLayout;

    private MaterialRippleLayout sortTagsLayout;

    private MaterialRippleLayout showPictureLayout;
    private MaterialIconView showPictureIcon;
    private SwitchButton showPictureSB;

    private MaterialRippleLayout hollowLayout;
    private MaterialIconView hollowIcon;
    private SwitchButton hollowSB;

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
        userEmail = (TextView)findViewById(R.id.user_email);
        expense = (RiseNumberTextView)findViewById(R.id.expense);
        records = (RiseNumberTextView)findViewById(R.id.records);
        expense.withNumber(10000).setDuration(2000).start();
        records.withNumber(996).setDuration(2000).start();
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
//        switch (v.getId()) {
//            case
//        }
    }
}
