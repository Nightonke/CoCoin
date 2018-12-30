package com.nightonke.saver.ui;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;


/**
 * Created by Weiping on 2016/1/21.
 */
public class logoShowAnimation extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        AnimatorSet animatorAgent = getAnimatorAgent();

        animatorAgent.playTogether(
                ObjectAnimator.ofFloat(target,"translationY",target.getMeasuredHeight(), -40,20,-10,5,0),
                ObjectAnimator.ofFloat(target,"alpha",0,1,1,1)
        );
    }
}