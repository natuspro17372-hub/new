package com.logo9.hmi.screens;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.graphics.Color;
import android.widget.*;

import com.logo9.hmi.controller.PlcConfig;
import com.logo9.hmi.controller.PlcController;

import java.io.IOException;

public class IOActivity extends Activity {

    private PlcController plc;
    private Handler handler = new Handler();

    private TextView[] inputStatus = new TextView[8];
    private Button[] outputButtons = new Button[4];

    private int[] outputAddress = {
            8192,
            8193,
            8194,
            8195
    };

    private boolean[] outputState = {
            false,false,false,false
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        PlcConfig config = new PlcConfig();

config.setIp("192.168.1.2");
config.setModbusPort(502);
config.setUnitId(1);
config.setTimeout(3000);
        

        plc = new PlcController(config);


        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(30,30,30,30);


        TextView title = new TextView(this);
        title.setText("CONTROL PLC\nLOGO! 8 MODBUS TCP");
        title.setTextSize(24);
        title.setTextColor(Color.CYAN);

        root.addView(title);


        TextView inTitle = new TextView(this);
        inTitle.setText("\nENTRADAS DIGITALES");
        inTitle.setTextSize(20);

        root.addView(inTitle);


        for(int i=0;i<8;i++){

            inputStatus[i]=new TextView(this);

            inputStatus[i].setText(
                    "I"+(i+1)+"  OFF"
            );

            inputStatus[i].setTextSize(18);

            root.addView(inputStatus[i]);
        }


        TextView outTitle = new TextView(this);
        outTitle.setText("\nSALIDAS DIGITALES");
        outTitle.setTextSize(20);

        root.addView(outTitle);



        for(int i=0;i<4;i++){

            final int index=i;

            outputButtons[i]=new Button(this);

            outputButtons[i].setText(
                    "Q"+(i+1)+" OFF"
            );


            outputButtons[i].setOnClickListener(v -> {

                outputState[index]=!outputState[index];

                try{

                    plc.writeOutput(
                            outputAddress[index],
                            outputState[index]
                    );

                }catch(Exception e){

    Toast.makeText(
        IOActivity.this,
        "Error Q"+(index+1)+": Verificar Enlace Modbus",
        Toast.LENGTH_SHORT
    ).show();

}


                updateButton(index);

            });


            root.addView(outputButtons[i]);
        }


        setContentView(root);


        startReading();

    }



    private void startReading(){

        handler.postDelayed(new Runnable(){

            @Override
            public void run(){

                readInputs();
                readOutputs();
                handler.postDelayed(this,1000);

            }

        },1000);

    }



    private void readInputs(){

        new Thread(() -> {

            try{

                boolean[] inputs =
                        plc.readInputs(0,8);


                runOnUiThread(() -> {


                    for(int i=0;i<8;i++){

                        inputStatus[i].setText(
                                "I"+(i+1)+
                                "  "+
                                (inputs[i]?"ON":"OFF")
                        );

                        inputStatus[i].setTextColor(
                                inputs[i]?
                                Color.GREEN:
                                Color.RED
                        );

                    }


                });


            }catch(Exception e){

                e.printStackTrace();

            }


        }).start();

    }

 private void readOutputs(){

    new Thread(() -> {

        try {

            boolean[] outputs = plc.readOutputs(8192,4);

            runOnUiThread(() -> {

                for(int i=0;i<4;i++){

                    outputState[i] = outputs[i];

                    updateButton(i);

                }

            });

        } catch(Exception e){

            e.printStackTrace();

        }

    }).start();

}

    private void updateButton(int index){
        outputButtons[index].setHeight(100);
        outputButtons[index].setTextSize(18);

    if(outputState[index]){

        outputButtons[index].setText(
                "Q"+(index+1)+" ON"
        );

        outputButtons[index].setTextColor(Color.WHITE);
        outputButtons[index].setBackgroundColor(Color.rgb(0,170,0));

    }else{

        outputButtons[index].setText(
                "Q"+(index+1)+" OFF"
        );

        outputButtons[index].setTextColor(Color.WHITE);
        outputButtons[index].setBackgroundColor(Color.rgb(80,80,80));

    }

}

}
