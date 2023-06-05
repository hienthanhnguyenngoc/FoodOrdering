package com.project.foodordering_firebase.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.foodordering_firebase.model.Food;

public class Store {
    String storeID, storeCate, storeName, storeImg, address;

    public Store() {
    }

    public Store(String storeID, String storeCate, String storeName, String storeImg, String address) {
        this.storeID = storeID;
        this.storeCate = storeCate;
        this.storeName = storeName;
        this.storeImg = storeImg;
        this.address = address;
    }

    public Store(String storeCate, String storeName, String storeImg, String address) {
        this.storeCate = storeCate;
        this.storeName = storeName;
        this.storeImg = storeImg;
        this.address = address;
    }

    public String getStoreCate() {
        return storeCate;
    }

    public void setStoreCate(String storeCate) {
        this.storeCate = storeCate;
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

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }
}
