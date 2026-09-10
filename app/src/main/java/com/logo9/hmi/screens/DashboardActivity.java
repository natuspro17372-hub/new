package com.logo9.hmi.screens;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import android.content.Intent;
import com.logo9.hmi.controller.PlcConfig;

import com.logo9.hmi.ui.HmiCard;
import com.logo9.hmi.ui.HmiButton;
import com.logo9.hmi.ui.StatusLed;
import com.logo9.hmi.WebHmiActivity;


public class DashboardActivity extends Activity {

    private final int CYAN = Color.parseColor("#00E5FF");
    private final int WHITE = Color.WHITE;
    private final int GRAY = Color.parseColor("#90A4AE");
    private final int BG = Color.parseColor("#040B14");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
        buildDashboard();
    }


    private TextView label(String text,float size,int color){

        TextView t=new TextView(this);
        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        t.setPadding(18,14,18,14);
        return t;

    }


    private void buildDashboard(){

        ScrollView scroll=new ScrollView(this);

        LinearLayout root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(18,18,18,18);
        root.setBackgroundColor(BG);

        // Carga dinamica del PLC activo
        PlcConfig plcConfig = new PlcConfig(this);
        String plcIp = plcConfig.getIp();
        int plcPort = plcConfig.getModbusPort();

        scroll.addView(root);

        // HMI PRO Evolution V3
        Button plcManager = new Button(this);
        plcManager.setText("PLC MANAGER\nMULTI LOGO V8");
        plcManager.setTextSize(16);
        plcManager.setOnClickListener(v ->
            startActivity(new Intent(this, PlcManagerActivity.class)));
        root.addView(plcManager);

        Button alarmCenter = new Button(this);
        alarmCenter.setText("ALARMAS + EVENTOS + HISTÓRICOS");
        alarmCenter.setOnClickListener(v ->
            startActivity(new Intent(this, AlarmsActivity.class)));
        root.addView(alarmCenter);



        TextView title=label(
                "LOGO! HMI PRO\nSMART INDUSTRIAL CONTROL",
                25,
                WHITE
        );
        title.setGravity(Gravity.CENTER);
        root.addView(title);



        HmiCard connection=new HmiCard(this);

        connection.addView(label(
                "PLC STATUS",
                18,
                CYAN
        ));


        LinearLayout status=new LinearLayout(this);
        status.setGravity(Gravity.CENTER_VERTICAL);


        StatusLed led=new StatusLed(this);
        led.setOnline();


        status.addView(
                led,
                new LinearLayout.LayoutParams(75,75)
        );


        status.addView(label(
                "ONLINE\nSIEMENS LOGO! 8\nMODBUS TCP\n192.168.1.2 : 502",
                16,
                WHITE
        ));


        connection.addView(status);
        root.addView(connection);



        HmiCard monitor=new HmiCard(this);

        monitor.addView(label(
                "SYSTEM MONITOR\n\n" +
                "● 8 DIGITAL INPUTS\n" +
                "● 4 DIGITAL OUTPUTS\n" +
                "● VW REGISTERS\n" +
                "● MODBUS TCP READY",
                16,
                WHITE
        ));

        root.addView(monitor);



        root.addView(label(
                "CONTROL CENTER",
                19,
                CYAN
        ));



        addButton(root,"DIGITAL INPUTS",
                new Intent(this,InputsActivity.class));

        addButton(root,"DIGITAL OUTPUTS",
                new Intent(this,OutputsActivity.class));

        addButton(root,"PLC CONNECTION",
                new Intent(this,ConnectionActivity.class));

        addButton(root,"HMI WEB",
                new Intent(this,WebHmiActivity.class));

        addButton(root,"VARIABLES VW",
                new Intent(this,VariablesActivity.class));

        addButton(root,"DIAGNOSTIC",
                new Intent(this,DiagnosticActivity.class));



        root.addView(label(
                "LOGO! HMI PRO v8.0 INDUSTRIAL EDITION\nReal Modbus TCP Control Platform",
                12,
                GRAY
        ));


        setContentView(scroll);

    }



    private void addButton(
            LinearLayout root,
            String name,
            Intent intent
    ){

        HmiButton b=new HmiButton(this);

        b.setText(name);

        b.setTextSize(15);

        b.setOnClickListener(
                v -> startActivity(intent)
        );


        LinearLayout.LayoutParams p=
                new LinearLayout.LayoutParams(
                        -1,
                        85
                );

        p.setMargins(
                0,
                8,
                0,
                8
        );

        b.setLayoutParams(p);

        root.addView(b);

    }

}
