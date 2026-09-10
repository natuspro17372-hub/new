package com.logo9.hmi.screens;

import android.os.Bundle;
import android.graphics.Color;
import com.logo9.hmi.controller.PlcConfig;
import com.logo9.hmi.controller.PlcController;

public class DiagnosticActivity extends ProfessionalActivity{

 public void onCreate(Bundle b){
  super.onCreate(b);
  base("DIAGNOSTICO PLC");

  PlcConfig cfg=new PlcConfig(this);
  PlcController plc=new PlcController(cfg);

  root.addView(item("PLC IP: "+cfg.getIp()));
  root.addView(item("MODBUS PORT: "+cfg.getModbusPort()));
  root.addView(item("UNIT ID: "+cfg.getUnitId()));

  new Thread(() -> {
    String state;
    try{
      boolean[] test=plc.readInputs(0,1);
      state="COMUNICACION OK - MODBUS TCP ACTIVO";
    }catch(Exception e){
      state="SIN RESPUESTA PLC - "+e.getMessage();
    }
    final String result=state;
    runOnUiThread(() -> root.addView(item(result)));
  }).start();

  root.addView(item("Sistema preparado para LOGO! 8"));
  setContentView(root);
 }
}
