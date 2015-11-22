package com.nightonke.saver.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.afollestad.materialdialogs.internal.MDButton;
import com.balysv.materialripple.MaterialRippleLayout;
import com.github.johnpersano.supertoasts.SuperToast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.nightonke.saver.R;
import com.nightonke.saver.model.Logo;
import com.nightonke.saver.model.RecordManager;
import com.nightonke.saver.model.SettingManager;
import com.nightonke.saver.model.User;
import com.nightonke.saver.ui.RiseNumberTextView;
import com.nightonke.saver.util.EmailValidator;
import com.nightonke.saver.util.Util;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Switch;
import com.rey.material.widget.Switch.OnCheckedChangeListener;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountBookSettingActivity extends AppCompatActivity
    implements
        View.OnClickListener,
        ColorChooserDialog.ColorCallback,
        OnCheckedChangeListener {

    private final int UPDATE_LOGO = 0;
    private final int UPDATE_IS_MONTH_LIMIT = 1;
    private final int UPDATE_MONTH_LIMIT = 2;
    private final int UPDATE_IS_COLOR_REMIND = 3;
    private final int UPDATE_MONTH_WARNING = 4;
    private final int UPDATE_REMIND_COLOR = 5;
    private final int UPDATE_IS_FORBIDDEN = 6;
    private final int UPDATE_ACCOUNT_BOOK_NAME = 7;
    private final int UPDATE_ACCOUNT_BOOK_PASSWORD = 8;
    private final int UPDATE_SHOW_PICTURE = 9;
    private final int UPDATE_IS_HOLLOW = 10;

    private Context mContext;

    private MaterialIconView back;

    private File logoFile;
    private CircleImageView logo;
    private Bitmap logoBitmap;

    private MaterialEditText registerUserName;
    private MaterialEditText registerUserEmail;
    private MaterialEditText registerPassword;
    private MaterialEditText loginUserName;
    private MaterialEditText loginPassword;

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
    private MaterialIconView monthWarningIcon;
    private MaterialIconView monthColorRemindTypeIcon;
    private MaterialIconView monthForbiddenIcon;
    private Switch monthSB;
    private Switch monthColorRemindSB;
    private Switch monthForbiddenSB;
    private RiseNumberTextView monthMaxExpense;
    private RiseNumberTextView monthWarning;
    private MaterialIconView monthColorRemindSelect;
    private TextView monthLimitTV;
    private TextView monthMaxExpenseTV;
    private TextView monthColorRemindTV;
    private TextView monthWarningTV;
    private TextView monthColorRemindTypeTV;
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
    private Switch showPictureSB;
    private TextView showPictureTV;

    private MaterialRippleLayout hollowLayout;
    private MaterialIconView hollowIcon;
    private Switch hollowSB;
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
            View statusBarView = findViewById(R.id.status_bar_view);
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
        SuperToast.cancelAllSuperToasts();
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

// switch change listener///////////////////////////////////////////////////////////////////////////
    @Override
    public void onCheckedChanged(Switch view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.month_limit_enable_button:
                SettingManager.getInstance().setIsMonthLimit(isChecked);
                updateSettingsToServer(UPDATE_IS_MONTH_LIMIT);
                SettingManager.getInstance().setMainViewMonthExpenseShouldChange(true);
                SettingManager.getInstance().setMainViewRemindColorShouldChange(true);
                SettingManager.getInstance().setTodayViewMonthExpenseShouldChange(true);
                setMonthState();
                break;
            case R.id.month_color_remind_button:
                SettingManager.getInstance().setIsColorRemind(isChecked);
                updateSettingsToServer(UPDATE_IS_COLOR_REMIND);
                SettingManager.getInstance().setMainViewRemindColorShouldChange(true);
                setIconEnable(monthColorRemindIcon, isChecked
                        && SettingManager.getInstance().getIsMonthLimit());
                setIconEnable(monthColorRemindTypeIcon, isChecked
                        && SettingManager.getInstance().getIsMonthLimit());
                setIconEnable(monthWarningIcon, isChecked
                        && SettingManager.getInstance().getIsMonthLimit());
                if (isChecked && SettingManager.getInstance().getIsMonthLimit()) {
                    monthColorRemindSelect.setEnabled(true);
                    monthColorRemindSelect
                            .setColor(SettingManager.getInstance().getRemindColor());
                    monthWarning.setEnabled(true);
                    monthWarning.setTextColor(
                            ContextCompat.getColor(mContext, R.color.drawer_text));
                } else {
                    monthColorRemindSelect.setEnabled(false);
                    monthColorRemindSelect
                            .setColor(mContext.getResources().getColor(R.color.my_gray));
                    monthWarning.setEnabled(false);
                    monthWarning.setTextColor(
                            ContextCompat.getColor(mContext, R.color.my_gray));
                }
                setTVEnable(monthColorRemindTypeTV, isChecked
                        && SettingManager.getInstance().getIsMonthLimit());
                setTVEnable(monthWarningTV, isChecked
                        && SettingManager.getInstance().getIsMonthLimit());
                break;
            case R.id.month_forbidden_button:
                SettingManager.getInstance().setIsForbidden(isChecked);
                updateSettingsToServer(UPDATE_IS_FORBIDDEN);
                setIconEnable(monthForbiddenIcon, isChecked
                        && SettingManager.getInstance().getIsMonthLimit());
                break;
            case R.id.whether_show_picture_button:
                SettingManager.getInstance().setShowPicture(isChecked);
                updateSettingsToServer(UPDATE_SHOW_PICTURE);
                setShowPictureState(isChecked);
                break;
            case R.id.whether_show_circle_button:
                SettingManager.getInstance().setIsHollow(isChecked);
                updateSettingsToServer(UPDATE_IS_HOLLOW);
                setHollowState(isChecked);
                SettingManager.getInstance().setTodayViewPieShouldChange(Boolean.TRUE);
                break;
            default:
                break;
        }
    }

