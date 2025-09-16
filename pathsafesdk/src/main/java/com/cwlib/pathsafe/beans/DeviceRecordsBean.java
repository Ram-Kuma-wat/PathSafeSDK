package com.cwlib.pathsafe.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DeviceRecordsBean implements Serializable {
    @SerializedName("success")
    String success;
    @SerializedName("returnmessage")
    String returnmessage;
    @SerializedName("info")
    ArrayList<InfoBean> info;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getReturnmessage() {
        return returnmessage;
    }

    public void setReturnmessage(String returnmessage) {
        this.returnmessage = returnmessage;
    }

    public ArrayList<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable {
        @SerializedName("VehicleNumber")
        String VehicleNumber;
        @SerializedName("Lockno")
        String Lockno;
        @SerializedName("lockid")
        String lockid;
        @SerializedName("Command")
        String Command;
        @SerializedName("Openedtime")
        String Openedtime;
        @SerializedName("Openedtype")
        String Openedtype;
        @SerializedName("Openedby")
        String Openedby;
        @SerializedName("Openeddate")
        String Openeddate;
        @SerializedName("CardNumber")
        String CardNumber;

        public String getVehicleNumber() {
            return VehicleNumber;
        }

        public void setVehicleNumber(String vehicleNumber) {
            VehicleNumber = vehicleNumber;
        }

        public String getLockno() {
            return Lockno;
        }

        public void setLockno(String lockno) {
            Lockno = lockno;
        }

        public String getLockid() {
            return lockid;
        }

        public void setLockid(String lockid) {
            this.lockid = lockid;
        }

        public String getCommand() {
            return Command;
        }

        public void setCommand(String command) {
            Command = command;
        }

        public String getOpenedtime() {
            return Openedtime;
        }

        public void setOpenedtime(String openedtime) {
            Openedtime = openedtime;
        }

        public String getOpenedtype() {
            return Openedtype;
        }

        public void setOpenedtype(String openedtype) {
            Openedtype = openedtype;
        }

        public String getOpenedby() {
            return Openedby;
        }

        public void setOpenedby(String openedby) {
            Openedby = openedby;
        }

        public String getOpeneddate() {
            return Openeddate;
        }

        public void setOpeneddate(String openeddate) {
            Openeddate = openeddate;
        }

        public String getCardNumber() {
            return CardNumber;
        }

        public void setCardNumber(String cardNumber) {
            CardNumber = cardNumber;
        }
    }

}
