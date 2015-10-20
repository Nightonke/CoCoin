package com.nightonke.saver;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.glomadrian.codeinputlib.CodeInput;
import com.jungly.gridpasswordview.GridPasswordView;
import com.liangfeizc.RubberIndicator;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.victor.loading.book.BookLoading;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

import android.support.v7.widget.Toolbar;

import java.io.IOException;

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
    private int passwordIndex = 0;

    private TextView passwordTip;

    private float x1, y1, x2, y2;
    static final int MIN_DISTANCE = 150;

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

        Utils.init(mContext);

        try {
            RecordManager recordManager = RecordManager.getInstance(mContext);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        ButterKnife.inject(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        toolbar.hideOverflowMenu();

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        transparentLy = (LinearLayout)guillotineMenu.findViewById(R.id.transparent_ly);

        passwordTip = (TextView)guillotineMenu.findViewById(R.id.password_tip);

        animation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu,
                        guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                        isPassword = true;
                    }

                    @Override
                    public void onGuillotineClosed() {
                        isPassword = false;
                        editView.requestFocus();
                        Spannable span = new SpannableString("PASSWORD");
                        span.setSpan(Utils.whiteForegroundSpan, 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        passwordTip.setText(span);
                        inputPassword = "";
                        passwordIndex = 0;
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
                    editView.setHelperText(Utils.FLOATINGLABELS[editView.getText().toString().length()]);
                }
            } else {
                inputPassword = "";
                Spannable span = new SpannableString("PASSWORD");
                span.setSpan(Utils.whiteForegroundSpan, 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                passwordTip.setText(span);
            }
            return false;
        }
    };


    private void checkPassword() {
        if (inputPassword.length() != 4) {
            Spannable span = new SpannableString("PASSWORD");
            span.setSpan(Utils.greenForegroundSpan, 0, 0 + inputPassword.length() * 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            passwordTip.setText(span);
            return;
        }
        if (Utils.PASSWORD.equals(inputPassword)) {
            Toast.makeText(mContext, "Correct!", Toast.LENGTH_SHORT).show();
            Spannable span = new SpannableString("PASSWORD");
            span.setSpan(Utils.greenForegroundSpan, 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            passwordTip.setText(span);
        } else {
            Toast.makeText(mContext, "Incorrect!", Toast.LENGTH_SHORT).show();
            Spannable span = new SpannableString("PASSWORD");
            span.setSpan(Utils.redForegroundSpan, 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            passwordTip.setText(span);
        }
    }

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
                        }
                    } else if (Utils.ClickButtonCommit(position)) {
                        Toast.makeText(mContext, "Commit", Toast.LENGTH_SHORT).show();
                        editView.setText("0");
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
                        inputPassword = inputPassword.substring(0, inputPassword.length() - 1);
                    }
                } else if (Utils.ClickButtonCommit(position)) {
                } else {
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