// Load logo from local/////////////////////////////////////////////////////////////////////////////
    private void loadLogo() {
        try {
            logoFile = new File(Util.LOGO_PATH, Util.LOGO_NAME);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(logoFile));
            if (b == null) {
                // the local logo file is missed
                User user = getCurrentUser();
                if (user != null) {
                    // try to get from the server
                    BmobQuery bmobQuery = new BmobQuery();
                    bmobQuery.addWhereEqualTo("objectId", user.getLogoObjectId());
                    bmobQuery.findObjects(CoCoinApplication.getAppContext()
                            , new FindListener<Logo>() {
                        @Override
                        public void onSuccess(List<Logo> object) {
// there has been an old logo in the server/////////////////////////////////////////////////////////
                            Log.d("Saver", "There is an old logo");
                            String url = object.get(0).getFile().getUrl();
                            Ion.with(CoCoinApplication.getAppContext()).load(url)
                                    .write(new File(Util.LOGO_PATH, Util.LOGO_NAME))
                                    .setCallback(new FutureCallback<File>() {
                                        @Override
                                        public void onCompleted(Exception e, File file) {
                                            logo.setImageBitmap(BitmapFactory.
                                                    decodeFile(Util.LOGO_PATH + Util.LOGO_NAME));
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
                    // use the default logo
                    logo.setImageResource(R.drawable.default_user_logo);
                }
            } else {
                // the user logo is in the storage
                logo.setImageBitmap(b);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

// change the user logo/////////////////////////////////////////////////////////////////////////////
    private void changeLogo() {
        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.donation_icon)
                .typeface(Util.GetTypeface(), Util.GetTypeface())
                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                .title(R.string.change_logo_title)
                .content(R.string.change_logo_content)
                .positiveText(R.string.from_gallery)
                .negativeText(R.string.from_camera)
                .neutralText(R.string.cancel)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        if (which == DialogAction.POSITIVE) {
                            Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                            intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent1, 1);
                        } else if (which == DialogAction.NEGATIVE) {
                            Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                    Util.LOGO_NAME)));
                            startActivityForResult(intent2, 2);
                        }
                    }
                })
                .show();
    }

// Crop a picture///////////////////////////////////////////////////////////////////////////////////
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX : aspectY
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY the height and width
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

// After select a picture///////////////////////////////////////////////////////////////////////////
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                // after select from gallery
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());
                }
                break;
            case 2:
                // after taking a photo
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + Util.LOGO_NAME);
                    cropPhoto(Uri.fromFile(temp));
                }
                break;
            case 3:
                // after crop the picture
                if (data != null) {
                    Bundle extras = data.getExtras();
                    logoBitmap = extras.getParcelable("data");
                    if(logoBitmap != null) {
                        setPicToView(logoBitmap);
                        logo.setImageBitmap(logoBitmap);
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

// Storage a picture////////////////////////////////////////////////////////////////////////////////
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(Util.LOGO_PATH);
        file.mkdirs();// 创建文件夹
        file = new File(Util.LOGO_PATH + Util.LOGO_NAME);
        String fileName = Util.LOGO_PATH + Util.LOGO_NAME;  // get logo position
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);  // write the data to file
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // close
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        final User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
// if login/////////////////////////////////////////////////////////////////////////////////////////
        if (user != null) {
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    String url = bmobFile.getFileUrl(CoCoinApplication.getAppContext());
                    Log.d("Saver", "Upload successfully " + url);
                    final Logo logo = new Logo(bmobFile);
                    BmobQuery bmobQuery = new BmobQuery();
                    bmobQuery.addWhereEqualTo("objectId", user.getObjectId());
                    bmobQuery.findObjects(CoCoinApplication.getAppContext()
                            , new FindListener<Logo>() {
                        @Override
                        public void onSuccess(List<Logo> object) {
// there has been an old logo in the server/////////////////////////////////////////////////////////
                            // delete the old logo file
                            BmobFile file = new BmobFile();
                            file.setUrl(logo.getFile().getUrl());
                            file.delete(CoCoinApplication.getAppContext(), new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d("Saver", "Successfully delete the old logo.");
                                    // update the logo object
                                    logo.update(CoCoinApplication.getAppContext(),
                                            user.getLogoObjectId(), new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                    Log.d("Saver", "Update logo successfully");
                                                }

                                                @Override
                                                public void onFailure(int arg0, String arg1) {
                                                    Log.d("Saver", "Update logo failed " + arg1);
                                                }
                                            });
                                }

                                @Override
                                public void onFailure(int code, String msg) {
                                    Log.d("Saver", "Fail to delete the old logo. " + msg);
                                }
                            });
                        }
                        @Override
                        public void onError(int code, String msg) {
// this is a new logo///////////////////////////////////////////////////////////////////////////////
                            logo.save(CoCoinApplication.getAppContext(), new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d("Saver", "Insert Bmobject successfully");
                                    User user = getCurrentUser();
                                    user.setLogoObjectId(logo.getObjectId());
                                    updateSettingsToServer(UPDATE_LOGO);
                                }

                                @Override
                                public void onFailure(int arg0, String arg1) {
                                    Log.d("Saver", "Insert Bmobject failed " + arg1);
                                }
                            });
                        }
                    });
                }
                @Override
                public void onProgress(Integer arg0) {}
                @Override
                public void onFailure(int arg0, String arg1) {Log.d("Saver", "Upload failed " + arg1);}
            });
        }
    }

// download logo to local///////////////////////////////////////////////////////////////////////////
    private void downloadLogoFromServer() {
        User user = getCurrentUser();
        BmobQuery bmobQuery = new BmobQuery();
        bmobQuery.addWhereEqualTo("objectId", user.getLogoObjectId());
        bmobQuery.findObjects(CoCoinApplication.getAppContext()
                , new FindListener<Logo>() {
            @Override
            public void onSuccess(List<Logo> object) {
// there has been an old logo in the server/////////////////////////////////////////////////////////
                Log.d("Saver", "There is an old logo");
                String url = object.get(0).getFile().getUrl();
                Ion.with(CoCoinApplication.getAppContext()).load(url)
                        .write(new File(Util.LOGO_PATH, Util.LOGO_NAME))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File file) {
                                logo.setImageBitmap(BitmapFactory.
                                        decodeFile(Util.LOGO_PATH + Util.LOGO_NAME));
                            }
                        });
            }
            @Override
            public void onError(int code, String msg) {
                // the picture is lost
                Log.d("Saver", "Can't find the old logo in server.");
            }
        });
    }

