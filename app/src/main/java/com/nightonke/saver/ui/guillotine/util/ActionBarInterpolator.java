package com.nightonke.saver.ui.guillotine.util;

import android.animation.TimeInterpolator;

/**
 * Created by Dmytro Denysenko on 5/15/15.
 */
public class ActionBarInterpolator implements TimeInterpolator {

    private static final float FIRST_BOUNCE_PART = 0.375f;
    private static final float SECOND_BOUNCE_PART = 0.625f;

    @Override
    public float getInterpolation(float t) {
        if (t < FIRST_BOUNCE_PART) {
            return (-28.4444f) * t * t + 10.66667f * t;
        } else if (t < SECOND_BOUNCE_PART) {
            return (21.33312f) * t * t - 21.33312f * t + 4.999950f;
        } else {
            return (-9.481481f) * t * t + 15.40741f * t - 5.925926f;
        }
    }
}