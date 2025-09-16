package com.cwlib.pathsafe.listeners;

import com.cwlib.pathsafe.beans.LockRecordsBean;
import com.cwlib.pathsafe.beans.AllLocksBean;

import java.util.ArrayList;

public interface OnSafeAuthListener {
    void onSafeAuth(String code,String message);
    void onSafeDevices(String code, String message, ArrayList<AllLocksBean.InfoBean> mListLocks);
    void onSafeRecords(String code, String message, ArrayList<LockRecordsBean.InfoBean> mListLocks);
    void onSafeLockAction(String code, String message,String type);
}
