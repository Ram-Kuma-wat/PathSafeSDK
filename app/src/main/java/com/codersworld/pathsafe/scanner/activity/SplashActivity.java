package com.codersworld.pathsafe.scanner.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cwlib.pathsafe.SafeLock;
import com.cwlib.pathsafe.beans.DevicesBean;
import com.cwlib.pathsafe.beans.DeviceRecordsBean;
import com.cwlib.pathsafe.listeners.OnPSXAuthListener;
import com.cwlib.pathsafe.utils.CommonMethods;
import com.codersworld.pathsafe.R;
import com.google.gson.Gson;

import java.util.ArrayList;


public class SplashActivity extends AppCompatActivity implements OnPSXAuthListener {
    SafeLock mSafeLock;
    EditText etId;
    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        etId = findViewById(R.id.etLockId);
        txtResult = findViewById(R.id.txtResult);
        mSafeLock = new SafeLock(SplashActivity.this, this);
      //  mSafeLock.authUser("uffizio", "uffizio123", "1.0", "Safe SDK demo");
        //mSafeLock.authUser("prashant67", "prashant67", "1.0", "Safe SDK demo");
        //mSafeLock.authUser("prashant67", "prashant67", "1.0", "Safe SDK demo");
        mSafeLock.authUser("fltlock@7896", "789654123", "1.0", "PathSafe SDK demo");
    }
//        mSafeLock.getLockRecords("9605866");
    public void onOpen(View v) {
        if (CommonMethods.isValidString(etId.getText().toString())) {
            mSafeLock.performDeviceAction(etId.getText().toString(), 1);
        } else {
            Toast.makeText(this, "Enter device id", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClose(View v) {
        if (CommonMethods.isValidString(etId.getText().toString())) {
            mSafeLock.performDeviceAction(etId.getText().toString(), 0);
        } else {
            Toast.makeText(this, "Enter device id", Toast.LENGTH_SHORT).show();
        }
    }
    public void getRecords(View v) {
        if (CommonMethods.isValidString(etId.getText().toString())) {
            mSafeLock.getLockRecords(etId.getText().toString());
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

           // mSafeLock.actionManualLock("","",1);
        }
    }

    @Override
    public void onPSXDevices(String errorCode, String message, ArrayList<DevicesBean.InfoBean> mListLocks) {
        Log.e("onPSXDevices", errorCode + "\n" + message);
        if (errorCode.equalsIgnoreCase("106")) {
            if (CommonMethods.isValidArrayList(mListLocks)) {
                Log.e("mListLocks", new Gson().toJson(mListLocks));

/*
                for (int a = 0; a < mListLocks.size(); a++) {
                    Log.e("locakname", mListLocks.get(a).getVehicleNumber());
                    if (mListLocks.get(a).getVehicleNumber().equalsIgnoreCase("FRANCHISE LOCK")) {
                        mSafeLock.openLock(System.currentTimeMillis(), mListLocks.get(a).getDeviceCode());
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
    public void onPSXDeviceAction(String code, String message, String type) {
        Toast.makeText(this, " "+message, Toast.LENGTH_SHORT).show();

        Log.e("action_lock", code + "\n" + message + "\n" + type);
    }
}
