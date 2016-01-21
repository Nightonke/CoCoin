package com.nightonke.saver.ui;

import android.view.View;

import com.daimajia.androidanimations.library.BaseViewAnimator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * Created by Weiping on 2016/1/21.
 */
public class logoShowAnimation extends BaseViewAnimator {
    @Override
    protected void prepare(View target) {
        getAnimatorAgent().playTogether(
                ObjectAnimator.ofFloat(target,"translationY",target.getMeasuredHeight(), -40,20,-10,5,0),
                ObjectAnimator.ofFloat(target,"alpha",0,1,1,1)
        );
    }
}
