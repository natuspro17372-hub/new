package com.logo9.hmi.screens;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.content.Intent;
import android.view.Gravity;
import android.widget.*;

public class PlcManagerActivity extends Activity {

    LinearLayout root;

    TextView t(String s,int z){
        TextView v=new TextView(this);
        v.setText(s);
        v.setTextSize(z);
        v.setTextColor(Color.WHITE);
        v.setPadding(24,20,24,20);
        return v;
    }

    Button b(String text){
        Button b=new Button(this);
        b.setText(text);
        b.setTextSize(16);
        return b;
    }

    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);

        root=new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(20,20,20,20);
        root.setBackgroundColor(Color.parseColor("#07131F"));

        root.addView(t("PLC MANAGER - HMI LOGO PRO",24));
        root.addView(t("Gestión de múltiples controladores Siemens LOGO V8",16));

        root.addView(t(
            "✓ PLC activos\n✓ IP configurable\n✓ Puerto Modbus TCP\n✓ Estado online/offline\n✓ Cambio rápido de controlador",
           18));

        Button alarms=b("Alarmas e Históricos");
        alarms.setOnClickListener(v ->
            startActivity(new Intent(this, AlarmsActivity.class)));
        root.addView(alarms);

        Button vars=b("Variables VM / VW");
        vars.setOnClickListener(v ->
            startActivity(new Intent(this, VariablesActivity.class)));
        root.addView(vars);

        setContentView(root);
    }
}
