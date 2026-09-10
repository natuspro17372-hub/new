package com.logo9.hmi.screens;
import android.os.Bundle;
public class LogicActivity extends ProfessionalActivity{
 public void onCreate(Bundle b){super.onCreate(b);base("BLOQUES / LOGICA");root.addView(item("I1 AND I2  ---> Q1"));root.addView(item("TEMP > SETPOINT ---> Q2"));root.addView(item("TIMER 5s ---> Q3"));setContentView(root);}
}
