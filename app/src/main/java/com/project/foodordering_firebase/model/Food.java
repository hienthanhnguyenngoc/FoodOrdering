package com.project.foodordering_firebase.model;

public class Food {
    String foodID, foodName, price, foodDes, foodImg, storeCate, storeName;

    public Food() {
    }

    public Food(String foodID, String foodName, String price, String foodDes, String foodImg, String storeCate, String storeName) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.price = price;
        this.foodDes = foodDes;
        this.foodImg = foodImg;
        this.storeCate = storeCate;
        this.storeName = storeName;
    }

    public Food(String foodID, String foodName, String price, String foodDes, String foodImg, String storeCate) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.price = price;
        this.foodDes = foodDes;
        this.foodImg = foodImg;
        this.storeCate = storeCate;
    }

    public Food(String foodID, String foodName, String price, String foodDes, String foodImg) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.price = price;
        this.foodDes = foodDes;
        this.foodImg = foodImg;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFoodDes() {
        return foodDes;
    }

    public void setFoodDes(String foodDes) {
        this.foodDes = foodDes;
    }

    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
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
}
