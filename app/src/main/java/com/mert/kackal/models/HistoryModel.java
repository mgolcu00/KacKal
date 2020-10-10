package com.mert.kackal.models;

import com.google.type.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class HistoryModel {
    private String date;
    private DailyWaterNeeds dailyWaterNeeds;
    private List<FoodModel> foodModelList;
    private float eatedKcal;
    private float targetKcal;
    private boolean isKcalTargetOk;
    private boolean isWaterTargetOk;
    private static final String DATE_FORMAT = "dd-MM-yyyy";

    public HistoryModel(List<FoodModel> foodModelList, DailyWaterNeeds dailyWaterNeeds, float targetKcal) {
        this.foodModelList = foodModelList;
        this.dailyWaterNeeds = dailyWaterNeeds;
        this.targetKcal = targetKcal;
        date = getNowDateTime();
        findEatedKcal();
        findTarget();
    }


    private void findEatedKcal() {
        if (foodModelList == null) {
            eatedKcal = 0;
            return;

        }
        float v = 0;
        for (FoodModel item : foodModelList) {
            v += item.getFoodEatedKcal();
        }
        eatedKcal = v;
    }

    private void findTarget() {
        isKcalTargetOk = eatedKcal >= targetKcal;
        isWaterTargetOk = dailyWaterNeeds.getDailyDrinkedWater() >= dailyWaterNeeds.getDailyNeedWaterValue();
    }

    private String getNowDateTime() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date date1 = new Date();
        return String.valueOf(format.format(date1));
    }


}
