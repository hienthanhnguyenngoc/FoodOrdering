package com.project.foodordering_firebase.model;

public class FavStore {
    String userName, storeID, storeName, storeImg, address;

    public FavStore() {
    }

    public FavStore(String userName, String storeID, String storeName, String storeImg, String address) {
        this.userName = userName;
        this.storeID = storeID;
        this.storeName = storeName;
        this.storeImg = storeImg;
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
