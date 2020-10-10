package com.mert.kackal.models;

public class FoodModel {
    private String foodName;
    private String foodDesc;
    private String foodCalStr;
    private String foodWeightStr;
    private String foodImageUrl;

    private float foodEatedKcal;
    private float foodCal;
    private float foodWeight;


    //HttpClient Model
    public FoodModel(String foodName, String foodCalStr, String foodWeightStr) {
        this.foodName = foodName;
        this.foodCalStr = foodCalStr;
        this.foodWeightStr = foodWeightStr;
    }

    //Model Const
    public FoodModel(String foodName, float foodCal, float foodWeight, String foodImageUrl, String foodDesc) {
        this.foodName = foodName;
        this.foodCal = foodCal;
        this.foodWeight = foodWeight;
        this.foodImageUrl = foodImageUrl;
        this.foodDesc = foodDesc;
        this.foodCalStr = String.valueOf(foodCal + " kcal");
        this.foodWeightStr = String.valueOf(foodWeight + " gr");
    }

    public FoodModel() {
    }

    public FoodModel(String foodName, String foodDesc, String foodCalStr, String foodWeightStr, String foodImageUrl, float foodCal, float foodWeight) {
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.foodCalStr = foodCalStr;
        this.foodWeightStr = foodWeightStr;
        this.foodImageUrl = foodImageUrl;
        this.foodCal = foodCal;
        this.foodWeight = foodWeight;
    }

    public FoodModel(String foodName, String foodDesc, String foodCalStr, String foodWeightStr, String foodImageUrl, float foodEatedKcal, float foodCal, float foodWeight) {
        this.foodName = foodName;
        this.foodDesc = foodDesc;
        this.foodCalStr = foodCalStr;
        this.foodWeightStr = foodWeightStr;
        this.foodImageUrl = foodImageUrl;
        this.foodEatedKcal = foodEatedKcal;
        this.foodCal = foodCal;
        this.foodWeight = foodWeight;
    }


    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public String getFoodCalStr() {
        return foodCalStr;
    }

    public void setFoodCalStr(String foodCalStr) {
        this.foodCalStr = foodCalStr;
    }

    public String getFoodWeightStr() {
        return foodWeightStr;
    }

    public void setFoodWeightStr(String foodWeightStr) {
        this.foodWeightStr = foodWeightStr;
    }


    public String getFoodImageUrl() {
        return foodImageUrl;
    }

    public void setFoodImageUrl(String foodImageUrl) {
        this.foodImageUrl = foodImageUrl;
    }

    public float getFoodCal() {
        return foodCal;
    }

    public void setFoodCal(float foodCal) {
        this.foodCal = foodCal;
    }

    public float getFoodWeight() {
        return foodWeight;
    }

    public void setFoodWeight(float foodWeight) {
        this.foodWeight = foodWeight;
    }

    public float getFoodEatedKcal() {
        return foodEatedKcal;
    }

    public void setFoodEatedKcal(float foodEatedKcal) {
        this.foodEatedKcal = foodEatedKcal;
    }
}