// update a logo to server//////////////////////////////////////////////////////////////////////////
    private void uploadLogoToServer() {
        File file = new File(Util.LOGO_PATH + Util.LOGO_NAME);
        final User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
// if login/////////////////////////////////////////////////////////////////////////////////////////
        if (user != null) {
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(this, new UploadFileListener() {
                @Override
                public void onSuccess() {
                    String url = bmobFile.getFileUrl(CoCoinApplication.getAppContext());
                    Log.d("Saver", "Upload successfully " + url);
                    final Logo logo = new Logo(bmobFile);
                    BmobQuery bmobQuery = new BmobQuery();
                    bmobQuery.addWhereEqualTo("objectId", user.getLogoObjectId());
                    bmobQuery.findObjects(CoCoinApplication.getAppContext()
                            , new FindListener<Logo>() {
                        @Override
                        public void onSuccess(List<Logo> object) {
// there has been an old logo in the server/////////////////////////////////////////////////////////
                            // delete the old logo file
                            BmobFile file = new BmobFile();
                            file.setUrl(logo.getFile().getUrl());
                            file.delete(CoCoinApplication.getAppContext(), new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d("Saver", "Successfully delete the old logo.");
                                    // update the logo object
                                    logo.update(CoCoinApplication.getAppContext(),
                                            user.getLogoObjectId(), new UpdateListener() {
                                                @Override
                                                public void onSuccess() {
                                                    Log.d("Saver", "Update logo successfully");
                                                }

                                                @Override
                                                public void onFailure(int arg0, String arg1) {
                                                    Log.d("Saver", "Update logo failed " + arg1);
                                                }
                                            });
                                }

                                @Override
                                public void onFailure(int code, String msg) {
                                    Log.d("Saver", "Fail to delete the old logo. " + msg);
                                }
                            });
                        }
                        @Override
                        public void onError(int code, String msg) {
// this is a new logo///////////////////////////////////////////////////////////////////////////////
                            logo.save(CoCoinApplication.getAppContext(), new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    Log.d("Saver", "Insert Bmobject successfully");
                                    User user = getCurrentUser();
                                    user.setLogoObjectId(logo.getObjectId());
                                    updateSettingsToServer(UPDATE_LOGO);
                                }

                                @Override
                                public void onFailure(int arg0, String arg1) {
                                    Log.d("Saver", "Insert Bmobject failed " + arg1);
                                }
                            });
                        }
                    });
                }
                @Override
                public void onProgress(Integer arg0) {}
                @Override
                public void onFailure(int arg0, String arg1) {Log.d("Saver", "Upload failed " + arg1);}
            });
        }
    }

// the user's operation when clicking the first card view///////////////////////////////////////////
    private void userOperator() {
        if (!SettingManager.getInstance().getLoggenOn()) {
            // register or log on
            new MaterialDialog.Builder(this)
                    .iconRes(R.drawable.donation_icon)
                    .typeface(Util.GetTypeface(), Util.GetTypeface())
                    .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                    .title(R.string.welcome)
                    .content(R.string.login_or_register)
                    .positiveText(R.string.login)
                    .negativeText(R.string.register)
                    .neutralText(R.string.cancel)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog,
                                            @NonNull DialogAction which) {
                            if (which.equals(DialogAction.POSITIVE)) {
                                userLogin();
                            } else if (which.equals(DialogAction.NEGATIVE)) {
                                userRegister();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    })
                    .show();
        } else {
            // log out or user operate
            new MaterialDialog.Builder(this)
                    .iconRes(R.drawable.donation_icon)
                    .typeface(Util.GetTypeface(), Util.GetTypeface())
                    .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                    .title(mContext.getResources().getString(R.string.hi)
                            + SettingManager.getInstance().getUserName())
                    .content(R.string.whether_logout)
                    .positiveText(R.string.log_out)
                    .neutralText(R.string.cancel)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog,
                                            @NonNull DialogAction which) {
                            if (which.equals(DialogAction.POSITIVE)) {
                                userLogout();
                            } else {
                                dialog.dismiss();
                            }
                        }
                    })
                    .show();
        }
    }

// User log out/////////////////////////////////////////////////////////////////////////////////////
    private void userLogout() {
        BmobUser.logOut(CoCoinApplication.getAppContext());
        SettingManager.getInstance().setLoggenOn(false);
        SettingManager.getInstance().setUserName(null);
        SettingManager.getInstance().setUserEmail(null);
        updateViews();
        showToast(8, "");
    }

// User login///////////////////////////////////////////////////////////////////////////////////////
    private void userLogin() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.go_login)
                .typeface(Util.GetTypeface(), Util.GetTypeface())
                .customView(R.layout.user_login, true)
                .positiveText(R.string.go_login)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which.equals(DialogAction.POSITIVE)) {
// the user ask to login////////////////////////////////////////////////////////////////////////////
                            final User user = new User();
                            user.setUsername(loginUserName.getText().toString());
                            user.setPassword(loginPassword.getText().toString());
                            user.login(CoCoinApplication.getAppContext(), new SaveListener() {
// try with user name///////////////////////////////////////////////////////////////////////////////
                                @Override
                                public void onSuccess() {
// login successfully through user name/////////////////////////////////////////////////////////////
                                    User loginUser =
                                            BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
                                    if (!CoCoinApplication.getAndroidId().equals(loginUser.getAndroidId())) {
// 2 users on one mobile////////////////////////////////////////////////////////////////////////////
                                        showToast(7, "unique...");
                                        return;
                                    }
                                    SettingManager.getInstance().setLoggenOn(true);
                                    SettingManager.getInstance().setUserName(loginUserName.getText().toString());
                                    SettingManager.getInstance().setUserEmail(
                                            loginUser.getEmail());
                                    updateViews();
                                    RecordManager.updateOldRecordsToServer();
                                    whetherSyncSettingsFromServer();
                                    showToast(6, loginUserName.getText().toString());
                                }
// login fail through user name/////////////////////////////////////////////////////////////////////
                                @Override
                                public void onFailure(int code, String msg) {
// try with user email//////////////////////////////////////////////////////////////////////////////
                                    user.setEmail(loginUserName.getText().toString());
                                    user.login(CoCoinApplication.getAppContext(), new SaveListener() {
                                        @Override
                                        public void onSuccess() {
// login successfully through user email////////////////////////////////////////////////////////////
                                            User loginUser =
                                                    BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
                                            if (!CoCoinApplication.getAndroidId().equals(loginUser.getAndroidId())) {
// 2 users on one mobile////////////////////////////////////////////////////////////////////////////
                                                showToast(7, "unique...");
                                                return;
                                            }
                                            String userName = loginUser.getUsername();
                                            SettingManager.getInstance().setLoggenOn(true);
                                            SettingManager.getInstance().setUserName(userName);
                                            SettingManager.getInstance().setUserEmail(loginUserName.getText().toString());
                                            SettingManager.getInstance().setUserPassword(loginPassword.getText().toString());
                                            updateViews();
                                            RecordManager.updateOldRecordsToServer();
                                            whetherSyncSettingsFromServer();
                                            showToast(6, userName);
                                        }
// login fail through user name and email///////////////////////////////////////////////////////////
                                        @Override
                                        public void onFailure(int code, String msg) {
                                            showToast(7, msg);
                                        }
                                    });
                                }
                            });
                        }
                    }
                }).build();

        final MDButton positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        positiveAction.setEnabled(false);

        TextView userNameTV
                = (TextView)dialog.getCustomView().findViewById(R.id.login_user_name_text);
        TextView userPasswordTV
                = (TextView)dialog.getCustomView().findViewById(R.id.login_password_text);
        userNameTV.setTypeface(Util.GetTypeface());
        userPasswordTV.setTypeface(Util.GetTypeface());

        loginUserName
                = (MaterialEditText)dialog.getCustomView().findViewById(R.id.login_user_name);
        loginPassword
                = (MaterialEditText)dialog.getCustomView().findViewById(R.id.login_password);

        loginUserName.setTypeface(Util.GetTypeface());
        loginUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(
                        0 < loginUserName.getText().toString().length()
                                && 0 < loginPassword.getText().toString().length());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        loginPassword.setTypeface(Util.GetTypeface());
        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(
                        0 < loginUserName.getText().toString().length()
                                && 0 < loginPassword.getText().toString().length());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        dialog.show();
    }

