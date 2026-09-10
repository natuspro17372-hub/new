package com.logo9.hmi.controller;

import com.logo9.hmi.ModbusTcpClient;

import java.io.IOException;


public class PlcController {


    private ModbusTcpClient modbusClient;

    private PlcConfig config;



    public PlcController(PlcConfig config) {

        this.config = config;

        this.modbusClient = new ModbusTcpClient();

    }



    // Leer entradas digitales I

    public boolean[] readInputs(int address, int quantity)
            throws IOException {


        return modbusClient.readCoils(
                config.getIp(),
                config.getModbusPort(),
                config.getUnitId(),
                address,
                quantity,
                config.getTimeout()
        );

    }



    // Escribir salida Q

    public void writeOutput(
            int address,
            boolean state
    ) throws IOException {


        modbusClient.writeSingleCoil(
                config.getIp(),
                config.getModbusPort(),
                config.getUnitId(),
                address,
                state,
                config.getTimeout()
        );

    }
     // Leer salidas digitales Q
public boolean[] readOutputs(int address, int quantity)
        throws IOException {

    return modbusClient.readCoils(
            config.getIp(),
            config.getModbusPort(),
            config.getUnitId(),
            address,
            quantity,
            config.getTimeout()
    );
}


    // Leer registros VW

    public int[] readRegisters(
            int address,
            int quantity
    ) throws IOException {


        return modbusClient.readHoldingRegisters(
                config.getIp(),
                config.getModbusPort(),
                config.getUnitId(),
                address,
                quantity,
                config.getTimeout()
        );

    }



    public PlcConfig getConfig(){

        return config;

    }


}

