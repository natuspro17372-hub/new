package com.logo9.hmi.screens;

import android.os.Bundle;
import com.logo9.hmi.controller.PlcConfig;
import com.logo9.hmi.controller.PlcController;

public class VariablesActivity extends ProfessionalActivity{

 public void onCreate(Bundle b){
  super.onCreate(b);
  base("VARIABLES VW - MODBUS REAL");

  PlcConfig cfg=new PlcConfig(this);
  PlcController plc=new PlcController(cfg);

  root.addView(item("PLC: "+cfg.getIp()));

  new Thread(() -> {
    try{
      int[] vw=plc.readRegisters(0,8);
      runOnUiThread(() -> {
        for(int i=0;i<vw.length;i++){
          root.addView(item("VW"+(i*10)+"   VALOR: "+vw[i]));
        }
      });
    }catch(Exception e){
      runOnUiThread(() -> root.addView(item("ERROR LECTURA VW: "+e.getMessage())));
    }
  }).start();

  setContentView(root);
 }
}
