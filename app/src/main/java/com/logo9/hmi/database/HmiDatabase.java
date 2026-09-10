package com.logo9.hmi.database;
import android.content.*;
import android.database.sqlite.*;
public class HmiDatabase extends SQLiteOpenHelper{
 public HmiDatabase(Context c){super(c,"hmi_logo.db",null,1);}
 public void onCreate(SQLiteDatabase db){
  db.execSQL("CREATE TABLE alarms(id INTEGER PRIMARY KEY AUTOINCREMENT,time TEXT,level TEXT,message TEXT)");
  db.execSQL("CREATE TABLE trends(id INTEGER PRIMARY KEY AUTOINCREMENT,time TEXT,tag TEXT,value REAL)");
 }
 public void onUpgrade(SQLiteDatabase db,int o,int n){}
 public void addAlarm(String t,String l,String m){getWritableDatabase().execSQL("INSERT INTO alarms(time,level,message) VALUES(?,?,?)",new Object[]{t,l,m});}
 public void addTrend(String t,String tag,double v){getWritableDatabase().execSQL("INSERT INTO trends(time,tag,value) VALUES(?,?,?)",new Object[]{t,tag,v});}
}