package com.cwlib.pathsafe.listeners;

import com.cwlib.pathsafe.beans.DeviceRecordsBean;
import com.cwlib.pathsafe.beans.DevicesBean;

import java.util.ArrayList;

public interface OnPSXAuthListener {
    void onPSXAuth(String code,String message);
    void onPSXDevices(String code, String message, ArrayList<DevicesBean.InfoBean> mListDevices);
    void onPSXRecords(String code, String message, ArrayList<DeviceRecordsBean.InfoBean> mListLocks);
    void onPSXDeviceAction(String code, String message,String type);
}
