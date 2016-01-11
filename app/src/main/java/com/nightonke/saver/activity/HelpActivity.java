package com.nightonke.saver.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nightonke.saver.R;
import com.nineoldandroids.animation.Animator;

public class HelpActivity extends Activity {

    private CardView card1;
    private CardView card2;
    private CardView card3;
    private CardView card4;
    private CardView card5;
    private CardView card6;
    private CardView card7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        card1 = (CardView)findViewById(R.id.card1);
        card2 = (CardView)findViewById(R.id.card2);
        card3 = (CardView)findViewById(R.id.card3);
        card4 = (CardView)findViewById(R.id.card4);
        card5 = (CardView)findViewById(R.id.card5);
        card6 = (CardView)findViewById(R.id.card6);
        card7 = (CardView)findViewById(R.id.card7);

        int t = 700;
        int delay = 700;
        int duration = 700;

        YoYo.with(Techniques.Shake).delay(t).duration(duration)
                .withListener(new ShowListener(card1)).playOn(card1);
        t += delay;
        YoYo.with(Techniques.Shake).delay(t).duration(duration)
                .withListener(new ShowListener(card2)).playOn(card2);
        t += delay;
        YoYo.with(Techniques.Shake).delay(t).duration(duration)
                .withListener(new ShowListener(card3)).playOn(card3);
        t += delay;
        YoYo.with(Techniques.Shake).delay(t).duration(duration)
                .withListener(new ShowListener(card4)).playOn(card4);
        t += delay;
        YoYo.with(Techniques.Shake).delay(t).duration(duration)
                .withListener(new ShowListener(card5)).playOn(card5);
        t += delay;
        YoYo.with(Techniques.Shake).delay(t).duration(duration)
                .withListener(new ShowListener(card6)).playOn(card6);
        t += delay;
        YoYo.with(Techniques.Shake).delay(t).duration(duration)
                .withListener(new ShowListener(card7)).playOn(card7);
    }

    class ShowListener implements Animator.AnimatorListener {

        private View view;

        public ShowListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animator animation) {
            view.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }
}
