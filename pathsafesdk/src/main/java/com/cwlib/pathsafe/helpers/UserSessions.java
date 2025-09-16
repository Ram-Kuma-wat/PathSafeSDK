package com.cwlib.pathsafe.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cwlib.pathsafe.beans.AccountInfo;
import com.cwlib.pathsafe.beans.KeyListObj;
import com.cwlib.pathsafe.beans.LoginBean;
import com.cwlib.pathsafe.beans.SensitiveInfo;
import com.cwlib.pathsafe.utils.CommonMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class UserSessions {
    public static SharedPreferences mPrefs = null;
    public static SharedPreferences.Editor prefsEditor = null;
    public static Context mContext;

    public UserSessions() {

    }

    public UserSessions(Context ctx) {
        this.mContext = ctx;
        initSession();
    }

    public static void initSession() {
        if (mPrefs == null) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        }
        if (prefsEditor == null) {
            prefsEditor = mPrefs.edit();
        }
    }

    public static void saveAccessToken(Context ctx, String strToken) {
        mContext = ctx;
        initSession();
        prefsEditor.putString("SB_ACCESS_TOKEN", strToken);
        prefsEditor.commit();
    }

    public static String getAccessToken(Context ctx) {
        mContext = ctx;
        initSession();
        return mPrefs.getString("SB_ACCESS_TOKEN", "");
    }


    public static void saveUserInfo(Context ctx, LoginBean.InfoBean mUserBean) {
        mContext = ctx;
        initSession();
        prefsEditor.putString("SB_USER_INFO", (mUserBean != null) ? new Gson().toJson(mUserBean) : "");
        prefsEditor.commit();
    }

    public static void clearUserInfo(Context ctx) {
        mContext = ctx;
        initSession();
        prefsEditor.putString("SB_USER_INFO", "");
        prefsEditor.commit();
    }

    public static LoginBean.InfoBean getUserInfo(Context ctx) {
        mContext = ctx;
        initSession();
        if (mPrefs.getString("SB_USER_INFO", "").isEmpty()) {
            return null;
        } else {
            return new Gson().fromJson(mPrefs.getString("SB_USER_INFO", ""), LoginBean.InfoBean.class);
        }
    }


    public static void saveKeyLists(Context ctx, ArrayList<KeyListObj.KeyObj> mList) {
        mContext = ctx;
        initSession();
        String str = new Gson().toJson(mList);
        prefsEditor.putString("SB_TT_KEY_LIST", str);
        prefsEditor.commit();
    }

    public static ArrayList<KeyListObj.KeyObj> getKeyLists(Context ctx) {
        mContext = ctx;
        initSession();
        if (mPrefs.getString("SB_TT_KEY_LIST", "").isEmpty()) {
            return null;
        } else {
            return new Gson().fromJson(mPrefs.getString("SB_TT_KEY_LIST", ""), new TypeToken<List<KeyListObj.KeyObj>>() {
            }.getType());
            //return new Gson().fromJson(mPrefs.getString(Tags.SB_USER_OTHER_INFO, ""), UserOtherInfoBean.InfoBean.class);
        }
    }

    public static void saveTTAccountInfo(Context context, String str) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("SB_TT_ACCOUNT_INFO", CommonMethods.isValidString(str) ? str : "");
        editor.commit();
    }

    public static AccountInfo getTTAccountInfo(Context context) {
        mContext = context;
        initSession();
        if (mPrefs.getString("SB_TT_ACCOUNT_INFO", "").isEmpty()) {
            return null;
        } else {
            return new Gson().fromJson(mPrefs.getString("SB_TT_ACCOUNT_INFO", ""), AccountInfo.class);
        }
    }

    public static void saveUpdate(Context context, int str) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putInt("isUpdate", str);
        editor.commit();
    }

    public static int getUpdate(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getInt("isUpdate", 0);
    }

    public static void saveAction(Context context, String strKey, String strValue) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(strKey, strValue);
        editor.commit();
    }

    public static String getAction(Context context, String strKey) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return mPrefs.getString(strKey, "");
    }
    public static void saveMap(Context context, ArrayList<SensitiveInfo> mMap) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mPrefs.edit();
        String str =  new Gson().toJson(mMap);

        editor.putString("map", str);
        editor.commit();
    }

    public static ArrayList<SensitiveInfo> getMap(Context context) {
        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String str = mPrefs.getString("map", "");
        ArrayList<SensitiveInfo> mapObj = new ArrayList<>();
        if (!str.isEmpty()){
         mapObj = new Gson().fromJson(str, new TypeToken<ArrayList<SensitiveInfo>>() {}.getType());
        }
        return mapObj ;
    }

}
