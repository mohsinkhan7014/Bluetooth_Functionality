package com.mohsin.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Bluetooth1 extends AppCompatActivity {

    TextView textView;
    Button scan,disable,activitaed;

    BluetoothAdapter ba;
    ProgressDialog pd;
    ArrayList<BluetoothDevice> ad=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth1);
        textView=findViewById(R.id.txt);
        scan=findViewById(R.id.enable);
        disable=findViewById(R.id.disable);
        activitaed=findViewById(R.id.Paired);
        ba=BluetoothAdapter.getDefaultAdapter();
        pd=new ProgressDialog(this);


        if(ba==null)
        {
               ShowUnsupported();
        }
        else
        {

        }
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        activitaed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ba.isEnabled())
                {
                    ba.disable();
                    showDisable();
                }
                else
                {
                    Intent intent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,1000);
                }
            }
        });
        if(ba.isEnabled())
        {
            showEnabled();
        }
        else
        {
            showDisable();
        }



        IntentFilter filter=new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    }

    @Override
    protected void onPause() {
        if(ba!=null)
        {
            if(ba.isDiscovering())
            {
                ba.cancelDiscovery();
            }
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    void showEnabled()
    {
        textView.setText("Bluetoth is enable");
        textView.setTextColor(Color.BLUE);
        activitaed.setEnabled(true);
        scan.setEnabled(true);
        disable.setEnabled(true);
    }

    void showDisable()
    {
        textView.setText("Bluetoth is OFFe");
        textView.setTextColor(Color.GREEN);
        activitaed.setText("Enable");
        activitaed.setEnabled(true);
        scan.setEnabled(false);
        disable.setEnabled(false);
    }
    void ShowUnsupported()
    {
        textView.setText("Bluetooth is not working");
        textView.setTextColor(Color.CYAN);
        activitaed.setText("Enable");
        activitaed.setEnabled(false);
        scan.setEnabled(true);
        disable.setEnabled(true);

    }

    final BroadcastReceiver mReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED.equals(action))
            {
                int state=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
                if(state==BluetoothAdapter.STATE_ON)
                {
                    Toast.makeText(Bluetooth1.this,"Enabled",Toast.LENGTH_LONG).show();
                    showEnabled();
                }
            }
        }
    };



}