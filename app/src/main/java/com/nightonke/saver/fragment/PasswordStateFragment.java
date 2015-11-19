package com.nightonke.saver.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nightonke.saver.R;
import com.nightonke.saver.activity.CoCoinApplication;
import com.nightonke.saver.util.Util;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.rey.material.widget.RadioButton;


/**
 * Created by 伟平 on 2015/10/27.
 */
public class PasswordStateFragment extends Fragment {

    private int fragmentPosition;
    private RadioButton button0;
    private RadioButton button1;
    private RadioButton button2;
    private RadioButton button3;
    private LinearLayout ly;
    private TextView passwordTip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(
                getActivity()).inflate(R.layout.password_state_fragment, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.password_state_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentPosition = FragmentPagerItem.getPosition(getArguments());
        button0 = (RadioButton)view.findViewById(R.id.button0);
        button1 = (RadioButton)view.findViewById(R.id.button1);
        button2 = (RadioButton)view.findViewById(R.id.button2);
        button3 = (RadioButton)view.findViewById(R.id.button3);
        ly = (LinearLayout)view.findViewById(R.id.buttonLy);
        passwordTip = (TextView)view.findViewById(R.id.password_tip);
        passwordTip.setTypeface(Util.GetTypeface());
        switch (fragmentPosition) {
            case 0:
                passwordTip.setText(CoCoinApplication.getAppContext().getResources()
                        .getString(R.string.password_tip_0));
                break;
            case 1:
                passwordTip.setText(CoCoinApplication.getAppContext().getResources()
                        .getString(R.string.password_tip_1));
                break;
            case 2:
                passwordTip.setText(CoCoinApplication.getAppContext().getResources()
                        .getString(R.string.password_tip_2));
                break;
            default:
                break;
        }
    }

    public void set(int i) {
        switch (i) {
            case 0:
                button0.setChecked(true);
                YoYo.with(Techniques.Bounce).delay(0).duration(1000).playOn(button0);
                break;
            case 1:
                button1.setChecked(true);
                YoYo.with(Techniques.Bounce).delay(0).duration(1000).playOn(button1);
                break;
            case 2:
                button2.setChecked(true);
                YoYo.with(Techniques.Bounce).delay(0).duration(1000).playOn(button2);
                break;
            case 3:
                button3.setChecked(true);
                YoYo.with(Techniques.Bounce).delay(0).duration(1000).playOn(button3);
                break;
            default:
                break;
        }
    }

    public void clear(int i) {
        switch (i) {
            case 0:
                button0.setChecked(false);
                break;
            case 1:
                button1.setChecked(false);
                break;
            case 2:
                button2.setChecked(false);
                break;
            case 3:
                button3.setChecked(false);
                break;
            case 4:
                YoYo.with(Techniques.Shake).duration(700).playOn(ly);
                button0.setChecked(false);
                button1.setChecked(false);
                button2.setChecked(false);
                button3.setChecked(false);
                break;
            default:
                break;
        }
    }

    public void init() {
        button0.setChecked(false);
        button1.setChecked(false);
        button2.setChecked(false);
        button3.setChecked(false);
    }

}
