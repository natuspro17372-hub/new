package com.logo9.hmi.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;

public class HmiButton extends Button {

    public HmiButton(Context context){
        super(context);
        setAllCaps(false);
        setTextColor(Color.WHITE);
        setTextSize(15);
        setGravity(Gravity.CENTER);
        setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        setMinHeight(64);
        setPadding(16,12,16,12);
        setElevation(10);
        normal();
        setOnTouchListener((v,e)->{
            if(e.getAction()==MotionEvent.ACTION_DOWN){
                setScaleX(0.97f); setScaleY(0.97f);
            }else if(e.getAction()==MotionEvent.ACTION_UP || e.getAction()==MotionEvent.ACTION_CANCEL){
                setScaleX(1f); setScaleY(1f);
            }
            return false;
        });
    }

    private GradientDrawable bg(String fill,String stroke){
        GradientDrawable d=new GradientDrawable();
        d.setColor(Color.parseColor(fill));
        d.setCornerRadius(22);
        d.setStroke(2,Color.parseColor(stroke));
        return d;
    }

    public void normal(){
        setBackground(bg("#101F30","#0099CC"));
    }

    public void setActive(){
        setBackground(bg("#064E3B","#00FF88"));
    }

    public void setInactive(){
        normal();
    }

    public void setWarning(){
        setBackground(bg("#4A3200","#FFB300"));
    }
}
