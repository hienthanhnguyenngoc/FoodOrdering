package com.project.foodordering_firebase.model;

public class User {
    String userName, fullName,  passWord, phoneNumber, address;

    public User() {
    }

    public User(String userName, String fullName, String phoneNumber, String passWord) {
        this.userName = userName;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.passWord = passWord;
    }

    public User(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public User(String userName, String fullName, String passWord, String phoneNumber, String address) {
        this.userName = userName;
        this.fullName = fullName;
        this.passWord = passWord;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public User(String userName, String fullName, String phoneNumber) {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
