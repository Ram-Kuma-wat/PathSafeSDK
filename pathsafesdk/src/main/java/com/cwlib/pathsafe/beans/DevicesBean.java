package com.cwlib.pathsafe.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DevicesBean implements Serializable {
    @SerializedName("success")
    String success;
    @SerializedName("returnmessage")
    String returnmessage;
    @SerializedName("info")
    ArrayList<DeviceRecordsBean.InfoBean> info;

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

    public ArrayList<DeviceRecordsBean.InfoBean> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<DeviceRecordsBean.InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean implements Serializable {
        @SerializedName("id")
        int id;
        @SerializedName("Success")
        String Success;
        @SerializedName("CustomerName")
        String CustomerName;
        @SerializedName("PhoneNo")
        String PhoneNo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSuccess() {
            return Success;
        }

        public void setSuccess(String success) {
            Success = success;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }

        public String getPhoneNo() {
            return PhoneNo;
        }

        public void setPhoneNo(String phoneNo) {
            PhoneNo = phoneNo;
        }
    }
}
