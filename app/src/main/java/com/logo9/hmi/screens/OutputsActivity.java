package com.logo9.hmi.screens;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.widget.*;
import com.logo9.hmi.controller.*;
import com.logo9.hmi.ui.*;

public class OutputsActivity extends Activity{
 private PlcController plc;
 private HmiButton[] buttons=new HmiButton[4];
 @Override public void onCreate(Bundle b){
  super.onCreate(b);
  getWindow().setFlags(1024,1024);
  plc=new PlcController(new PlcConfig(this));
  LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(20,20,20,20); root.setBackgroundColor(Color.parseColor("#040B14"));
  TextView t=new TextView(this); t.setText("DIGITAL OUTPUTS\\nREAL PLC CONTROL"); t.setTextColor(Color.WHITE); t.setTextSize(22); root.addView(t);
  for(int i=0;i<4;i++){ final int n=i; buttons[i]=new HmiButton(this); buttons[i].setText("Q"+(i+1)+" OFF"); buttons[i].setOnClickListener(v->write(n)); root.addView(buttons[i],new LinearLayout.LayoutParams(-1,80)); }
  setContentView(root);
 }
 private void write(int n){ new Thread(()->{try{plc.writeOutput(n,true);runOnUiThread(()->{buttons[n].setText("Q"+(n+1)+" ON");buttons[n].setActive();});}catch(Exception e){runOnUiThread(()->Toast.makeText(this,"PLC NO CONNECT",0).show());}}).start(); }
}
