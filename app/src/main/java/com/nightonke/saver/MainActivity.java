package com.nightonke.saver;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import carbon.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    private MyGridView myGridView;
    private MyGridViewAdapter myGridViewAdapter;

    private MaterialEditText editView;

    private LinearLayout transparentLy;

    private boolean isPassword = false;

    private static final long RIPPLE_DURATION = 250;

    private GuillotineAnimation animation;

    private String inputPassword = "";

    private float x1, y1, x2, y2;
    static final int MIN_DISTANCE = 150;

    private RadioButton radioButton0;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;

    private MaterialMenuView statusButton;

    private LinearLayout radioButtonLy;

    private View guillotineMenu;

    private ViewPager viewPager;
    private SmartTabLayout smartTabLayout;

    public static TextView tagName;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.root)
    FrameLayout root;
    @InjectView(R.id.content_hamburger)
    View contentHamburger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContext = this;

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        Log.d("Saver", "Version number: " + currentapiVersion);

        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.statusBarColor));
        } else{
            // do something for phones running an SDK before lollipop
        }

        Utils.init(mContext);

        try {
            RecordManager recordManager = RecordManager.getInstance(mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        smartTabLayout = (SmartTabLayout)findViewById(R.id.viewpagertab);

        FragmentPagerItems pages = new FragmentPagerItems(this);
        for (int i = 0; i < (RecordManager.TAGS.size() - 2) / 8; i++) {
            pages.add(FragmentPagerItem.of("1", tagFragment.class));
        }

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        viewPager.setOffscreenPageLimit(2);

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

        myGridView = (MyGridView)findViewById(R.id.gridview);
        myGridViewAdapter = new MyGridViewAdapter(this);
        myGridView.setAdapter(myGridViewAdapter);

        myGridView.setOnItemClickListener(gridViewClickListener);
        myGridView.setOnItemLongClickListener(gridViewLongClickListener);

        myGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        myGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        View lastChild = myGridView.getChildAt(myGridView.getChildCount() - 1);
                        myGridView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, lastChild.getBottom()));
                        Log.d("Saver", "" + myGridView.getHeight());

                        ViewGroup.LayoutParams params = transparentLy.getLayoutParams();
                        Log.d("Saver", "" + myGridView.getMeasuredHeight());
                        params.height = myGridView.getMeasuredHeight();
                    }
                });

        editView = (MaterialEditText)findViewById(R.id.edit_view);
        editView.setTypeface(Utils.typefaceBernhardFashion);
        editView.setText("0");
        editView.requestFocus();
        editView.setHelperText(" ");

        tagName = (TextView)findViewById(R.id.tag_name);

        ButterKnife.inject(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        toolbar.hideOverflowMenu();

        guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        transparentLy = (LinearLayout)guillotineMenu.findViewById(R.id.transparent_ly);

        radioButton0 = (RadioButton)guillotineMenu.findViewById(R.id.radio_button_0);
        radioButton1 = (RadioButton)guillotineMenu.findViewById(R.id.radio_button_1);
        radioButton2 = (RadioButton)guillotineMenu.findViewById(R.id.radio_button_2);
        radioButton3 = (RadioButton)guillotineMenu.findViewById(R.id.radio_button_3);

        radioButtonLy = (LinearLayout)guillotineMenu.findViewById(R.id.radio_button_ly);

        statusButton = (MaterialMenuView)guillotineMenu.findViewById(R.id.status_button);
        statusButton.setState(MaterialMenuDrawable.IconState.ARROW);

        statusButton.setOnClickListener(statusButtonOnClickListener);

        animation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu,
                        guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                        isPassword = true;
                        editView.setHelperText(" ");
                    }

                    @Override
                    public void onGuillotineClosed() {
                        isPassword = false;
                        editView.requestFocus();
                        radioButton0.setChecked(false);
                        radioButton1.setChecked(false);
                        radioButton2.setChecked(false);
                        radioButton3.setChecked(false);
                        inputPassword = "";
                        statusButton.setState(MaterialMenuDrawable.IconState.ARROW);
                    }
                })
                .build();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.open();
            }
        });
    }

    private AdapterView.OnItemLongClickListener gridViewLongClickListener
            = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (!isPassword) {
                if (Utils.ClickButtonDelete(position)) {
                    editView.setText("0");
                    editView.setHelperText(" ");
                    editView.setHelperText(Utils.FLOATINGLABELS[editView.getText().toString().length()]);
                }
            } else {
                radioButton0.setChecked(false);
                radioButton1.setChecked(false);
                radioButton2.setChecked(false);
                radioButton3.setChecked(false);
                inputPassword = "";
            }
            return false;
        }
    };


    private void checkPassword() {
        if (inputPassword.length() != 4) {
            return;
        }
        if (Utils.PASSWORD.equals(inputPassword)) {
            statusButton.animateState(MaterialMenuDrawable.IconState.CHECK);
            statusButton.setClickable(false);
            Toast.makeText(mContext, "Correct!", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    statusButton.setClickable(true);
                    Intent intent = new Intent(mContext, AccountBook.class);
                    mContext.startActivity(intent);
                }
            }, 1500);
            final Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    animation.close();
                }
            }, 3000);
        } else {
            Toast.makeText(mContext, "Incorrect!", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake).duration(700).playOn(radioButtonLy);
            radioButton0.setChecked(false);
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            inputPassword = "";
            statusButton.animateState(MaterialMenuDrawable.IconState.X);
        }
    }

    private View.OnClickListener statusButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            animation.close();
        }
    };

    private AdapterView.OnItemClickListener gridViewClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!isPassword) {
                if (editView.getText().toString().equals("0")) {
                    if (Utils.ClickButtonDelete(position)
                            || Utils.ClickButtonCommit(position)
                            || Utils.ClickButtonIsZero(position)) {

                    } else {
                        editView.setText(Utils.BUTTONS[position]);
                    }
                } else {
                    if (Utils.ClickButtonDelete(position)) {
                        editView.setText(editView.getText().toString()
                                .substring(0, editView.getText().toString().length() - 1));
                        if (editView.getText().toString().length() == 0) {
                            editView.setText("0");
                            editView.setHelperText(" ");
                        }
                    } else if (Utils.ClickButtonCommit(position)) {
                        Toast.makeText(mContext, "Commit", Toast.LENGTH_SHORT).show();
                        editView.setText("0");
                        editView.setHelperText(" ");
                    } else {
                        editView.setText(editView.getText().toString() + Utils.BUTTONS[position]);
                    }
                }
                editView.setHelperText(Utils.FLOATINGLABELS[editView.getText().toString().length()]);
            } else {
                if (Utils.ClickButtonDelete(position)) {
                    if (inputPassword.length() == 0) {
                        inputPassword = "";
                    } else {
                        if (inputPassword.length() == 1) {
                            radioButton0.setChecked(false);
                        } else if (inputPassword.length() == 2) {
                            radioButton1.setChecked(false);
                        } else if (inputPassword.length() == 3) {
                            radioButton2.setChecked(false);
                        } else {
                            radioButton3.setChecked(false);
                        }
                        inputPassword = inputPassword.substring(0, inputPassword.length() - 1);
                    }
                } else if (Utils.ClickButtonCommit(position)) {
                } else {
                    if (statusButton.getState() == MaterialMenuDrawable.IconState.X) {
                        statusButton.animateState(MaterialMenuDrawable.IconState.ARROW);
                    }
                    if (inputPassword.length() == 0) {
                        radioButton0.setChecked(true);
                        YoYo.with(Techniques.Bounce).duration(1000).playOn(radioButton0);
                    } else if (inputPassword.length() == 1) {
                        radioButton1.setChecked(true);
                        YoYo.with(Techniques.Bounce).duration(1000).playOn(radioButton1);
                    } else if (inputPassword.length() == 2) {
                        radioButton2.setChecked(true);
                        YoYo.with(Techniques.Bounce).duration(1000).playOn(radioButton2);
                    } else if (inputPassword.length() == 3) {
                        radioButton3.setChecked(true);
                        YoYo.with(Techniques.Bounce).duration(1000).playOn(radioButton3);
                    }
                    if (inputPassword.length() < 4) {
                        inputPassword += Utils.BUTTONS[position];
                    }
                }
                checkPassword();
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = ev.getX();
                y1 = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                x2 = ev.getX();
                y2 = ev.getY();
                if (y2 - y1 > 300) {
                    if (!isPassword) {
                        animation.open();
                    }
                }
                if (y1 - y2 > 300) {
                    if (isPassword) {
                        animation.close();
                    }
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (isPassword) {
            animation.close();
            return;
        }
        editView.setText(editView.getText().toString()
                .substring(0, editView.getText().toString().length() - 1));
        if (editView.getText().toString().equals("")) {
            editView.setText("0");
        }
    }
}
