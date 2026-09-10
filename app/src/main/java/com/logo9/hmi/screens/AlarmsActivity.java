package com.logo9.hmi.screens;
import android.os.*;import android.widget.*;
import com.logo9.hmi.database.HmiDatabase;
public class AlarmsActivity extends ProfessionalActivity{
 public void onCreate(Bundle b){super.onCreate(b);base("ALARMAS / EVENTOS");
 root.addView(item("Registro local de alarmas LOGO V8"));
 root.addView(item("Base SQLite activa"));
 setContentView(root);
 }
}