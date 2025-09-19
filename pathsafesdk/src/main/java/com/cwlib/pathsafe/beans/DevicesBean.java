package com.cwlib.pathsafe.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DevicesBean implements Serializable {
    @SerializedName("success")
    int success;
    @SerializedName("info")
    ArrayList<InfoBean> info;
    @SerializedName("returnmessage")
    String returnmessage;


    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public ArrayList<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<InfoBean> info) {
        this.info = info;
    }

    public String getReturnmessage() {
        return returnmessage;
    }

    public void setReturnmessage(String returnmessage) {
        this.returnmessage = returnmessage;
    }

    public static class InfoBean implements Serializable {
        @SerializedName("Success")
        String Success;
        @SerializedName("vehicleno")
        String vehicleno;
        @SerializedName("lockid")
        String lockid;
        @SerializedName("lockname")
        String lockname;
        @SerializedName("MacID")
        String MacID;
        @SerializedName("ttlockdata")
        String ttlockdata;

        public String getSuccess() {
            return Success;
        }

        public void setSuccess(String success) {
            Success = success;
        }

        public String getVehicleno() {
            return vehicleno;
        }

        public void setVehicleno(String vehicleno) {
            this.vehicleno = vehicleno;
        }

        public String getLockid() {
            return lockid;
        }

        public void setLockid(String lockid) {
            this.lockid = lockid;
        }

        public String getLockname() {
            return lockname;
        }

        public void setLockname(String lockname) {
            this.lockname = lockname;
        }

        public String getMacID() {
            return MacID;
        }

        public void setMacID(String macID) {
            MacID = macID;
        }

        public String getTtlockdata() {
            return ttlockdata;
        }

        public void setTtlockdata(String ttlockdata) {
            this.ttlockdata = ttlockdata;
        }
    }
}