// User register////////////////////////////////////////////////////////////////////////////////////
    private void userRegister() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.go_register)
                .typeface(Util.GetTypeface(), Util.GetTypeface())
                .customView(R.layout.user_register, true)
                .positiveText(R.string.go_register)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (which.equals(DialogAction.POSITIVE)) {
// User register, a new user////////////////////////////////////////////////////////////////////////
                            final User user = new User();
                            // basic info
                            user.setUsername(registerUserName.getText().toString());
                            user.setPassword(registerPassword.getText().toString());
                            user.setEmail(registerUserEmail.getText().toString());
                            user.setAndroidId(CoCoinApplication.getAndroidId());
                            // settings info
                            // user.setLogo();
                            user.setIsMonthLimit(SettingManager.getInstance().getIsMonthLimit());
                            user.setMonthLimit(SettingManager.getInstance().getMonthLimit());
                            user.setIsColorRemind(SettingManager.getInstance().getIsColorRemind());
                            user.setMonthWarning(SettingManager.getInstance().getMonthWarning());
                            user.setRemindColor(SettingManager.getInstance().getRemindColor());
                            user.setIsForbidden(SettingManager.getInstance().getIsForbidden());
                            user.setAccountBookName(SettingManager.getInstance().getAccountBookName());
                            user.setAccountBookPassword(SettingManager.getInstance().getPassword());
                            // Todo store tag order
                            user.setShowPicture(SettingManager.getInstance().getShowPicture());
                            user.setIsHollow(SettingManager.getInstance().getIsHollow());
                            user.signUp(CoCoinApplication.getAppContext(), new SaveListener() {
                                @Override
                                public void onSuccess() {
// if register successfully/////////////////////////////////////////////////////////////////////////
                                    SettingManager.getInstance().setLoggenOn(true);
                                    SettingManager.getInstance().setUserName(registerUserName.getText().toString());
                                    SettingManager.getInstance().setUserEmail(registerUserEmail.getText().toString());
                                    SettingManager.getInstance().setUserPassword(registerPassword.getText().toString());
                                    showToast(4, registerUserName.getText().toString());
// if login successfully////////////////////////////////////////////////////////////////////////////
                                    user.login(CoCoinApplication.getAppContext(), new SaveListener() {
                                        @Override
                                        public void onSuccess() {
                                            updateViews();
                                            RecordManager.updateOldRecordsToServer();
                                        }
                                        @Override
                                        public void onFailure(int code, String msg) {
// if login failed//////////////////////////////////////////////////////////////////////////////////
                                        }
                                    });
                                }
// if register failed///////////////////////////////////////////////////////////////////////////////
                                @Override
                                public void onFailure(int code, String msg) {
                                    showToast(5, msg);
                                }
                            });
                        }
                    }
                }).build();

        final MDButton positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        positiveAction.setEnabled(false);
        final EmailValidator emailValidator = new EmailValidator();

        TextView userNameTV
                = (TextView)dialog.getCustomView().findViewById(R.id.register_user_name_text);
        TextView userEmailTV
                = (TextView)dialog.getCustomView().findViewById(R.id.register_user_email_text);
        TextView userPasswordTV
                = (TextView)dialog.getCustomView().findViewById(R.id.register_password_text);
        userNameTV.setTypeface(Util.GetTypeface());
        userEmailTV.setTypeface(Util.GetTypeface());
        userPasswordTV.setTypeface(Util.GetTypeface());

        registerUserName
                = (MaterialEditText)dialog.getCustomView().findViewById(R.id.register_user_name);
        registerUserEmail
                = (MaterialEditText)dialog.getCustomView().findViewById(R.id.register_user_email);
        registerPassword
                = (MaterialEditText)dialog.getCustomView().findViewById(R.id.register_password);

        registerUserName.setTypeface(Util.GetTypeface());
        registerUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean emailOK = emailValidator.validate(registerUserEmail.getText().toString());
                positiveAction.setEnabled(
                        0 < registerUserName.getText().toString().length()
                                && registerUserName.getText().toString().length() <= 16
                                && registerPassword.getText().toString().length() > 0
                                && emailOK);
                if (emailValidator.validate(registerUserEmail.getText().toString())) {
                    registerUserEmail.validate();
                } else {
                    registerUserEmail.invalidate();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerUserEmail.setTypeface(Util.GetTypeface());
        registerUserEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean emailOK = emailValidator.validate(registerUserEmail.getText().toString());
                positiveAction.setEnabled(
                        0 < registerUserName.getText().toString().length()
                                && registerUserName.getText().toString().length() <= 16
                                && registerPassword.getText().toString().length() > 0
                                && emailOK);
                if (emailValidator.validate(registerUserEmail.getText().toString())) {
                    registerUserEmail.validate();
                } else {
                    registerUserEmail.invalidate();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerPassword.setTypeface(Util.GetTypeface());
        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean emailOK = emailValidator.validate(registerUserEmail.getText().toString());
                positiveAction.setEnabled(
                        0 < registerUserName.getText().toString().length()
                                && registerUserName.getText().toString().length() <= 16
                                && registerPassword.getText().toString().length() > 0
                                && emailOK);
                if (emailValidator.validate(registerUserEmail.getText().toString())) {
                    registerUserEmail.validate();
                } else {
                    registerUserEmail.invalidate();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.show();
    }

// Change account book name/////////////////////////////////////////////////////////////////////////
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
                        // local change
                        SettingManager.getInstance().setAccountBookName(input.toString());
                        SettingManager.getInstance().setTodayViewTitleShouldChange(true);
                        SettingManager.getInstance().setMainViewTitleShouldChange(true);
                        accountBookName.setText(input.toString());
                        // update change
                        User user = getCurrentUser();
                        if (user != null) {
                            updateSettingsToServer(UPDATE_ACCOUNT_BOOK_NAME);
                        } else {
                            // the new account book name is changed successfully
                            showToast(2, "");
                        }
                    }
                }).show();
    }

