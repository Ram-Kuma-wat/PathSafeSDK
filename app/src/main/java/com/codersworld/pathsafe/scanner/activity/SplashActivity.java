package com.codersworld.pathsafe.scanner.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cwlib.pathsafe.PathSafe;
import com.cwlib.pathsafe.beans.DevicesBean;
import com.cwlib.pathsafe.beans.DeviceRecordsBean;
import com.cwlib.pathsafe.listeners.OnPSXAuthListener;
import com.cwlib.pathsafe.utils.CommonMethods;
import com.codersworld.pathsafe.R;
import com.google.gson.Gson;

import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity implements OnPSXAuthListener {
    PathSafe mPathSafe;
    EditText etId;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        etId = findViewById(R.id.etLockId);
        txtResult = findViewById(R.id.txtResult);
        mPathSafe = new PathSafe(SplashActivity.this, this);
        //mPathSafe.authUser("vrllock@gmail.com", "vrltms@9569", "1.0", "TMS Pathsecurex");
        mPathSafe.authUser("fltlock@7896", "789654123", "1.0", "PathSafe SDK demo");
    }
//        mPathSafe.getLockRecords("9605866");
    public void onOpen(View v) {
        if (CommonMethods.isValidString(etId.getText().toString())) {
            mPathSafe.performDeviceAction(etId.getText().toString(), 1);
        } else {
            Toast.makeText(this, "Enter device id", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClose(View v) {
        if (CommonMethods.isValidString(etId.getText().toString())) {
            mPathSafe.performDeviceAction(etId.getText().toString(), 0);
        } else {
            Toast.makeText(this, "Enter device id", Toast.LENGTH_SHORT).show();
        }
    }
    public void getRecords(View v) {
        if (CommonMethods.isValidString(etId.getText().toString())) {
            mPathSafe.getLockRecords(etId.getText().toString());
        } else {
            Toast.makeText(this, "Enter device id", Toast.LENGTH_SHORT).show();
        }
    }
    public void getBatteryStatus(View v) {
        if (CommonMethods.isValidString(etId.getText().toString())) {
            mPathSafe.getLockBatteryLevel(etId.getText().toString());
        } else {
            Toast.makeText(this, "Enter device id", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPSXAuth(String errorCode, String message) {
        Log.e("onPSXAuth", errorCode + "\n" + message);
        if (errorCode.equalsIgnoreCase("106")) {
            Toast.makeText(this, "Authenticated successfully.", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            mPathSafe.getDevices();

           // mPathSafe.actionManualLock("","",1);
        }
    }

    @Override
    public void onPSXDevices(String errorCode, String message, ArrayList<DevicesBean.InfoBean> mListDevices) {
        Log.e("onPSXDevices", errorCode + "\n" + message);
        if (errorCode.equalsIgnoreCase("106")) {
            if (CommonMethods.isValidArrayList(mListDevices)) {
                Log.e("mListLocks", new Gson().toJson(mListDevices));

/*
                for (int a = 0; a < mListLocks.size(); a++) {
                    Log.e("locakname", mListLocks.get(a).getVehicleNumber());
                    if (mListLocks.get(a).getVehicleNumber().equalsIgnoreCase("FRANCHISE LOCK")) {
                        mPathSafe.openLock(System.currentTimeMillis(), mListLocks.get(a).getDeviceCode());
                    }
                }
*/
            }
        }
    }

    @Override
    public void onPSXRecords(String errorCode, String message, ArrayList<DeviceRecordsBean.InfoBean> mListRecords) {
        Log.e("onPSXRecords", errorCode + "\n" + message);
        if (errorCode.equalsIgnoreCase("106")) {
            if (CommonMethods.isValidArrayList(mListRecords)) {
                Log.e("mListRecords", new Gson().toJson(mListRecords));
                txtResult.setText(new Gson().toJson(mListRecords));
            }

        }else{
            txtResult.setText(message);

        }
    }

    @Override
    public void onPSXDeviceBatteryCheck(String code, String message, int batteryPer) {
        Log.e("battery_per", code + "\n" + message + "\n" +batteryPer);
        txtResult.setText("code : "+code+"\nmessage : "+message+"\nBattery Status : "+batteryPer+"%");


    }

    @Override
    public void onPSXDeviceAction(String code, String message, String type,int batteryPer) {
        Toast.makeText(this, " "+message, Toast.LENGTH_SHORT).show();
        txtResult.setText("code : "+code+"\nMessage : "+message+"\nBattery Status : "+batteryPer+"%");

        Log.e("action_lock", code + "\n" + message + "\n" + type);
    }
}
