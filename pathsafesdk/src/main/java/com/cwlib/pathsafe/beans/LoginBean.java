package com.cwlib.pathsafe.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginBean implements Serializable {
    @SerializedName("success")
    private String success;

    @SerializedName("returnmessage")
    private ArrayList<InfoBean> returnMessage;

    // Getters and setters
    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ArrayList<InfoBean> getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(ArrayList<InfoBean> returnMessage) {
        this.returnMessage = returnMessage;
    }



    public static class InfoBean implements Serializable{
        @SerializedName("Success")
        private String success;

        @SerializedName("userdetailid")
        private String userDetailId;

        @SerializedName("username")
        private String username;

        @SerializedName("firstname")
        private String firstName;

        @SerializedName("fullname")
        private String fullName;

        @SerializedName("usertype")
        private String userType;

        @SerializedName("mainuser")
        private String mainUser;

        @SerializedName("subuserdetailid")
        private String subUserDetailId;

        @SerializedName("CompanyID")
        private String companyId;

        @SerializedName("RoleID")
        private String roleId;

        @SerializedName("isemployeetpt")
        private String isEmployeeTpt;

        @SerializedName("createdbypimsid")
        private String createdByPimsId;

        @SerializedName("IsEmployeeManager")
        private String isEmployeeManager;

        @SerializedName("password")
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        // Getters and setters
        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getUserDetailId() {
            return userDetailId;
        }

        public void setUserDetailId(String userDetailId) {
            this.userDetailId = userDetailId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getMainUser() {
            return mainUser;
        }

        public void setMainUser(String mainUser) {
            this.mainUser = mainUser;
        }

        public String getSubUserDetailId() {
            return subUserDetailId;
        }

        public void setSubUserDetailId(String subUserDetailId) {
            this.subUserDetailId = subUserDetailId;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getIsEmployeeTpt() {
            return isEmployeeTpt;
        }

        public void setIsEmployeeTpt(String isEmployeeTpt) {
            this.isEmployeeTpt = isEmployeeTpt;
        }

        public String getCreatedByPimsId() {
            return createdByPimsId;
        }

        public void setCreatedByPimsId(String createdByPimsId) {
            this.createdByPimsId = createdByPimsId;
        }

        public String getIsEmployeeManager() {
            return isEmployeeManager;
        }

        public void setIsEmployeeManager(String isEmployeeManager) {
            this.isEmployeeManager = isEmployeeManager;
        }

    }

}