// Update some views when login/////////////////////////////////////////////////////////////////////
    private void updateViews() {
        setIconEnable(userNameIcon, SettingManager.getInstance().getLoggenOn());
        setIconEnable(userEmailIcon, SettingManager.getInstance().getLoggenOn());
        if (SettingManager.getInstance().getLoggenOn()) {
            userName.setText(SettingManager.getInstance().getUserName());
            userEmail.setText(SettingManager.getInstance().getUserEmail());
        } else {
            userName.setText("");
            userEmail.setText("");
        }
    }

// Start change account book password activity//////////////////////////////////////////////////////
// I put the update to server part in the change password activity but not here/////////////////////
    private void changePassword() {
        Intent intent = new Intent(mContext, EditPasswordActivity.class);
        startActivity(intent);
    }

// Start sort tags activity/////////////////////////////////////////////////////////////////////////
// I put the update to server part in the sort tag activity but not here////////////////////////////
    private void sortTags() {
        Intent intent = new Intent(mContext, TagSettingActivity.class);
        startActivity(intent);
    }

// Init the setting activity////////////////////////////////////////////////////////////////////////
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
        monthWarningIcon = (MaterialIconView)findViewById(R.id.warning_expense_icon);
        monthColorRemindTypeIcon = (MaterialIconView)findViewById(R.id.month_color_type_icon);
        monthColorRemindSelect = (MaterialIconView)findViewById(R.id.month_color_type);
        monthColorRemindSelect.setColor(SettingManager.getInstance().getRemindColor());
        monthForbiddenIcon = (MaterialIconView)findViewById(R.id.month_forbidden_icon);
        monthSB = (Switch)findViewById(R.id.month_limit_enable_button);
        monthSB.setOnCheckedChangeListener(this);
        monthColorRemindSB = (Switch)findViewById(R.id.month_color_remind_button);
        monthColorRemindSB.setOnCheckedChangeListener(this);
        monthForbiddenSB = (Switch)findViewById(R.id.month_forbidden_button);
        monthForbiddenSB.setOnCheckedChangeListener(this);
        monthMaxExpense = (RiseNumberTextView)findViewById(R.id.month_expense);
        if (SettingManager.getInstance().getIsMonthLimit())
            monthMaxExpense.withNumber(SettingManager.getInstance()
                    .getMonthLimit()).setDuration(1000).start();
// change the month limit///////////////////////////////////////////////////////////////////////////
        monthMaxExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SettingManager.getInstance().getIsMonthLimit()) {
                    new MaterialDialog.Builder(mContext)
                            .theme(Theme.LIGHT)
                            .typeface(Util.GetTypeface(), Util.GetTypeface())
                            .title(R.string.set_month_expense_dialog_title)
                            .inputType(InputType.TYPE_CLASS_NUMBER)
                            .positiveText(R.string.submit)
                            .inputRange(3, 5)
                            .input(SettingManager.getInstance().getMonthLimit().toString()
                                    , null, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog dialog, CharSequence input) {
                                    int newExpense = SettingManager.getInstance().getMonthLimit();
                                    if (input.length() != 0) {
                                        newExpense = Integer.parseInt(input.toString());
                                    }
                                    // the month limit must be smaller than the month warning
                                    if (newExpense < SettingManager.getInstance().getMonthWarning()) {
                                        SettingManager.getInstance().setMonthWarning(
                                                ((int)(newExpense * 0.8) / 100 * 100));
                                        if (SettingManager.getInstance().getMonthWarning() < 100) {
                                            SettingManager.getInstance().setMonthWarning(100);
                                        }
                                        updateSettingsToServer(UPDATE_MONTH_WARNING);
                                        SettingManager.getInstance()
                                                .setMainViewRemindColorShouldChange(true);
                                        monthWarning.setText(SettingManager
                                                .getInstance().getMonthWarning().toString());
                                    }
                                    SettingManager.getInstance().setMonthLimit(newExpense);
                                    updateSettingsToServer(UPDATE_MONTH_LIMIT);
                                    SettingManager.getInstance()
                                            .setTodayViewMonthExpenseShouldChange(true);
                                    SettingManager.getInstance()
                                            .setMainViewMonthExpenseShouldChange(true);
                                    monthMaxExpense.withNumber(SettingManager.getInstance()
                                            .getMonthLimit()).setDuration(1000).start();
                                }
                            }).show();
                }
            }
        });
        monthWarning = (RiseNumberTextView)findViewById(R.id.warning_expense);
        monthWarning.setText(SettingManager.getInstance().getMonthWarning().toString());
        if (SettingManager.getInstance().getIsMonthLimit()
                && SettingManager.getInstance().getIsColorRemind())
            monthWarning.withNumber(SettingManager.getInstance()
                    .getMonthWarning()).setDuration(1000).start();
