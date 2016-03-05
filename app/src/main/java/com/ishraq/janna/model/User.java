package com.ishraq.janna.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ahmed on 2/16/2016.
 */
@DatabaseTable(tableName = "user")
public class User {
    @DatabaseField(id = true)
    private Integer id;

    @DatabaseField(columnName = "user_name")
    private String useName;

    @DatabaseField(columnName = "user_type")
    private Integer userType;

    @DatabaseField(columnName = "job_dest")
    private String jobDest;

    @DatabaseField
    private String email;

    @DatabaseField
    private String address;

    @DatabaseField
    private String mobile;

    @DatabaseField(columnName = "phone")
    private String telPhone;

    @DatabaseField
    private String img;

    @DatabaseField(columnName = "not_active")
    private String notActive;

    @DatabaseField
    private String notes;

    @DatabaseField(columnName = "is_manager")
    private boolean isManager;


    ////////////////////////////////////////////////////////////

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getJobDest() {
        return jobDest;
    }

    public void setJobDest(String jobDest) {
        this.jobDest = jobDest;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNotActive() {
        return notActive;
    }

    public void setNotActive(String notActive) {
        this.notActive = notActive;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }



    /////////////////////////////////////////////////// Exist user //////////////////////////////
    public class ExistUser {
        private Integer userExist;
        private Integer id;
        private String errorMessage;

        public Integer getUserExist() {
            return userExist;
        }

        public void setUserExist(Integer userExist) {
            this.userExist = userExist;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
