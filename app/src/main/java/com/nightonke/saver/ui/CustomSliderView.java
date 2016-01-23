package com.nightonke.saver.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.nightonke.saver.R;

/**
 * Created by Weiping on 2016/1/23.
 */
public class CustomSliderView extends BaseSliderView {
    private static Typeface font = null;
    private Context context ;
    public CustomSliderView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_slider_view,null);
        LinearLayout description = (LinearLayout)v.findViewById(R.id.description_layout);
        description.setVisibility(View.GONE);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        bindEventAndShow(v, target);
        return v;
    }
}
