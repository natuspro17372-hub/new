package com.logo9.hmi.screens;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logo9.hmi.controller.PlcConfig;
import com.logo9.hmi.controller.PlcController;
import com.logo9.hmi.ui.HmiCard;
import com.logo9.hmi.ui.StatusLed;

public class InputsActivity extends Activity {

    private Handler handler = new Handler();
    private TextView[] inputStatus = new TextView[8];
    private StatusLed[] leds = new StatusLed[8];
    private PlcController plc;

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);

        getWindow().setFlags(1024,1024);

        PlcConfig cfg = new PlcConfig(this);
        plc = new PlcController(cfg);

        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20,20,20,20);
        root.setBackgroundColor(Color.parseColor("#040B14"));

        TextView title=new TextView(this);
        title.setText("DIGITAL INPUTS\nREAL TIME PLC MONITOR");
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        HmiCard card=new HmiCard(this);
        card.setOrientation(LinearLayout.VERTICAL);

        for(int i=0;i<8;i++){
            LinearLayout row=new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);

            leds[i]=new StatusLed(this);
            row.addView(leds[i],new LinearLayout.LayoutParams(45,45));

            inputStatus[i]=new TextView(this);
            inputStatus[i].setText("I"+(i+1)+"   WAITING");
            inputStatus[i].setTextColor(Color.WHITE);
            inputStatus[i].setTextSize(18);
            row.addView(inputStatus[i]);

            card.addView(row);
        }

        root.addView(card);
        setContentView(root);
        startReading();
    }

    private void startReading(){
        handler.postDelayed(new Runnable(){
            public void run(){
                readInputs();
                handler.postDelayed(this,1000);
            }
        },500);
    }

    private void readInputs(){
        new Thread(()->{
            try{
                boolean[] data=plc.readInputs(0,8);
                runOnUiThread(()->{
                    for(int i=0;i<8;i++){
                        inputStatus[i].setText("I"+(i+1)+"     "+(data[i]?"ON":"OFF"));
                        if(data[i]) leds[i].setOnline();
                        else leds[i].setOffline();
                    }
                });
            }catch(Exception e){
                runOnUiThread(()->{
                    for(int i=0;i<8;i++){
                        inputStatus[i].setText("I"+(i+1)+"     NO CONNECTION");
                        leds[i].setOffline();
                    }
                });
            }
        }).start();
    }

    protected void onDestroy(){
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
