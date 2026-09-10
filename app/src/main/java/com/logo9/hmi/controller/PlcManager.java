package com.logo9.hmi.controller;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class PlcManager {
    private final android.content.SharedPreferences prefs;
    public PlcManager(Context c){ prefs=c.getSharedPreferences("PLC_LIST",0); }
    public void save(String name,String ip,int port,int unit){
        prefs.edit().putString(name,name+";"+ip+";"+port+";"+unit).apply();
    }
    public List<String> getAll(){
        return new ArrayList<>(prefs.getAll().values().stream().map(Object::toString).toList());
    }
}
