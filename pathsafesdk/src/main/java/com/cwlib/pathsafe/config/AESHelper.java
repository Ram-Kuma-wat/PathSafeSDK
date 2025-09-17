package com.cwlib.pathsafe.config;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AESHelper {

    public String etsEncryption(Context context, String strParam,String token,String userid) {
         try {
            strParam = ETSMain.ETSEncryptUtils(strParam, token, userid);
            return strParam;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String etsDecryption(String response, final Context context) {
        String responses = "";
        try {
            responses = ETSMain.ETSDecryptUtils(response);
           //Log.e("responsesDec",responses);
            JSONObject jsonObject = new JSONObject(responses);
            int success = jsonObject.getInt("success");
            if (success == 9999) {
                return "";
            } else {
                return responses;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responses;
    }

}
