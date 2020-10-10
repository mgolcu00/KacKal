package com.mert.kackal.models;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mert.kackal.receivers.AlarmBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DailyWaterNeeds {
    private static final int WATER_NEED_MULTIPER = 33;
    private static final int MILILITER_LITER_MULTIPLER = 1000;
    private static final int GLASS_FILL = 200;

    private int dailyWantWaterOfGlass = 0;
    private float dailyNeedWaterValue;
    private int dailyNeedWaterOfGlass;
    private float dailyDrinkedWater;
    private float dailyRemainWater;

    public DailyWaterNeeds(int dailyWantWaterOfGlass, float weight, int age) {
        this.dailyWantWaterOfGlass = dailyWantWaterOfGlass;
        calculate(weight, age);
    }

    private void calculate(float weight, int age) {
        dailyNeedWaterValue = WATER_NEED_MULTIPER * weight + (age * 5) + (dailyWantWaterOfGlass * GLASS_FILL);
        dailyNeedWaterOfGlass = Math.round(dailyNeedWaterValue / GLASS_FILL) + dailyWantWaterOfGlass;
        dailyRemainWater = dailyNeedWaterValue;
        dailyDrinkedWater = 0;

    }

    public void drinkWater(float mililiter) {
        dailyRemainWater -= mililiter;
        dailyDrinkedWater += mililiter;
    }

public boolean drinkedAll(){ return  getDailyDrinkedWaterGlass()>=dailyNeedWaterOfGlass;}
    public void drinkGlassWater(int count) {
        drinkWater(GLASS_FILL * count);
    }

    public int dailyRemainWaterofGlass() {
        return Math.round(dailyRemainWater / GLASS_FILL);
    }


    public int getDailyWantWaterOfGlass() {
        return dailyWantWaterOfGlass;
    }

    public void setDailyWantWaterOfGlass(int dailyWantWaterOfGlass) {
        this.dailyWantWaterOfGlass = dailyWantWaterOfGlass;
    }

    public float getDailyNeedWaterValue() {
        return dailyNeedWaterValue;
    }

    public void setDailyNeedWaterValue(float dailyNeedWaterValue) {
        this.dailyNeedWaterValue = dailyNeedWaterValue;
    }

    public int getDailyNeedWaterOfGlass() {
        return dailyNeedWaterOfGlass;
    }

    public void setDailyNeedWaterOfGlass(int dailyNeedWaterOfGlass) {
        this.dailyNeedWaterOfGlass = dailyNeedWaterOfGlass;
    }

    public float getDailyDrinkedWater() {
        return dailyDrinkedWater;
    }

    public int getDailyDrinkedWaterGlass() {
        return Math.round(dailyDrinkedWater / GLASS_FILL);
    }

    public float getDailyNeedWaterLiter() {
        return dailyNeedWaterValue / MILILITER_LITER_MULTIPLER;
    }

    public float getDailyDrinkedWaterLiter() {
        return dailyDrinkedWater / MILILITER_LITER_MULTIPLER;
    }

    public void setDailyDrinkedWater(float dailyDrinkedWater) {
        this.dailyDrinkedWater = dailyDrinkedWater;
    }

    public float getDailyRemainWater() {
        return dailyRemainWater;
    }

    public void setDailyRemainWater(float dailyRemainWater) {
        this.dailyRemainWater = dailyRemainWater;
    }


}
