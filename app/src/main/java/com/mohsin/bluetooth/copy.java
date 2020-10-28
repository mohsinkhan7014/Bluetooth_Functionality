package com.mohsin.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class copy extends AppCompatActivity {
    TextView status;
    Button pair,scan,activate;
    BluetoothAdapter ba;
    ProgressDialog pd;
    ArrayList<BluetoothDevice> ad=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy);
        status=findViewById(R.id.status);
        pair=findViewById(R.id.pair);
        scan=findViewById(R.id.scan);
        activate=findViewById(R.id.activate);
        ba=BluetoothAdapter.getDefaultAdapter();
        pd=new ProgressDialog(this);
        pd.setMessage("Scanning....");
        pd.setCancelable(false);
        pd.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ba.cancelDiscovery();
                    }
                });

        if(ba==null)
        {
            showUnsupported();
        }
        else
        {

            pair.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Set<BluetoothDevice> pdevice=ba.getBondedDevices();
                    if(pdevice==null || pdevice.size()==0)
                    {
                        Toast.makeText(getApplicationContext(),"NO pair device found",Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        ArrayList<BluetoothDevice> list=new ArrayList<>();
                        list.addAll(pdevice);
                        Intent intent=new Intent(getApplicationContext(),Custom_Pair_Unpair.class);
                        intent.putParcelableArrayListExtra("device.list",list);
                        startActivity(intent);
                    }
                }
            });
        }
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ba.startDiscovery();
            }
        });
        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ba.isEnabled())
                {
                    ba.disable();
                    showDisabled();
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
            showDisabled();
        }
        IntentFilter filter=new IntentFilter();
        filter.addAction((BluetoothAdapter.ACTION_STATE_CHANGED));
        filter.addAction((BluetoothDevice.ACTION_FOUND));
        filter.addAction((BluetoothAdapter.ACTION_DISCOVERY_STARTED));
        filter.addAction((BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        registerReceiver(mrec,filter);
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
//unregister ur reciver
        super.onDestroy();
    }
    void showEnabled()
    {
        status.setText("Bluetooth is ON");
        status.setTextColor(Color.BLUE);
        activate.setText("Disable");
        activate.setEnabled(true);
        pair.setEnabled(true);
        scan.setEnabled(true);
    }
    void showDisabled()
    {
        status.setText("Bluetooth is OFF");
        status.setTextColor(Color.RED);
        activate.setText("Enable");
        activate.setEnabled(true);
        pair.setEnabled(false);
        scan.setEnabled(false);
    }
    void showUnsupported()
    {
        status.setText("Bluetooth is not supported");
        status.setTextColor(Color.CYAN);
        activate.setText("Enable");
        activate.setEnabled(false);
        pair.setEnabled(true);
        scan.setEnabled(true);
    }
    final BroadcastReceiver mrec=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action=intent.getAction();
            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
            {
                int state=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,BluetoothAdapter.ERROR);
                if(state==BluetoothAdapter.STATE_ON)
                {
                    Toast.makeText(context, "Enabled", Toast.LENGTH_SHORT).show();
                    showEnabled();
                }

            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
            {
                ad=new ArrayList<BluetoothDevice>();
                pd.show();
            }
            else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))
            {
                pd.dismiss();
                Intent newintent=new Intent(getApplicationContext(),Custom_Pair_Unpair.class);
                newintent.putParcelableArrayListExtra("device.list",ad);
                startActivity(newintent);
            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice bd=(BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                ad.add(bd);
                Toast.makeText(context, "Found", Toast.LENGTH_SHORT).show();

            }



        }
    };

}