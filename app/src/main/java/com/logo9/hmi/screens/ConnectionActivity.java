package com.logo9.hmi.screens;

import android.os.Bundle;
import android.graphics.Color;
import android.widget.*;
import android.view.Gravity;
import com.logo9.hmi.controller.PlcConfig;

public class ConnectionActivity extends android.app.Activity{

 EditText ip,port,unit;

 public void onCreate(Bundle b){
  super.onCreate(b);

  PlcConfig cfg=new PlcConfig(this);

  LinearLayout root=new LinearLayout(this);
  root.setOrientation(LinearLayout.VERTICAL);
  root.setPadding(25,25,25,25);
  root.setBackgroundColor(Color.parseColor("#040B14"));

  TextView t=new TextView(this);
  t.setText("PLC CONNECTION\nCONFIGURATION");
  t.setTextColor(Color.WHITE);
  t.setTextSize(24);
  root.addView(t);

  ip=field("IP PLC",cfg.getIp());
  port=field("MODBUS PORT",""+cfg.getModbusPort());
  unit=field("UNIT ID",""+cfg.getUnitId());

  root.addView(ip);root.addView(port);root.addView(unit);

  Button save=new Button(this);
  save.setText("SAVE PLC PROFILE");
  save.setOnClickListener(v->{
    cfg.setIp(ip.getText().toString());
    cfg.setModbusPort(Integer.parseInt(port.getText().toString()));
    cfg.setUnitId(Integer.parseInt(unit.getText().toString()));
    cfg.save();
    Toast.makeText(this,"PLC CONFIG SAVED",Toast.LENGTH_LONG).show();
  });
  root.addView(save);

  setContentView(root);
 }

 EditText field(String name,String value){
  EditText e=new EditText(this);
  e.setHint(name);
  e.setText(value);
  e.setTextColor(Color.WHITE);
  e.setHintTextColor(Color.GRAY);
  e.setTextSize(18);
  return e;
 }
}
