package com.alierbas.powershare;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    /** Called when the activity is first created. */
    private static final int VID = 0x2341;
    private static final int PID = 0x0001;//I believe it is 0x0000 for the Arduino Megas
    private static UsbController sUsbController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(sUsbController == null){
            sUsbController = new UsbController(this, mConnectionHandler, VID, PID);
        }


        ((SeekBar)(findViewById(R.id.seekBar1))).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if(fromUser){
                    if(sUsbController != null){
                        sUsbController.send((byte)(progress&0xFF));
                    }
                }
            }
        });


        ((Button)findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sUsbController == null)
                    sUsbController = new UsbController(MainActivity.this, mConnectionHandler, VID, PID);
                else{
                    sUsbController.stop();
                    sUsbController = new UsbController(MainActivity.this, mConnectionHandler, VID, PID);
                }
            }
        });

    }

    private final IUsbConnectionHandler mConnectionHandler = new IUsbConnectionHandler() {
        @Override
        public void onUsbStopped() {
            Log.e("**","Usb stopped!");
        }

        @Override
        public void onErrorLooperRunningAlready() {
            Log.e("**","Looper already running!");
        }

        @Override
        public void onDeviceNotFound() {
            if(sUsbController != null){
                sUsbController.stop();
                sUsbController = null;
            }
        }
    };

}
