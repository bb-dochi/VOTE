package com.example.user.vote;

/**
 * Created by SKY on 2017-11-27.
 */

public class IAM {

    private String userID;
    private String userPW;
    private String userName;
    private String userPhone;
    private String userGender;
    private String userEmail;
    private String userFcm;
    private int autoLogin;

    public int getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(int autoLogin) {
        this.autoLogin = autoLogin;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPW() {
        return userPW;
    }

    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public String getUserFcm() {
        return userFcm;
    }

    public void setUserFcm(String userFcm) {
        this.userFcm = userFcm;
    }


    private static final IAM ourInstance = new IAM();

    public static IAM getInstance() {
        return ourInstance;
    }

    private IAM() {
    }
}