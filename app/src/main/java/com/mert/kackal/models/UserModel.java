package com.mert.kackal.models;


import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class UserModel {

    private String name = "";
    private String surname = "";
    private String email = "";
    private String gender = "";
    private String imageUrl = "";
    private float targetKcal = 0;
    private float extraKcal = 0;
    private float declineKcal = 0;
    private float height = 0;
    private float weight = 0;
    private int age = 0;
    private DailyWaterNeeds dailyWaterNeeds = null;
    private List<FoodModel> eatFoodModelList = null;
    private List<PostModel> postModelList = null;
    private List<HistoryModel> historyModelList = null;

    //Null Kurucu
    public UserModel() {

    }


    public UserModel(String name, String surname, String email, String gender, float extraKcal, float declineKcal, float height, float weight, int age) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.extraKcal = extraKcal;
        this.declineKcal = declineKcal;
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    public UserModel(String name, String surname, float height, float weight, int age, String gender) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    public UserModel(String name, String surname, String email, String gender, String imageUrl, float extraKcal, float declineKcal, float height, float weight, int age, DailyWaterNeeds dailyWaterNeeds, List<FoodModel> eatFoodModelList, List<PostModel> postModelList, List<HistoryModel> historyModelList) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.extraKcal = extraKcal;
        this.declineKcal = declineKcal;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.dailyWaterNeeds = dailyWaterNeeds;
        this.eatFoodModelList = eatFoodModelList;
        this.postModelList = postModelList;
        this.historyModelList = historyModelList;
    }


    public float userEatedKcal() {
        if (eatFoodModelList == null)
            return 0;

        float v = 0;
        for (FoodModel item : eatFoodModelList) {
            v += item.getFoodEatedKcal();
        }
        return v;
    }


    public void dailyReset() {
        if (historyModelList == null)
            historyModelList = new ArrayList<HistoryModel>();
        if (dailyWaterNeeds == null)
            dailyWaterNeeds = new DailyWaterNeeds(0, getWeight(), getAge());
        historyModelList.add(new HistoryModel(getEatFoodModelList(), getDailyWaterNeeds(), getTargetKcal()));

        settingsReset();

    }

    public void settingsReset() {
        if (eatFoodModelList != null)
            eatFoodModelList.clear();

        setExtraKcal(0);
        setDeclineKcal(0);
        dailyWaterNeeds.setDailyWantWaterOfGlass(0);
        dailyWaterNeeds.setDailyDrinkedWater(0);
    }

    public void addEatedFood(FoodModel foodModel) {
        if (eatFoodModelList != null) {
            eatFoodModelList.add(foodModel);
        } else {
            eatFoodModelList = new ArrayList<FoodModel>();
            eatFoodModelList.add(foodModel);
        }

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getExtraKcal() {
        return extraKcal;
    }

    public void setExtraKcal(float extraKcal) {
        this.extraKcal = extraKcal;
    }

    public float getDeclineKcal() {
        return declineKcal;
    }

    public void setDeclineKcal(float declineKcal) {
        this.declineKcal = declineKcal;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public DailyWaterNeeds getDailyWaterNeeds() {
        if (dailyWaterNeeds == null) {
            dailyWaterNeeds = new DailyWaterNeeds(0, getWeight(), getAge());
            return dailyWaterNeeds;
        }
        return dailyWaterNeeds;
    }

    public void setDailyWaterNeeds(DailyWaterNeeds dailyWaterNeeds) {
        this.dailyWaterNeeds = dailyWaterNeeds;
    }

    public List<FoodModel> getEatFoodModelList() {
        return eatFoodModelList;
    }

    public void setEatFoodModelList(List<FoodModel> eatFoodModelList) {
        this.eatFoodModelList = eatFoodModelList;
    }

    public List<PostModel> getPostModelList() {
        return postModelList;
    }

    public void setPostModelList(List<PostModel> postModelList) {
        this.postModelList = postModelList;
    }

    public List<HistoryModel> getHistoryModelList() {
        return historyModelList;
    }

    public void setHistoryModelList(List<HistoryModel> historyModelList) {
        this.historyModelList = historyModelList;
    }

    public float getTargetKcal() {
        return targetKcal;
    }

    public void setTargetKcal(float targetKcal) {
        this.targetKcal = targetKcal;
    }
}
