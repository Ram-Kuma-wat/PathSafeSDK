package com.cwlib.pathsafe.config;

import android.content.Context;
import android.util.Log;

import com.cwlib.pathsafe.helpers.UserSessions;
import com.cwlib.pathsafe.utils.CommonMethods;


public class ETSConfigs {

    public String etsEncryption(Context context, String strParam) {
        UserSessions mUserSession = new UserSessions(context);
        String token = (mUserSession != null && CommonMethods.isValidString(mUserSession.getAccessToken(context))) ? mUserSession.getAccessToken(context) : "";
        String userid = (mUserSession != null && mUserSession.getUserInfo(context) != null) ? mUserSession.getUserInfo(context).getUsername() : "";
        try {
            Log.e("strParam",strParam);
            strParam = new AESHelper().etsEncryption(context,strParam,token,userid);
            return strParam;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String etsDecryption(String response, final Context context) {
        String responses = "";
        try {
            responses = new AESHelper().etsDecryption(response,context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responses;
    }

}
