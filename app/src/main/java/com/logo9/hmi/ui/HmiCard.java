package com.logo9.hmi.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class HmiCard extends LinearLayout {


    public HmiCard(Context context) {

        super(context);

        setOrientation(VERTICAL);

        setPadding(
                32,
                28,
                32,
                28
        );


        GradientDrawable background = new GradientDrawable();

        background.setColor(Color.parseColor("#142433"));

        background.setCornerRadius(25);

        background.setStroke(
                2,
                Color.parseColor("#24506B")
        );

        setElevation(12);
        setBackground(background);


        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );


        setLayoutParams(params);

    }


}
