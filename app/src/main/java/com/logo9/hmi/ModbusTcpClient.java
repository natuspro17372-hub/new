package com.logo9.hmi;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class ModbusTcpClient {
    private final AtomicInteger tx = new AtomicInteger(1);
    private static void validate(int address, int quantity) {
        if (address < 0 || address > 65535) throw new IllegalArgumentException("Dirección fuera de rango");
        if (quantity < 1 || quantity > 125) throw new IllegalArgumentException("Cantidad fuera de rango");
    }
    public int[] readHoldingRegisters(String host, int port, int unitId, int address, int quantity, int timeoutMs) throws IOException {
        validate(address, quantity);
        byte[] pdu = new byte[]{3,(byte)(address>>8),(byte)address,(byte)(quantity>>8),(byte)quantity};
        byte[] data = request(host,port,unitId,pdu,timeoutMs);
        if ((data[0]&0xFF)!=3) throw new IOException("Respuesta Modbus inesperada");
        int bytes=data[1]&0xFF; if(bytes!=quantity*2) throw new IOException("Longitud Modbus inválida");
        int[] out=new int[quantity]; for(int i=0;i<quantity;i++) out[i]=((data[2+i*2]&255)<<8)|(data[3+i*2]&255); return out;
    }
    public boolean[] readCoils(String host,int port,int unitId,int address,int quantity,int timeoutMs)throws IOException{
        if(quantity<1||quantity>2000) throw new IllegalArgumentException("Cantidad de coils fuera de rango");
        byte[] pdu=new byte[]{1,(byte)(address>>8),(byte)address,(byte)(quantity>>8),(byte)quantity};
        byte[] data=request(host,port,unitId,pdu,timeoutMs); if((data[0]&255)!=1)throw new IOException("Respuesta Modbus inesperada");
        boolean[] out=new boolean[quantity]; for(int i=0;i<quantity;i++)out[i]=(data[2+(i/8)]&(1<<(i%8)))!=0; return out;
    }
    public void writeSingleRegister(String host,int port,int unitId,int address,int value,int timeoutMs)throws IOException{
        if(value<0||value>65535)throw new IllegalArgumentException("Valor fuera de rango");
        byte[] pdu=new byte[]{6,(byte)(address>>8),(byte)address,(byte)(value>>8),(byte)value}; request(host,port,unitId,pdu,timeoutMs);
    }
    public void writeSingleCoil(String host,int port,int unitId,int address,boolean value,int timeoutMs)throws IOException{
        byte hi=(byte)(value?0xFF:0x00); byte[] pdu=new byte[]{5,(byte)(address>>8),(byte)address,hi,0}; request(host,port,unitId,pdu,timeoutMs);
    }
    private byte[] request(String host,int port,int unitId,byte[] pdu,int timeoutMs)throws IOException{
        int tid=tx.getAndUpdate(v->v>=65535?1:v+1); int len=pdu.length+1;
        try(Socket s=new Socket()){
            s.connect(new InetSocketAddress(host,port),timeoutMs); s.setSoTimeout(timeoutMs); s.setTcpNoDelay(true);
            DataOutputStream o=new DataOutputStream(new BufferedOutputStream(s.getOutputStream())); DataInputStream in=new DataInputStream(new BufferedInputStream(s.getInputStream()));
            o.writeShort(tid); o.writeShort(0); o.writeShort(len); o.writeByte(unitId); o.write(pdu); o.flush();
            int rtid=in.readUnsignedShort(), proto=in.readUnsignedShort(), rlen=in.readUnsignedShort(), uid=in.readUnsignedByte();
            if(proto!=0||rtid!=tid||uid!=(unitId&255)||rlen<2||rlen>260)throw new IOException("Cabecera Modbus inválida");
            byte[] body=new byte[rlen-1]; in.readFully(body);
            int fc=body[0]&255; if((fc&0x80)!=0){int code=body.length>1?body[1]&255:-1; throw new IOException("Excepción Modbus "+code);}
            return body;
        }
    }
}
