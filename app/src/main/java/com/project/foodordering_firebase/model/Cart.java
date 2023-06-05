package com.project.foodordering_firebase.model;

public class Cart {
    String userName, foodID, foodName, note, foodImg;
    Food food;
    int quantity, price;

    public Cart() {
    }

    public Cart(String userName, String foodID, String foodName, int price, int quantity, String foodImg) {
        this.userName = userName;
        this.foodID = foodID;
        this.foodName = foodName;
        this.price = price;
        this.foodImg = foodImg;
        this.quantity = quantity;
    }

    public Cart(String foodID, String foodName, int quantity, int price) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.quantity = quantity;
        this.price = price;
    }

    public Cart(String userName, String foodID, String foodName, int price, int quantity, String note, String foodImg) {
        this.userName = userName;
        this.foodID = foodID;
        this.foodName = foodName;
        this.price = price;
        this.note = note;
        this.foodImg = foodImg;
        this.quantity = quantity;
    }


    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
