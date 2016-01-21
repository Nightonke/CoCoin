package com.nightonke.saver.activity;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.nightonke.saver.R;
import com.nightonke.saver.model.CoCoin;
import com.nightonke.saver.model.RecordManager;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import cn.bmob.v3.Bmob;

public class SplashActivity extends AwesomeSplash {

    @Override
    public void initSplash(ConfigSplash configSplash) {

        configSplash.setBackgroundColor(R.color.my_blue); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.cocoin_logo_splash); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("CoCoin\ncopyright © github.com/Nightonke\n");
        configSplash.setTitleTextColor(R.color.white);
        configSplash.setTitleTextSize(15f); //float value
        configSplash.setAnimTitleDuration(500);
        configSplash.setAnimTitleTechnique(Techniques.FadeIn);
        configSplash.setTitleFont("fonts/LatoLatin-Light.ttf"); //provide string to your font located in assets/fonts/

        Bmob.initialize(CoCoinApplication.getAppContext(), CoCoin.APPLICATION_ID);
        RecordManager recordManager = RecordManager.getInstance(this.getApplicationContext());

    }

    @Override
    public void animationsFinished() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
