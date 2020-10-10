package com.mert.kackal.ui.foods;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.gson.Gson;
import com.mert.kackal.Constants;
import com.mert.kackal.R;
import com.mert.kackal.models.FoodModel;
import com.mert.kackal.models.UserModel;

public class FoodCustomDialog extends AppCompatDialogFragment implements AdapterView.OnItemSelectedListener {
    private TextView txtFoodName;
    private TextView txtFoodKcal;
    private EditText edtFoodValue;
    private Spinner spnValueType;
    private Button btnCancel;
    private Button btnAdd;
    private boolean isPass = false;
    private int Multiper = 1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private UserModel user;
    private FoodModel food;

    public FoodCustomDialog(FoodModel food, UserModel user) {
        this.food = food;
        this.user = user;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        sharedPreferences = getActivity().getSharedPreferences("SharedReferances", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.item_food_dialog, null);
        findViews(view);
        builder.setView(view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_spinner, Constants.PORSIONS);
        spnValueType.setOnItemSelectedListener(this);
        spnValueType.setAdapter(arrayAdapter);
        txtFoodKcal.setText(String.format("%.01f kcal", food.getFoodCal()));
        txtFoodName.setText(food.getFoodName());
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food.setFoodEatedKcal(getKcalValue());
                user.addEatedFood(food);
                saveUser();
                getDialog().cancel();
                Toast.makeText(getActivity(), food.getFoodName() + " Eklendi", Toast.LENGTH_LONG).show();
            }
        });

        return builder.create();
    }

    private void findViews(View view) {
        txtFoodName = view.findViewById(R.id.alert_food_name);
        txtFoodKcal = view.findViewById(R.id.alert_food_kcal);
        edtFoodValue = view.findViewById(R.id.alert_value);
        spnValueType = view.findViewById(R.id.alert_spinner);
        btnAdd = view.findViewById(R.id.alert_button_add);
        btnCancel = view.findViewById(R.id.alert_button_cancel);
    }

    //Spinner selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Multiper = Constants.PORSION_VALUES.get(Constants.PORSIONS[position]);
        isPass = true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), "Lütfen bir posiyon türü seciniz ", Toast.LENGTH_LONG).show();
        isPass = false;
    }

    private float getKcalValue() {
        float kcal = food.getFoodCal() / food.getFoodWeight();
        return kcal * Float.parseFloat(edtFoodValue.getText().toString()) * Multiper;


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


}
