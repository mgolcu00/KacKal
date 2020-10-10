package com.mert.kackal.models;

import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mert.kackal.Constants;
import com.mert.kackal.R;
import com.mert.kackal.adapters.FoodEatListAdapter;

public class DailyKcalNeedsModel {
    private CardView cardView_DCN;

    private TextView txtDCN_daily_need;
    private TextView txtDCN_pg_text;
    public RecyclerView recyclerView_eatFood;
    public FoodEatListAdapter foodEatListAdapter;
    public TextView txtFoodEatKcal;
    private ProgressBar pb_DCN;
    private UserModel user;
    //    private float eatedKcal;
//    private float extraKcal;
//    private float declineKcal;
//    private float weight;
//    private float height;
//    private int age;
    private String gender;


    private View view;

    private float DCN_value_globe;

    public DailyKcalNeedsModel(View view, UserModel userModel) {
        this.user = userModel;
        this.cardView_DCN = view.findViewById(R.id.DCN_card_view);
        this.txtDCN_daily_need = view.findViewById(R.id.DCN_daily_kcal);
        this.txtDCN_pg_text = view.findViewById(R.id.DCN_pb_text);
        this.pb_DCN = view.findViewById(R.id.DCN_pb);
        this.recyclerView_eatFood = view.findViewById(R.id.home_kcal_eat_foods_list);
        this.txtFoodEatKcal = view.findViewById(R.id.home_kcal_eat_food_value);
        this.view = view;
    }

    public void setValues() {
        float need = DCN_value_globe = calculateDCN_value();
        float eat = user.userEatedKcal();
        float extra = user.getExtraKcal();
        float decline = user.getDeclineKcal();
        need += extra - decline;
        float rest = need - eat;

        pb_DCN.setMax((int) need);
        txtDCN_daily_need.setText(String.format("%.01f", need));
        txtFoodEatKcal.setText(String.format(" %.01f kcal", user.userEatedKcal()));
        if (eat >= need) {
            pb_DCN.setProgress((int) need - 1);
            pb_DCN.getProgressDrawable().setColorFilter(ContextCompat.getColor(view.getContext().getApplicationContext(), R.color.BMI_good_green), PorterDuff.Mode.SRC_IN);
            txtDCN_pg_text.setText(String.format("%.01f", 0.0));
            txtDCN_pg_text.setTextColor(ContextCompat.getColor(view.getContext(), R.color.BMI_good_green));
        } else {
            pb_DCN.setProgress((int) (eat));
            txtDCN_pg_text.setText(String.format("%.01f", rest));
        }

        if (user.getEatFoodModelList() == null) {

        } else {
            setRecyclerView_eatFood();
        }
        user.setTargetKcal(need);
    }

    private void setRecyclerView_eatFood() {
        foodEatListAdapter = new FoodEatListAdapter(user.getEatFoodModelList(), view);
        recyclerView_eatFood.setAdapter(foodEatListAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView_eatFood.setLayoutManager(layoutManager);
    }

    public float calculateDCN_value() {
        if (user.getGender() == "F") {
            return (float) (655 + 9.6 * user.getWeight() + 1.8 * user.getHeight() - 4.7 * user.getAge());
        } else {
            return (float) (66 + 13.7 * user.getWeight() + 5 * user.getHeight() - 6.8 * user.getAge());
        }
    }

    public void runDailyKcalNeedsModel() {
        setValues();

    }

}

