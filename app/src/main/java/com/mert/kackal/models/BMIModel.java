package com.mert.kackal.models;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.firebase.firestore.auth.User;
import com.mert.kackal.R;

import java.util.ArrayList;
import java.util.List;

public class BMIModel {
    private CardView crdBmiCardView;
    private TextView txtBmiValue;
    private TextView txtBmiStatus;
    private TextView txtBmiIdealWeight;
    private TextView txtBmiBodyArea;
    private ProgressBar pgBmi;
    private List<Integer> colors;
    private View currentView;
    private float BMI_value_globe;
    private UserModel user;

    public BMIModel(View view, UserModel user) {
        this.currentView = view;
        this.user = user;
    }

    public void findViews() {
        crdBmiCardView = (CardView) currentView.findViewById(R.id.bmi_card_view);

        txtBmiValue = (TextView) currentView.findViewById(R.id.bmi_value);
        txtBmiStatus = (TextView) currentView.findViewById(R.id.bmi_status_text);
        txtBmiIdealWeight = (TextView) currentView.findViewById(R.id.BMI_spending_kcal);
        txtBmiBodyArea = (TextView) currentView.findViewById(R.id.bmi_body_area_value);


        pgBmi = (ProgressBar) currentView.findViewById(R.id.bmi_pb);
    }

    public void setValues() {
        float BMI_value = user.getWeight() / (user.getHeight() / 100 * user.getHeight() / 100);
        float BMI_idealWeight;
        if (user.getGender() == "F") {
            BMI_idealWeight = (float) (45.5 + 2.3 * (user.getHeight() / 2.54) - 60);
        } else {
            BMI_idealWeight = (float) (50 + 2.3 * ((user.getHeight() / 2.54) - 60));
        }
        float BMI_bodyArea = (float) Math.sqrt((double) (user.getHeight() * user.getWeight()) / 3600);
        float spendKcal = (float) (22.02 * user.getWeight());
        txtBmiValue.setText(String.format("%.01f", BMI_value));
        txtBmiBodyArea.setText(String.format("%.01f m2", BMI_bodyArea));
        txtBmiIdealWeight.setText(String.format("%.01f kcal", spendKcal));
        txtBmiStatus.setText(BMI_Calculate(BMI_value));
        BMI_value_globe = BMI_value;
        setPB();
        setColors();
    }

    public String BMI_Calculate(float BMI_value) {


        if (BMI_value < 18.4) {
            return "İdeal Kilonun Altında ";
        } else if (BMI_value <= 24.9) {
            return "Kilonuz İdeal";
        } else if (BMI_value <= 29.9) {
            return "İdeal Kilonun Üzerinde";
        } else if (BMI_value <= 34.9) {
            return "İdeal Kilonun Çok Üzerinde";
        } else if (BMI_value <= 44.9) {
            return "Kilonuz Riskli Bölgede ";
        } else {
            return "Kilonuz Kritik Acil Yardım Alın !";
        }
    }


    public void setPB() {

        int BMI_value_int = (int) BMI_value_globe;
        pgBmi.setProgress(BMI_value_int);
    }

    public void setColors() {
        colors = new ArrayList<Integer>();
        colors.add(currentView.getContext().getResources().getColor(R.color.BMI_good_green));
        colors.add(currentView.getContext().getResources().getColor(R.color.BMI_meh_orange));
        colors.add(currentView.getContext().getResources().getColor(R.color.BMI_bad_red));

        if (BMI_value_globe < 18.4) {

            paint(colors.get(1));
        } else if (BMI_value_globe <= 24.9) {
            paint(colors.get(0));
        } else if (BMI_value_globe <= 29.9) {
            paint(colors.get(1));
        } else {
            paint(colors.get(2));
        }
    }

    public void paint(int color) {
        txtBmiStatus.setTextColor(color);
        txtBmiValue.setTextColor(color);
        txtBmiIdealWeight.setTextColor(color);
        txtBmiBodyArea.setTextColor(color);
        pgBmi.getProgressDrawable().setColorFilter(
                color, android.graphics.PorterDuff.Mode.SRC_IN);
    }

    public void runBodyMassIndexModel() {
        findViews();
        setValues();
    }

    public float getBMI_value_globe() {
        return BMI_value_globe;
    }
}
