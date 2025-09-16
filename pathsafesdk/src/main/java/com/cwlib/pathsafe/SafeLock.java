package com.cwlib.pathsafe;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.cwlib.pathsafe.beans.DeviceInfoBean;
import com.cwlib.pathsafe.beans.DeviceRecordsBean;
import com.cwlib.pathsafe.beans.DeviceDetailBean;
import com.cwlib.pathsafe.beans.SensitiveInfo;
import com.cwlib.pathsafe.config.ETSConfigs;
import com.cwlib.pathsafe.helpers.JKHelper;
import com.cwlib.pathsafe.helpers.UserSessions;
import com.cwlib.pathsafe.listeners.OnAuthListener;
import com.cwlib.pathsafe.listeners.OnPSXAuthListener;
import com.cwlib.pathsafe.beans.AccountInfo;
import com.cwlib.pathsafe.beans.DevicesBean;
import com.cwlib.pathsafe.beans.LoginBean;
import com.cwlib.pathsafe.rest.ApiCall;
import com.cwlib.pathsafe.rest.ApiService;
import com.cwlib.pathsafe.rest.OnResponse;
import com.cwlib.pathsafe.rest.UniverSelObjct;
import com.cwlib.pathsafe.utils.AppUrls;
import com.cwlib.pathsafe.utils.CommonMethods;
import com.cwlib.pathsafe.utils.PermissionModule;
import com.cwlib.pathsafe.utils.SFProgress;
import com.google.gson.Gson;
import com.ttlock.bl.sdk.api.TTLockClient;
import com.ttlock.bl.sdk.callback.ControlLockCallback;
import com.ttlock.bl.sdk.constant.ControlAction;
import com.ttlock.bl.sdk.entity.ControlLockResult;
import com.ttlock.bl.sdk.entity.LockError;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public class SafeLock implements OnResponse<UniverSelObjct>, OnAuthListener {
    static Activity mActivity;
    static long iniDateTime;
    static String deviceCode;
    static String strLat = "0.0";
    static String strLong = "0.0";
    static ApiCall mApiCall = null;
    OnPSXAuthListener mAuthListener = null;
    ArrayList<SensitiveInfo> mInfo = new ArrayList<>();


    public SafeLock(Activity activity, OnPSXAuthListener listener) {
        this.mActivity = activity;
        this.mAuthListener = listener;
        getInstance(mActivity);
        initApiCall();
    }

    public static void initApiCall() {
        if (mApiCall == null) {
            mApiCall = new ApiCall(mActivity);
        }
    }

    private static void getInstance(Activity activity) {
        mActivity = activity;
    }

    public void checkPermission() {
        //statusCheck();
        try {
            final BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            bAdapter.enable();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void statusCheck() {
        final LocationManager manager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    public static void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton(mActivity.getResources().getString(R.string.lbl_yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        mActivity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    String strUsername = "";

    public void authUser(String strUsername, String strPassword, String strAppVersion, String strAppName) {
        this.strUsername = strUsername;
        initApiCall();
        new UserSessions().saveAccessToken(mActivity, "");

        String params = AppUrls.PSX_LOGIN_API + "&userid=" + strUsername + "&pwd=" + strPassword;
        ETSConfigs mETSConfigs = new ETSConfigs();
        String encParam = mETSConfigs.etsEncryption(mActivity, params);

        if (CommonMethods.isNetworkAvailable(mActivity)) {
            mApiCall.userLogin(this, encParam);
        } else {
            onAuthResult("100", mActivity.getString(R.string.error_internet));
        }
    }

    public void saveDeviceRecord(SensitiveInfo mMap, String msg) {
        initApiCall();
        String strDeviceId = (CommonMethods.isValidString(mMap.getLOCK_ID())) ? mMap.getLOCK_ID() : (CommonMethods.isValidString(mMap.getLOCK_CODE()) ? mMap.getLOCK_CODE() : "");
        LoginBean.InfoBean mBeanUser = UserSessions.getUserInfo(mActivity);
        String params = AppUrls.PSX_SAVE_LOCK_RECORD + "&type=1&vno=&lockno="+strDeviceId+"&commandtype=" +msg+"&openedtype=Direct Command&lat=&lng=&timestamp=&fdate=&tdate=&batteryper=&address=&lockopenedby="+mBeanUser.getUserDetailId();
        ETSConfigs mETSConfigs = new ETSConfigs();
        String encParam = mETSConfigs.etsEncryption(mActivity, params);
        mApiCall.callApi(this, false,encParam,AppUrls.PSX_SAVE_LOCK_RECORD+"-1");
    }

    public void getLockRecords(String device_id) {
        actionType = 3;
        getDeviceInfo(device_id);
    }

    private void getLockRecordsData(DeviceInfoBean.InfoBean mMap) {
        initApiCall();
        String startDate = CommonMethods.getCalculatedDate("MM/dd/yyyy", -7);
        String endDate = CommonMethods.getCurrentFormatedDate("MM/dd/yyyy");

        LoginBean.InfoBean mBeanUser = UserSessions.getUserInfo(mActivity);
        String params = AppUrls.PSX_SAVE_LOCK_RECORD + "&type=2&vno="+mMap.getVehicleno()+"&lockno="+mMap.getLockid()+"&commandtype=&openedtype=&lat=&lng=&timestamp=&fdate="+startDate+"&tdate="+endDate+"&batteryper=&address=&lockopenedby="+mBeanUser.getUserDetailId();
        ETSConfigs mETSConfigs = new ETSConfigs();
        String encParam = mETSConfigs.etsEncryption(mActivity, params);
        mApiCall.callApi(this, false,encParam,AppUrls.PSX_SAVE_LOCK_RECORD+"-2");
    }


    @Override
    public void onSuccess(UniverSelObjct response) {
        try {
            switch (response.getMethodname()) {
                case AppUrls.PSX_GET_DEVICE_INFO:
                    try {
                        SFProgress.hideProgressDialog(mActivity);
                    } catch (Exception e) {
                    }
                    try {
                        DeviceInfoBean mDeviceInfoBean = (DeviceInfoBean) response.getResponse();
                        if (mDeviceInfoBean.getSuccess() == 1) {
                            mInfo = new ArrayList<>();
                            for (int a = 0; a < mDeviceInfoBean.getInfo().size(); a++) {
                                if (CommonMethods.isValidString(mDeviceInfoBean.getInfo().get(a).getTtlockdata()) && CommonMethods.isValidString(mDeviceInfoBean.getInfo().get(a).getMacID())) {
                                    SensitiveInfo mbn = new SensitiveInfo();
                                    mbn.setLockData(mDeviceInfoBean.getInfo().get(a).getTtlockdata());
                                    mbn.setMACID(mDeviceInfoBean.getInfo().get(a).getMacID());
                                    mbn.setLOCK_CODE(mDeviceInfoBean.getInfo().get(a).getVehicleno());
                                    mbn.setLOCK_ID(mDeviceInfoBean.getInfo().get(a).getLockid());
                                    mbn.setBtlockid(mDeviceInfoBean.getInfo().get(a).getLockid());
                                    mbn.setBtlockidval(mDeviceInfoBean.getInfo().get(a).getLockid());
                                    mbn.setGPSDeviceCode(mDeviceInfoBean.getInfo().get(a).getLockid());
                                    mbn.setGPSDeviceId(mDeviceInfoBean.getInfo().get(a).getLockid());
                                    mbn.setLockname(mDeviceInfoBean.getInfo().get(a).getLockname());
                                    mInfo.add(mbn);
                                }
                            }
                            UserSessions.saveMap(mActivity, (mInfo.size() > 0) ? mInfo : new ArrayList<>());
                            if (actionType == 1) {
                                openLock(System.currentTimeMillis(), deviceCode);
                            } else if (actionType == 0) {
                                closeLock(deviceCode);
                            } else if (actionType == 3) {
                                getLockRecordsData(mDeviceInfoBean.getInfo().get(0));
                            }
                        } else {
                            if (actionType == 0) {
                                onLockAction("101", mActivity.getString(R.string.something_wrong), "close lock");
                            } else {
                                onLockAction("102", mActivity.getString(R.string.something_wrong), "open lock");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (actionType == 0) {
                            onLockAction("101", mActivity.getString(R.string.something_wrong), "close lock");
                        } else {
                            onLockAction("102", mActivity.getString(R.string.something_wrong), "open lock");
                        }
                    }
                    break;
                case AppUrls.PSX_LOGIN_API:
                    try {
                        LoginBean mLoginBean = (LoginBean) response.getResponse();
                        if (mLoginBean.getSuccess().equalsIgnoreCase("1") && CommonMethods.isValidArrayList(mLoginBean.getReturnMessage())) {
                            UserSessions.saveAccessToken(mActivity, (CommonMethods.isValidString(mLoginBean.getReturnMessage().get(0).getUsername())) ? mLoginBean.getReturnMessage().get(0).getUsername() : "");
                            if (CommonMethods.isValidString(mLoginBean.getReturnMessage().get(0).getUserDetailId()) && !mLoginBean.getReturnMessage().get(0).getUserDetailId().equalsIgnoreCase("0")) {
                                UserSessions.saveUserInfo(mActivity, mLoginBean.getReturnMessage().get(0));
                                new JKHelper().makeTTAuthentication(mActivity, 4, this);
                                //moveToNext(mLoginBean.getLoginvalidation().get(0));
                            } /*else if (CommonMethods.isValidString(mLoginBean.getReturnMessage().get(0).getReturnmessage())) {
                                onAuthResult("105", mLoginBean.getLoginvalidation().get(0).getReturnmessage());
                            } */else {
                                onAuthResult("104", mActivity.getResources().getString(R.string.error_no_role));
                            }
                        } /*else if (CommonMethods.isValidString(mLoginBean.getMessage())) {
                            onAuthResult("103", mLoginBean.getMessage());
                        } else if (CommonMethods.isValidArrayList(mLoginBean.getReturnMessage()) && CommonMethods.isValidString(mLoginBean.getReturnMessage().get(0).getReturnmessage())) {
                            onAuthResult("102", mLoginBean.getReturnMessage().get(0).getReturnmessage());
                        } */else {
                            onAuthResult("101", mActivity.getString(R.string.something_wrong));
                        }
                    } catch (Exception e) {
                        onAuthResult("101", mActivity.getString(R.string.something_wrong));
                    }
                    break;
                case AppUrls.PSX_SAVE_LOCK_RECORD+"-2":
                    try {
                        JSONObject jsonObjct = new JSONObject(response.getResponse().toString());
                        if (jsonObjct.getInt("success") == 1) {
                            DeviceRecordsBean gateOpenBean = new Gson().fromJson(response.getResponse().toString(), DeviceRecordsBean.class);
                            if (gateOpenBean != null && CommonMethods.isValidArrayList(gateOpenBean.getInfo())) {
                                onPSXRecords("106", "Success", gateOpenBean.getInfo());
                            } else {
                                onPSXRecords("100", mActivity.getString(R.string.something_wrong), null);
                            }
                        } else {
                            onPSXRecords("100", "No records found for this device.", null);
                        }
                    } catch (Exception ex1) {
                        onPSXRecords("100", mActivity.getString(R.string.something_wrong), null);
                        ex1.printStackTrace();
                    }
                    break;
                case AppUrls.PSX_API_TTLOCK_GET_LOCKDATA:
                    try {
                        DeviceDetailBean mDeviceDetailBean = (DeviceDetailBean) response.getResponse();
                        if (mDeviceDetailBean != null) {
                            ArrayList<SensitiveInfo> mMap2 = new ArrayList<>();
                            mMap2 = UserSessions.getMap(mActivity);
                            if (CommonMethods.isValidArrayList(mMap2)) {
                                for (int a = 0; a < mMap2.size(); a++) {
                                    if (mMap2.get(a).getBtlockidval().equalsIgnoreCase(deviceCode)) {
                                        SensitiveInfo mMap3 = mMap2.get(a);
                                        mMap3.setLockData(mDeviceDetailBean.getLockData());
                                        mMap3.setMACID(mDeviceDetailBean.getLockMac());
                                        mMap3.setLOCK_CODE(mMap3.getLOCK_CODE());
                                        mMap3.setLOCK_ID(mMap3.getLOCK_ID());
                                        mMap3.setBtlockid(mMap3.getBtlockid());
                                        mMap3.setBtlockidval(mMap3.getBtlockidval());
                                        mMap3.setGPSDeviceCode(mMap3.getGPSDeviceCode());
                                        mMap3.setGPSDeviceId(mMap3.getGPSDeviceId());
                                        mMap2.set(a, mMap3);
                                        UserSessions.saveMap(mActivity, mMap2);
                                        if (actionType == 1) {
                                            openLock(System.currentTimeMillis(), deviceCode);
                                        } else if (actionType == 0) {
                                            closeLock(deviceCode);
                                        }
                                        new JKHelper().updateLockData(mActivity, mDeviceDetailBean.getLockData(), mDeviceDetailBean.getLockMac(), mDeviceDetailBean.getLockId());
                                    }
                                }
                            }
                        }

                    } catch (Exception ex1) {
                        ex1.printStackTrace();
                        if (actionType == 0) {
                            onLockAction("101", mActivity.getString(R.string.something_wrong), "close lock");
                        } else {
                            onLockAction("102", mActivity.getString(R.string.something_wrong), "open lock");
                        }

                    }
                    break;
            }
        } catch (Exception e) {
            onAuthResult("100", mActivity.getString(R.string.error_internet));
        }
    }


    private void onPSXDevices(String code, String msg, ArrayList<DevicesBean.InfoBean> mListLocks) {
        if (mAuthListener != null) {
            mAuthListener.onPSXDevices(code, msg, mListLocks);
        }
    }

    private void onPSXRecords(String code, String msg, ArrayList<DeviceRecordsBean.InfoBean> mListRecords) {
        if (mAuthListener != null) {
            mAuthListener.onPSXRecords(code, msg, mListRecords);
        }
    }

    private void onAuthResult(String code, String msg) {
        if (mAuthListener != null) {
            mAuthListener.onPSXAuth(code, msg);
        }
    }

    private void onLockAction(String code, String msg, String type) {
        if (mAuthListener != null) {
            mAuthListener.onPSXDeviceAction(code, msg, type);
        }
    }


    @Override
    public void onError(String type, String error) {
        try {
            switch (type) {
                case AppUrls.PSX_GET_DEVICE_INFO:
                    try {
                        SFProgress.hideProgressDialog(mActivity);
                    } catch (Exception e) {
                    }
                    actionLock();
                    break;
                case AppUrls.PSX_LOGIN_API:
                    if (mAuthListener != null) {
                        onAuthResult("100", error);
                    }
                    break;
                case AppUrls.PSX_SAVE_LOCK_RECORD+"-2":
                    if (mAuthListener != null) {
                        onPSXRecords("100", error, null);
                    }
                    break;
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onAuth(AccountInfo mInfo) {
        onAuthResult("106", mActivity.getString(R.string.login_success));
    }


    public void performDeviceAction(String lockId, int type) {//1 for lock open 0 for lock close
        deviceCode = lockId;
        actionType = type;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
           /* try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                mActivity.startActivityForResult(intent,STORAGE_PERMISSION_CODE);
            }catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                mActivity.startActivityForResult(intent,STORAGE_PERMISSION_CODE);
            }*/
        } else {
            //Below android 11
            ActivityCompat.requestPermissions(
                    mActivity,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }
        getDeviceInfo(lockId);
    }

    int actionType = -1;

    public void closeLock(String deviceCode/*String lockData, String macID*/) {
        checkPermission();
        final LocationManager manager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            actionType = 0;
            this.deviceCode = deviceCode;
            PermissionModule m = new PermissionModule(mActivity);
            if (m.checkBTPermissions()) {
                actionLock();
            } else {
                m.requestForPermissions();
            }
        }
    }


    private static final int STORAGE_PERMISSION_CODE = 23;

    protected void actionLock() {
        iniDateTime = System.currentTimeMillis() + 1800000;
        long unlockdate = System.currentTimeMillis();
        SensitiveInfo mMap = validateDevice(deviceCode);
        if (mMap == null) {
            onLockAction("100", "Invalid device info", (actionType == 0) ? "close lock" : "open lock");
        } else if (iniDateTime < unlockdate) {
            onLockAction("100", "Please Refresh Page", (actionType == 0) ? "close lock" : "open lock");
            return;
        }

        String lockData = (CommonMethods.isValidString(mMap.getLockData())) ? mMap.getLockData() : "";
        String macID = (CommonMethods.isValidString(mMap.getMACID())) ? mMap.getMACID() : "";
        String btlockid = (CommonMethods.isValidString(mMap.getBtlockid())) ? mMap.getBtlockid() : "";
        if (!CommonMethods.isValidString(lockData) || !CommonMethods.isValidString(macID)) {
            getLockData(btlockid);
        } else {
            SFProgress.showProgressDialog(mActivity, true);
            TTLockClient.getDefault().controlLock((actionType == 0) ? ControlAction.LOCK : ControlAction.UNLOCK, lockData, macID, new ControlLockCallback() {
                @Override
                public void onControlLockSuccess(ControlLockResult controlLockResult) {
                    SFProgress.hideProgressDialog(mActivity);
                    try {
                        if (actionType == 0) {
                            onLockAction("106", "Device is locked successfully.", "close lock");
                        } else {
                            onLockAction("106", "Lock opened successfully.", "open lock");
                        }
                        saveDeviceRecord(mMap, (actionType == 0) ? "locked" : "Unlocked");
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (actionType == 0) {
                            onLockAction("101", mActivity.getString(R.string.something_wrong), "close lock");
                        } else {
                            onLockAction("102", mActivity.getString(R.string.something_wrong), "open lock");
                        }
                        saveDeviceRecord(mMap, (actionType == 0) ? "Failed to close via APP" : "Failed to open via APP");
                    }
                    //unlockRecordUpload("Locked via App");
                }

                @Override
                public void onFail(LockError error) {
                    SFProgress.hideProgressDialog(mActivity);
                    try {
                        Log.e("Lockerror", error.getErrorMsg() + "\n" + error.getDescription());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (actionType == 0) {
                        onLockAction("100", "Failed to lock the device.", "close lock");
                    } else {
                        onLockAction("102", "failed to open the lock.", "open lock");
                    }
                    saveDeviceRecord(mMap, (actionType == 0) ? "Failed to close via APP" : "Failed to open via APP");
                }
            });
        }
    }

    int actionCounter = 0;

    public void actionManualLock(String lockData, String macID, int mActionType, String mDeviceCode) {
        actionType = mActionType;
        deviceCode = mDeviceCode;
        checkPermission();
        final LocationManager manager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            PermissionModule m = new PermissionModule(mActivity);
            if (m.checkBTPermissions()) {
                iniDateTime = System.currentTimeMillis() + 1800000;
                long unlockdate = System.currentTimeMillis();
                if (iniDateTime < unlockdate) {
                    onLockAction("100", "Please Refresh Page", (mActionType == 0) ? "close lock" : "open lock");
                    return;
                }

                if (CommonMethods.isValidString(lockData) && CommonMethods.isValidString(macID)) {
                    SFProgress.showProgressDialog(mActivity, true);
                    TTLockClient.getDefault().controlLock((mActionType == 0) ? ControlAction.LOCK : ControlAction.UNLOCK, lockData, macID, new ControlLockCallback() {
                        @Override
                        public void onControlLockSuccess(ControlLockResult controlLockResult) {
                            SFProgress.hideProgressDialog(mActivity);
                            try {
                                if (mActionType == 0) {
                                    onLockAction("106", "Device is locked successfully.", "close lock");
                                } else {
                                    onLockAction("106", "Lock opened successfully.", "open lock");
                                }
                                //saveDeviceRecord(mMap, (mActionType == 0) ? "Closed via APP" : "Opened via APP");
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (mActionType == 0) {
                                    onLockAction("101", mActivity.getString(R.string.something_wrong), "close lock");
                                } else {
                                    onLockAction("102", mActivity.getString(R.string.something_wrong), "open lock");
                                }
                                //saveDeviceRecord(mMap, (mActionType == 0) ? "Failed to close via APP" : "Failed to open via APP");
                            }
                            //unlockRecordUpload("Locked via App");
                        }

                        @Override
                        public void onFail(LockError error) {
                            SFProgress.hideProgressDialog(mActivity);
                            if (actionCounter == 0) {
                                actionCounter++;
                                getDeviceInfo(mDeviceCode);
                            } else {
                                try {
                                    Log.e("Lockerror", error.getErrorMsg() + "\n" + error.getDescription());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (mActionType == 0) {
                                    onLockAction("100", "Failed to lock the device.", "close lock");
                                } else {
                                    onLockAction("102", "failed to open the lock.", "open lock");
                                }
                            }
                            // saveDeviceRecord(mMap, (mActionType == 0) ? "Failed to close via APP" : "Failed to open via APP");
                        }
                    });
                }

            } else {
                m.requestForPermissions();
            }
        }
    }

    public void openLock(long unlockdate, String deviceCode) {
        checkPermission();
        final LocationManager manager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            this.deviceCode = deviceCode;
            actionType = 1;
            PermissionModule m = new PermissionModule(mActivity);
            if (m.checkBTPermissions()) {
                actionLock();
            } else {
                m.requestForPermissions();
            }
        }
    }

    private void getLockData(String lock_code) {
        initApiCall();
        LoginBean.InfoBean mBeanUser = UserSessions.getUserInfo(mActivity);
        AccountInfo mBeanAccountInfo = UserSessions.getTTAccountInfo(mActivity);
        String ClientID = ApiService.CLIENT_ID;
        String client_secret = ApiService.CLIENT_SECRET;
        String accessToken = "";
        if (mBeanAccountInfo != null) {
            accessToken = (CommonMethods.isValidString(mBeanAccountInfo.getAccess_token())) ? mBeanAccountInfo.getAccess_token() : "";
        }


        HashMap param = new HashMap<String, String>();
        param.put("client_secret", client_secret);
        param.put("clientId", ClientID);
        param.put("accessToken", accessToken);
        param.put("lockId", lock_code);
        param.put("date", System.currentTimeMillis() + "");
        //mApiCall.getUserKeyList(this, param);
        mApiCall.getLockData(this, ClientID, accessToken, lock_code, System.currentTimeMillis() + "");
    }

    private void authenticateTTLock() {
        new JKHelper().makeTTAuthentication(mActivity, 4, (OnAuthListener) mActivity);
    }

    private SensitiveInfo validateDevice(String deviceCode) {
        ArrayList<SensitiveInfo> mMap2 = new ArrayList<>();
        mMap2 = UserSessions.getMap(mActivity);
        SensitiveInfo mBn = null;
        for (int a = 0; a < mMap2.size(); a++) {
            if (mMap2.get(a).getBtlockidval().equalsIgnoreCase(deviceCode)) {
                mBn = mMap2.get(a);
            }
        }
        return mBn;
    }

    public void getDeviceInfo(String... strParams) {
        initApiCall();
        try {
            SFProgress.showProgressDialog(mActivity, true);
            LoginBean.InfoBean mBeanUser = UserSessions.getUserInfo(mActivity);
            if (mBeanUser !=null) {
                String params = AppUrls.PSX_GET_DEVICE_INFO + "&type=1&userdetailid=" + mBeanUser.getUserDetailId() + "&lockid=" + strParams[0];
                ETSConfigs mETSConfigs = new ETSConfigs();
                String encParam = mETSConfigs.etsEncryption(mActivity, params);
                mApiCall.callApi(this, true,encParam,AppUrls.PSX_GET_DEVICE_INFO);
            }else{
                if (actionType == 0) {
                    onLockAction("100", "Failed to lock the device.", "close lock");
                } else {
                    onLockAction("102", "failed to open the lock.", "open lock");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("mBeanUser","Invalid login data");
            if (actionType == 0) {
                onLockAction("100", "Failed to lock the device.", "close lock");
            } else {
                onLockAction("102", "failed to open the lock.", "open lock");
            }
        }
    }


}
