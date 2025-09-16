package com.cwlib.pathsafe.config;

import android.os.Build;
import android.util.Log;

import com.cwlib.pathsafe.utils.AppUrls;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ETSMain {
    public ETSMain() {
    }


    public static String ETSEncryptUtils(String text, String val1, String val2) throws Exception {
        text = text + "&tkv=" + val1 + "&contvals=" + val2;
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        String key = AppUrls.PSX_API_KEY;
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(1, keySpec, ivSpec);
        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
        if (Build.VERSION.SDK_INT >= 26) {
            Log.i("Encoded", Base64.getEncoder().encodeToString(results));
            String encval2 = Base64.getEncoder().encodeToString(results);
            return encval2;
        } else {
            String kkk = android.util.Base64.encodeToString(results, 0);
            String encoded = kkk.replace("/n", "");
            Log.i("Encoded", encoded);
            return encoded;
        }
    }


    public static String ETSDecryptUtils(String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        String key = AppUrls.PSX_API_KEY;//"safe@GCKS@#^wji@";
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }

        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(2, keySpec, ivSpec);
        byte[] results = new byte[text.length()];

        try {
            if (Build.VERSION.SDK_INT >= 26) {
                Base64.getDecoder().decode(text);
                results = cipher.doFinal(Base64.getDecoder().decode(text));
            } else {
                results = cipher.doFinal(android.util.Base64.decode(text, 0));
            }
        } catch (Exception var10) {
            Log.i("Erron in Decryption", var10.toString());
        }

        //Log.i("Data", new String(results, "UTF-8"));
        return new String(results, "UTF-8");
    }
}
