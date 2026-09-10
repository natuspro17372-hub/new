package com.logo9.hmi.ui;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;



public class StatusLed extends View {


    private GradientDrawable led;


    public StatusLed(Context context) {

        super(context);


        led = new GradientDrawable();

        led.setShape(
                GradientDrawable.OVAL
        );


        led.setSize(
                45,
                45
        );


        setBackground(led);


        setOffline();

    }



    // Estado conectado

    public void setOnline(){

        led.setColor(
                Color.parseColor("#00C853")
        );

        invalidate();

    }



    // Estado desconectado

    public void setOffline(){

        led.setColor(
                Color.parseColor("#D50000")
        );

        invalidate();

    }



    // Estado advertencia

    public void setWarning(){

        led.setColor(
                Color.parseColor("#FFD600")
        );

        invalidate();

    }



}