// change month warning/////////////////////////////////////////////////////////////////////////////
        monthWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SettingManager.getInstance().getIsMonthLimit()
                        && SettingManager.getInstance().getIsColorRemind()) {
                    new MaterialDialog.Builder(mContext)
                            .theme(Theme.LIGHT)
                            .typeface(Util.GetTypeface(), Util.GetTypeface())
                            .title(R.string.set_month_expense_dialog_title)
                            .inputType(InputType.TYPE_CLASS_NUMBER)
                            .positiveText(R.string.submit)
                            .alwaysCallInputCallback()
                            .input(null, null, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(final MaterialDialog dialog, final CharSequence input) {
                                    if (input.length() == 0) {
                                        dialog.setContent(mContext.getResources().getString(
                                                R.string.set_warning_expense_dialog_title));
                                        dialog.getActionButton(DialogAction.POSITIVE)
                                                .setEnabled(false);
                                    } else if (Integer.parseInt(input.toString()) < 100) {
                                        dialog.setContent("≥ 100");
                                        dialog.getActionButton(DialogAction.POSITIVE)
                                                .setEnabled(false);
                                    } else if (Integer.parseInt(input.toString())
                                            > SettingManager.getInstance().getMonthLimit()) {
                                        dialog.setContent("≤ " + SettingManager.getInstance()
                                                .getMonthLimit().toString());
                                        dialog.getActionButton(DialogAction.POSITIVE)
                                                .setEnabled(false);
                                    } else {
                                        dialog.setContent(mContext.getResources().getString(
                                                R.string.set_warning_expense_dialog_title));
                                        dialog.getActionButton(DialogAction.POSITIVE)
                                                .setEnabled(true);
                                    }
                                    dialog.getActionButton(DialogAction.POSITIVE)
                                            .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            SettingManager.getInstance()
                                                    .setMonthWarning(Integer.parseInt(input.toString()));
                                            updateSettingsToServer(UPDATE_MONTH_WARNING);
                                            SettingManager.getInstance()
                                                    .setMainViewRemindColorShouldChange(true);
                                            monthWarning.withNumber(SettingManager.getInstance()
                                                    .getMonthWarning()).setDuration(1000).start();
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            }).show();
                }
            }
        });
// change month remind color////////////////////////////////////////////////////////////////////////
        monthColorRemindSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingManager.getInstance()
                        .setMainViewRemindColorShouldChange(true);
                remindColorSelectDialog.show((AppCompatActivity) mContext);
            }
        });
        monthMaxExpense.setTypeface(Util.typefaceLatoLight);
        monthWarning.setTypeface(Util.typefaceLatoLight);
        monthLimitTV = (TextView)findViewById(R.id.month_limit_text);
        monthLimitTV.setTypeface(Util.GetTypeface());
        monthWarningTV = (TextView)findViewById(R.id.warning_expense_text);
        monthWarningTV.setTypeface(Util.GetTypeface());
        monthMaxExpenseTV = (TextView)findViewById(R.id.month_expense_text);
        monthMaxExpenseTV.setTypeface(Util.GetTypeface());
        monthColorRemindTV = (TextView)findViewById(R.id.month_color_remind_text);
        monthColorRemindTV.setTypeface(Util.GetTypeface());
        monthColorRemindTypeTV = (TextView)findViewById(R.id.month_color_type_text);
        monthColorRemindTypeTV.setTypeface(Util.GetTypeface());
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
        showPictureSB = (Switch)findViewById(R.id.whether_show_picture_button);
        showPictureSB.setOnCheckedChangeListener(this);
        showPictureTV = (TextView)findViewById(R.id.whether_show_picture_text);
        showPictureTV.setTypeface(Util.GetTypeface());

        hollowLayout = (MaterialRippleLayout)findViewById(R.id.whether_show_circle_layout);
        hollowIcon = (MaterialIconView)findViewById(R.id.whether_show_circle_icon);
        hollowSB = (Switch)findViewById(R.id.whether_show_circle_button);
        hollowSB.setOnCheckedChangeListener(this);
        hollowTV = (TextView)findViewById(R.id.whether_show_circle_text);
        hollowTV.setTypeface(Util.GetTypeface());

        boolean loggenOn = SettingManager.getInstance().getLoggenOn();
        if (loggenOn) {
            // is logged on, set the user name and email
            userName.setText(SettingManager.getInstance().getUserName());
            userEmail.setText(SettingManager.getInstance().getUserEmail());
        } else {
            userName.setText("");
            userEmail.setText("");
        }
        setIconEnable(userNameIcon, loggenOn);
        setIconEnable(userEmailIcon, loggenOn);

        loadLogo();

        monthSB.setCheckedImmediately(SettingManager.getInstance().getIsMonthLimit());
        setMonthState();

        showPictureSB.setCheckedImmediately(SettingManager.getInstance().getShowPicture());
        setShowPictureState(SettingManager.getInstance().getShowPicture());

        hollowSB.setCheckedImmediately(SettingManager.getInstance().getIsHollow());
        setHollowState(SettingManager.getInstance().getIsHollow());
    }

// Set all states about month limit/////////////////////////////////////////////////////////////////
    private void setMonthState() {
        boolean isMonthLimit = SettingManager.getInstance().getIsMonthLimit();
        boolean isMonthColorRemind = SettingManager.getInstance().getIsColorRemind();
        boolean isForbidden = SettingManager.getInstance().getIsForbidden();

        setIconEnable(monthIcon, isMonthLimit);
        setIconEnable(monthMaxExpenseIcon, isMonthLimit);
        setTVEnable(monthMaxExpenseTV, isMonthLimit);
        setTVEnable(monthMaxExpense, isMonthLimit);
        setTVEnable(monthColorRemindTV, isMonthLimit);
        setTVEnable(monthColorRemindTypeTV, isMonthLimit && isMonthColorRemind);
        setTVEnable(monthWarningTV, isMonthLimit && isMonthColorRemind);
        setTVEnable(monthForbiddenTV, isMonthLimit);
        monthMaxExpense.setText(SettingManager.getInstance().getMonthLimit() + "");

        setIconEnable(monthColorRemindIcon, isMonthLimit && isMonthColorRemind);
        setIconEnable(monthWarningIcon, isMonthLimit && isMonthColorRemind);
        setIconEnable(monthColorRemindTypeIcon, isMonthLimit && isMonthColorRemind);
        setIconEnable(monthColorRemindSelect, isMonthLimit && isMonthColorRemind);
        if (isMonthLimit && isMonthColorRemind) {
            monthColorRemindSelect.setEnabled(true);
            monthColorRemindSelect
                    .setColor(SettingManager.getInstance().getRemindColor());
            monthWarning.setEnabled(true);
            monthWarning.setTextColor(
                    ContextCompat.getColor(mContext, R.color.drawer_text));
        } else {
            monthColorRemindSelect.setEnabled(false);
            monthColorRemindSelect
                    .setColor(mContext.getResources().getColor(R.color.my_gray));
            monthWarning.setEnabled(false);
            monthWarning.setTextColor(
                    ContextCompat.getColor(mContext, R.color.my_gray));
        }
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

// choose a color///////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onColorSelection(ColorChooserDialog dialog, int selectedColor) {
        monthColorRemindSelect.setColor(selectedColor);
        SettingManager.getInstance().setRemindColor(selectedColor);
        updateSettingsToServer(UPDATE_REMIND_COLOR);
        SettingManager.getInstance().setMainViewRemindColorShouldChange(true);
    }

    ColorChooserDialog remindColorSelectDialog =
            new ColorChooserDialog.Builder(this, R.string.set_remind_color_dialog_title)
                    .titleSub(R.string.set_remind_color_dialog_sub_title)
                    .preselect(SettingManager.getInstance().getRemindColor())
                    .doneButton(R.string.submit)
                    .cancelButton(R.string.cancel)
                    .backButton(R.string.back)
                    .customButton(R.string.custom)
                    .dynamicButtonColor(true)
                    .build();

