package com.mert.kackal.ui.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.mert.kackal.Constants;
import com.mert.kackal.R;
import com.mert.kackal.api.HttpJsonClient;
import com.mert.kackal.models.BMIModel;
import com.mert.kackal.models.DailyKcalNeedsModel;
import com.mert.kackal.models.DailyWaterNeeds;
import com.mert.kackal.models.UserModel;
import com.mert.kackal.receivers.AlarmBroadcastReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HomeFragment extends Fragment {

    public View view;
    private UserModel user = Constants.USER;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //For Water Needs
    private DailyWaterNeeds dailyWaterNeeds;

    private ProgressBar pb_water;
    private TextView txt_need_water_liter;
    private TextView txt_drinked_water_of_glass;
    private TextView txt_drinked_water_liter;
    private TextView txt_need_water_glass;
    private Button btn_drink_glass_of_water;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        sharedPreferences = getActivity().getSharedPreferences("SharedReferances", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dailyWaterNeeds = user.getDailyWaterNeeds();
        pb_water = view.findViewById(R.id.water_progress);
        txt_need_water_liter = view.findViewById(R.id.water_daily);
        txt_drinked_water_of_glass = view.findViewById(R.id.water_count_off_glass);
        txt_need_water_glass = view.findViewById(R.id.water_daily_galss);
        txt_drinked_water_liter = view.findViewById(R.id.water_drinked_liter);
        btn_drink_glass_of_water = view.findViewById(R.id.water_add_glass_water);
        setNeedsWaterLiter();
        updateWaterGlassCount();
        if (!dailyWaterNeeds.drinkedAll())
            calculateDailyClock();
        btn_drink_glass_of_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyWaterNeeds.drinkGlassWater(1);
                updateWaterGlassCount();
                saveUser();
            }
        });

        new BMIModel(view, user).runBodyMassIndexModel();
        new DailyKcalNeedsModel(view, user).runDailyKcalNeedsModel();
        new HttpJsonClient.GetNewsFromJsonTask(getActivity()).execute();


        return view;
    }

    private void updateWaterGlassCount() {
        txt_drinked_water_of_glass.setText(String.valueOf(dailyWaterNeeds.getDailyDrinkedWaterGlass()));
        txt_drinked_water_liter.setText(String.format("%.01f", dailyWaterNeeds.getDailyDrinkedWaterLiter()));
        if (dailyWaterNeeds.getDailyNeedWaterValue() <= dailyWaterNeeds.getDailyDrinkedWater()) {
            txt_need_water_liter.setTextColor(getActivity().getColor(R.color.BMI_good_green));
        }
        if (dailyWaterNeeds.getDailyNeedWaterOfGlass() <= dailyWaterNeeds.getDailyDrinkedWaterGlass()) {
            txt_need_water_glass.setTextColor(getActivity().getColor(R.color.BMI_good_green));
        }
        setProgresBar();
    }

    private void setProgresBar() {
        pb_water.setMax((int) dailyWaterNeeds.getDailyNeedWaterValue());
        if (dailyWaterNeeds.getDailyNeedWaterValue() > dailyWaterNeeds.getDailyDrinkedWater())
            pb_water.setProgress((int) dailyWaterNeeds.getDailyDrinkedWater());
        else {
            pb_water.setProgress((int) dailyWaterNeeds.getDailyNeedWaterValue());
        }
    }

    private void setNeedsWaterLiter() {
        txt_need_water_liter.setText(String.format("%.01f Litre", dailyWaterNeeds.getDailyNeedWaterLiter()));
        txt_need_water_glass.setText(String.format("%d Bardak", dailyWaterNeeds.getDailyNeedWaterOfGlass()));
        txt_drinked_water_liter.setText(String.format("%.01f", dailyWaterNeeds.getDailyDrinkedWaterLiter()));
    }

    public void saveUser() {
        Constants.USER = user;
        saveUserToReferances(Constants.USER);
    }

    public void saveUserToReferances(UserModel userModel) {

        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        editor.putString("USER_MODEL", json);
        editor.commit();
    }

    public void calculateDailyClock() {
        int dayHourCount = 24;
        int daySleepCount = 7;
        dayHourCount -= daySleepCount;
        float timeFrame = (float) dayHourCount / (float) dailyWaterNeeds.getDailyNeedWaterOfGlass();
        float min = (float) (timeFrame * 60);
//        int t[] = timingCalcs(min);
        for (float i = 0; i < dailyWaterNeeds.getDailyNeedWaterOfGlass() * min; i += min) {
            int t[] = timingCalcs(i);
            String now = getCurrentTime();
//            Log.i("timing","now "+ now);
            String[] nows = now.split(":");

            if (t[0] >= Integer.parseInt(nows[0])) {
                if (t[0] == Integer.parseInt(nows[0]))
                    if (t[1] > Integer.parseInt(nows[1])) {
                        Log.i("timing ", String.format("time test : %d - %d", t[0], Integer.parseInt(nows[0])));
                        startAlarmBroadcastReceiver(getActivity(), t);
                        break;
                    }
                startAlarmBroadcastReceiver(getActivity(), t);
                break;
            }
        }

    }

    private int[] timingCalcs(float minute) {
        String now = "09:00:00";
        String[] nows = now.split(":");
        int nowHour = Integer.parseInt(nows[0]);
        int nowMinute = Integer.parseInt(nows[1]);
        int nowSecond = Integer.parseInt(nows[2]);
        nowHour += minute / 60;
        nowMinute += minute % 60;
        return new int[]{nowHour, nowMinute, nowSecond};
    }


    public static void startAlarmBroadcastReceiver(Context context, int time[]) {
        int hour = time[0];
        int minute = time[1];
        int second = time[2];
        Intent _intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, _intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.i("timing ", String.format("time : %d:%d:%d", hour, minute, second));
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date tdy = new Date();
        return dateFormat.format(tdy);

    }

}