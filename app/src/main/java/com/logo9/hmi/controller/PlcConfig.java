package com.logo9.hmi.controller;

import android.content.Context;
import android.content.SharedPreferences;

public class PlcConfig {

    private SharedPreferences prefs;


    // Dirección IP del LOGO!
    private String ip;

    // Puerto Modbus TCP
    private int modbusPort;

    // Puerto Web HMI del LOGO!
    private int webPort;

    // Dirección del esclavo Modbus
    private int unitId;

    // Tiempo máximo de espera de comunicación
    private int timeout;


    public PlcConfig() {

        // Valores iniciales
        ip = "192.168.1.2";

        modbusPort = 502;

        webPort = 80;

        unitId = 1;

        timeout = 2000;
    }

    public PlcConfig(Context c){
        prefs=c.getSharedPreferences("PLC_CONFIG",0);
        ip=prefs.getString("ip","192.168.1.2");
        modbusPort=prefs.getInt("port",502);
        unitId=prefs.getInt("unit",1);
        timeout=prefs.getInt("timeout",2000);
        webPort=prefs.getInt("web",80);
    }

    public void save(){
        if(prefs!=null) prefs.edit().putString("ip",ip).putInt("port",modbusPort).putInt("unit",unitId).putInt("timeout",timeout).putInt("web",webPort).apply();
    }


    // Obtener IP
    public String getIp() {
        return ip;
    }


    // Cambiar IP
    public void setIp(String ip) {
        this.ip = ip;
    }


    // Obtener puerto Modbus
    public int getModbusPort() {
        return modbusPort;
    }


    // Cambiar puerto Modbus
    public void setModbusPort(int modbusPort) {
        this.modbusPort = modbusPort;
    }


    // Obtener puerto Web
    public int getWebPort() {
        return webPort;
    }


    // Cambiar puerto Web
    public void setWebPort(int webPort) {
        this.webPort = webPort;
    }


    // Obtener Unit ID
    public int getUnitId() {
        return unitId;
    }


    // Cambiar Unit ID
    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }


    // Obtener Timeout
    public int getTimeout() {
        return timeout;
    }


    // Cambiar Timeout
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}