// whether sync the settings from server////////////////////////////////////////////////////////////
    private void whetherSyncSettingsFromServer() {
        new MaterialDialog.Builder(this)
                .iconRes(R.drawable.donation_icon)
                .typeface(Util.GetTypeface(), Util.GetTypeface())
                .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                .title(R.string.sync_dialog_title)
                .forceStacking(true)
                .content(R.string.sync_dialog_content)
                .positiveText(R.string.sync_dialog_sync_to_local)
                .negativeText(R.string.sync_dialog_sync_to_server)
                .cancelable(false)
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog,
                                        @NonNull DialogAction which) {
                        if (which.equals(DialogAction.POSITIVE)) {
                            // sync to local
                            downloadLogoFromServer();
                            User user = getCurrentUser();
                            String tip = "";
                            boolean accountBookPasswordChanged = false;
                            if (!user.getAccountBookPassword().equals(SettingManager.getInstance().getPassword()))
                                accountBookPasswordChanged = true;

                            SettingManager.getInstance().setIsMonthLimit(user.getIsMonthLimit());
                            monthSB.setChecked(user.getIsMonthLimit());
                            SettingManager.getInstance().setMonthLimit(user.getMonthLimit());
                            if (SettingManager.getInstance().getIsMonthLimit())
                                monthMaxExpense.withNumber(SettingManager.getInstance()
                                        .getMonthLimit()).setDuration(1000).start();
                            SettingManager.getInstance().setIsColorRemind(user.getIsColorRemind());
                            monthColorRemindSB.setChecked(user.getIsColorRemind());
                            SettingManager.getInstance().setMonthWarning(user.getMonthWarning());
                            if (SettingManager.getInstance().getIsMonthLimit()
                                    && SettingManager.getInstance().getIsColorRemind())
                                monthWarning.withNumber(SettingManager.getInstance()
                                        .getMonthWarning()).setDuration(1000).start();
                            SettingManager.getInstance().setRemindColor(user.getRemindColor());
                            monthColorRemindTypeIcon.setColor(SettingManager.getInstance().getRemindColor());
                            SettingManager.getInstance().setIsForbidden(user.getIsForbidden());
                            monthForbiddenSB.setChecked(user.getIsForbidden());
                            SettingManager.getInstance().setAccountBookName(user.getAccountBookName());
                            accountBookName.setText(user.getAccountBookName());
                            SettingManager.getInstance().setPassword(user.getAccountBookPassword());
                            // Todo tag sort
                            SettingManager.getInstance().setShowPicture(user.getShowPicture());
                            showPictureSB.setChecked(user.getShowPicture());
                            SettingManager.getInstance().setIsHollow(user.getIsHollow());
                            hollowSB.setChecked(user.getIsHollow());
                            SettingManager.getInstance().setMainViewMonthExpenseShouldChange(true);
                            SettingManager.getInstance().setMainViewRemindColorShouldChange(true);
                            SettingManager.getInstance().setMainViewTitleShouldChange(true);
                            SettingManager.getInstance().setTodayViewMonthExpenseShouldChange(true);
                            SettingManager.getInstance().setTodayViewPieShouldChange(true);
                            SettingManager.getInstance().setTodayViewTitleShouldChange(true);
                            // SettingManager.getInstance().getMainActivityTagShouldChange();
                            if (accountBookPasswordChanged)
                                tip = "\n" + getString(R.string.your_current_account_book_password_is)
                                        + SettingManager.getInstance().getPassword();
                            new MaterialDialog.Builder(mContext)
                                    .typeface(Util.GetTypeface(), Util.GetTypeface())
                                    .limitIconToDefaultSize() // limits the displayed icon size to 48dp
                                    .title(R.string.sync_to_local_successfully_dialog_title)
                                    .content(getString(R.string.sync_to_local_successfully_dialog_content) + tip)
                                    .positiveText(R.string.ok)
                                    .show();
                        } else if (which.equals(DialogAction.NEGATIVE)) {
                            // sync to server
                            uploadLogoToServer();
                            User user = getCurrentUser();
                            user.setIsMonthLimit(SettingManager.getInstance().getIsMonthLimit());
                            user.setMonthLimit(SettingManager.getInstance().getMonthLimit());
                            user.setIsColorRemind(SettingManager.getInstance().getIsColorRemind());
                            user.setMonthWarning(SettingManager.getInstance().getMonthWarning());
                            user.setRemindColor(SettingManager.getInstance().getRemindColor());
                            user.setIsForbidden(SettingManager.getInstance().getIsForbidden());
                            user.setAccountBookName(SettingManager.getInstance().getAccountBookName());
                            user.setAccountBookPassword(SettingManager.getInstance().getPassword());
                            // Todo tag sort
                            user.setShowPicture(SettingManager.getInstance().getShowPicture());
                            user.setIsHollow(SettingManager.getInstance().getIsHollow());
                            user.update(CoCoinApplication.getAppContext(),
                                    user.getObjectId(), new UpdateListener() {
                                @Override
                                public void onSuccess() {
                                    showToast(9, "");
                                }
                                @Override
                                public void onFailure(int code, String msg) {
                                    showToast(10, msg);
                                }
                            });
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void updateAllSettings() {
        updateSettingsToServer(0);
        updateSettingsToServer(1);
        updateSettingsToServer(2);
    }

// update part of settings//////////////////////////////////////////////////////////////////////////
    private void updateSettingsToServer(final int setting) {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            Log.d("Saver", "User hasn't log in.");
            return;
        }
        switch (setting) {
            case UPDATE_LOGO:
                // logo
                break;
            case UPDATE_IS_MONTH_LIMIT:
                // is month limit
                currentUser.setIsMonthLimit(SettingManager.getInstance().getIsMonthLimit());
                break;
            case UPDATE_MONTH_LIMIT:
                // month limit
                currentUser.setMonthLimit(SettingManager.getInstance().getMonthLimit());
                break;
            case UPDATE_IS_COLOR_REMIND:
                // is color remind
                currentUser.setIsColorRemind(SettingManager.getInstance().getIsColorRemind());
                break;
            case UPDATE_MONTH_WARNING:
                // month warning
                currentUser.setMonthWarning(SettingManager.getInstance().getMonthWarning());
                break;
            case UPDATE_REMIND_COLOR:
                // remind color
                currentUser.setRemindColor(SettingManager.getInstance().getRemindColor());
                break;
            case UPDATE_IS_FORBIDDEN:
                // is forbidden
                currentUser.setIsForbidden(SettingManager.getInstance().getIsForbidden());
                break;
            case UPDATE_ACCOUNT_BOOK_NAME:
                // account book name
                currentUser.setAccountBookName(SettingManager.getInstance().getAccountBookName());
                break;
            case UPDATE_ACCOUNT_BOOK_PASSWORD:
                // account book password
                currentUser.setAccountBookPassword(SettingManager.getInstance().getPassword());
                break;
            case UPDATE_SHOW_PICTURE:
                // show picture
                currentUser.setShowPicture(SettingManager.getInstance().getShowPicture());
                break;
            case UPDATE_IS_HOLLOW:
                // is hollow
                currentUser.setIsHollow(SettingManager.getInstance().getIsHollow());
                break;
        }
        currentUser.update(CoCoinApplication.getAppContext(),
                currentUser.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("Saver", "Update " + setting + " successfully.");
                        // the new account book name is updated to server successfully
                        if (setting == UPDATE_ACCOUNT_BOOK_NAME) showToast(0, "");
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        Log.d("Saver", "Update " + setting + " fail.");
                        // the new account book name is failed to updated to server
                        if (setting == UPDATE_ACCOUNT_BOOK_NAME) showToast(1, "");
                    }
                });
    }

    private void syncUserInfo() {
        User user = BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);

    }

