package com.logo9.hmi.screens;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;

public class ProfessionalActivity extends Activity {
    protected LinearLayout root;
    protected void base(String title){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,28,28,28); root.setBackgroundColor(Color.rgb(8,18,30));
        TextView t=new TextView(this); t.setText(title); t.setTextColor(Color.CYAN); t.setTextSize(25); t.setPadding(0,20,0,25); root.addView(t);
    }
    protected TextView item(String s){
        TextView v=new TextView(this); v.setText(s); v.setTextColor(Color.WHITE); v.setTextSize(18); v.setPadding(22,20,22,20);
        GradientDrawable g=new GradientDrawable(); g.setColor(Color.rgb(20,35,50)); g.setCornerRadius(18); g.setStroke(1,Color.rgb(30,90,120)); v.setBackground(g);
        LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(-1,-2); p.setMargins(0,8,0,8); v.setLayoutParams(p); return v;
    }
}
