package com.mohsin.bluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

public class MainActivity extends AppCompatActivity {
    Button on,off,pared,search;
    ListView lv;
    BluetoothAdapter ba;
    Set<BluetoothDevice> paireddevice;
    TextView tv;
    ArrayAdapter<String> arrayadap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        on=findViewById(R.id.on);
        off=findViewById(R.id.off);
        pared=findViewById(R.id.Paired);
        search=findViewById(R.id.search);
        lv=findViewById(R.id.lv1);
        tv=findViewById(R.id.text);
        ba=BluetoothAdapter.getDefaultAdapter();




        if(ba==null)
        {
            on.setEnabled(false);
            off.setEnabled(false);
            pared.setEnabled(false);
            search.setEnabled(false);
            tv.setText("Status : Not Supported");
            Toast.makeText(this,"Your Device does not support this servise ",Toast.LENGTH_LONG).show();
        }
        else
        {
            on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    on(v);

                }
            });

            off.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    off(v);
                }
            });
            pared.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showpareddevie(v);
                }
            });
            arrayadap=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            lv.setAdapter(arrayadap);
        }

    }
    public void on(View v)
    {
        if(!ba.isEnabled())
        {
            Intent turnonintent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnonintent,1);
            Toast.makeText(this,"Bluetooth is on",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Bluetooth is already on",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1)
        {
            if(ba.isEnabled())
            {
                tv.setText("Status : Enabled");
            }
            else
            {
                tv.setText("Status : Disabled");
            }
        }
    }


    public void off(View v)
    {
       ba.disable();
       tv.setText("status :Disconnected");
        Toast.makeText(this,"Bluetooth turned off",Toast.LENGTH_LONG).show();
    }

    public void showpareddevie(View v)
    {
        paireddevice=ba.getBondedDevices();
        for(BluetoothDevice bd:paireddevice)
            arrayadap.add(bd.getName()+"\n"+bd.getAddress());
        Toast.makeText(this,"Show Pared Device",Toast.LENGTH_LONG).show();


    }


}