// Get the current user/////////////////////////////////////////////////////////////////////////////
    private User getCurrentUser() {
        return BmobUser.getCurrentUser(CoCoinApplication.getAppContext(), User.class);
    }

// Get string///////////////////////////////////////////////////////////////////////////////////////
    private String getResourceString(int resourceId) {
        return CoCoinApplication.getAppContext().getResources().getString(resourceId);
    }

// activity finish//////////////////////////////////////////////////////////////////////////////////
    @Override
    public void finish() {

        SuperToast.cancelAllSuperToasts();

        super.finish();
    }

// Show toast///////////////////////////////////////////////////////////////////////////////////////
    private void showToast(int toastType, String msg) {
        Log.d("Saver", msg);
        SuperToast.cancelAllSuperToasts();
        SuperToast superToast = new SuperToast(mContext);

        superToast.setAnimations(Util.TOAST_ANIMATION);
        superToast.setDuration(SuperToast.Duration.LONG);
        superToast.setTextColor(Color.parseColor("#ffffff"));
        superToast.setTextSize(SuperToast.TextSize.SMALL);

        String tip = "";

        switch (toastType) {
            case 0:
                // the new account book name is updated to server successfully
                superToast.setText(CoCoinApplication.getAppContext().getResources().getString(
                        R.string.change_and_update_account_book_name_successfully));
                superToast.setBackground(SuperToast.Background.BLUE);
                break;
            case 1:
                // the new account book name is failed to updated to server
                superToast.setText(CoCoinApplication.getAppContext().getResources().getString(
                        R.string.change_and_update_account_book_name_fail));
                superToast.setBackground(SuperToast.Background.RED);
                break;
            case 2:
                // the new account book name is changed successfully
                superToast.setText(CoCoinApplication.getAppContext().getResources().getString(
                        R.string.change_account_book_name_successfully));
                superToast.setBackground(SuperToast.Background.BLUE);
                break;
            case 3:
                // the new account book name is failed to change
                superToast.setText(CoCoinApplication.getAppContext().getResources().getString(
                        R.string.change_account_book_name_fail));
                superToast.setBackground(SuperToast.Background.RED);
                break;
            case 4:
                // register successfully
                tip = msg;
                superToast.setText(getResourceString(R.string.register_successfully) + tip);
                superToast.setBackground(SuperToast.Background.BLUE);
                break;
            case 5:
                // register failed
                tip = getResourceString(R.string.network_disconnection);
                if (msg.charAt(1) == 's') tip = getResourceString(R.string.user_name_exist);
                if (msg.charAt(0) == 'e') tip = getResourceString(R.string.user_email_exist);
                if (msg.charAt(1) == 'n') tip = getResourceString(R.string.user_mobile_exist);
                superToast.setText(getResourceString(R.string.register_fail) + tip);
                superToast.setBackground(SuperToast.Background.RED);
                break;
            case 6:
                // login successfully
                tip = msg;
                superToast.setText(getResourceString(R.string.login_successfully) + tip);
                superToast.setBackground(SuperToast.Background.BLUE);
                break;
            case 7:
                // login failed
                tip = getResourceString(R.string.network_disconnection);
                if (msg.charAt(0) == 'u') tip = getResourceString(R.string.user_name_or_password_incorrect);
                if (msg.charAt(1) == 'n') tip = getResourceString(R.string.user_mobile_exist);
                superToast.setText(getResourceString(R.string.login_fail) + tip);
                superToast.setBackground(SuperToast.Background.RED);
                break;
            case 8:
                // log out successfully
                superToast.setText(getResourceString(R.string.log_out_successfully));
                superToast.setBackground(SuperToast.Background.BLUE);
                break;
            case 9:
                // sync settings successfully
                superToast.setText(getResourceString(R.string.sync_to_server_successfully));
                superToast.setBackground(SuperToast.Background.BLUE);
                break;
            case 10:
                // sync settings failed
                tip = getResourceString(R.string.network_disconnection);
                superToast.setText(getResourceString(R.string.sync_to_server_failed) + tip);
                superToast.setBackground(SuperToast.Background.RED);
                break;

        }
        superToast.getTextView().setTypeface(Util.GetTypeface());
        superToast.show();
    }

}
