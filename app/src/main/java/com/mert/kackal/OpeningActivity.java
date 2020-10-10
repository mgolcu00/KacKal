package com.mert.kackal;

import android.animation.IntArrayEvaluator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.auth.User;
import com.google.gson.Gson;
import com.google.type.DateTime;
import com.mert.kackal.models.FoodModel;
import com.mert.kackal.models.UserModel;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class OpeningActivity extends AppCompatActivity {

    private EditText editText_name;
    private EditText editText_surname;
    private EditText editText_weight;
    private EditText editText_height;
    private EditText editText_age;
    private Button button_open;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RadioGroup radioGroup_gender;
    private RadioButton radioButton_gender;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        Constants.USER = new UserModel();


        sharedPreferences = getSharedPreferences("SharedReferances", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        editText_name = findViewById(R.id.edit_opening_name);
        editText_surname = findViewById(R.id.edit_opening_surname);
        editText_height = findViewById(R.id.edit_opening_height);
        editText_weight = findViewById(R.id.edit_opening_weight);
        editText_age = findViewById(R.id.edit_opening_age);
        button_open = findViewById(R.id.open_button);
        radioGroup_gender = findViewById(R.id.radio_group_opeing_gender);
//        Constants.EAT_FOODS = new ArrayList<FoodModel>();

        Constants.PORSION_VALUES = new HashMap<String, Integer>();
        Constants.PORSION_VALUES.put(Constants.PORSIONS[0], 1);
        Constants.PORSION_VALUES.put(Constants.PORSIONS[1], 200);
        Constants.PORSION_VALUES.put(Constants.PORSIONS[2], 10);
        Constants.PORSION_VALUES.put(Constants.PORSIONS[3], 100);

        try {
            if (getUserFromReferances() != null) {
                Constants.USER = getUserFromReferances();
                if (sharedPreferences.getString("LastDay", "00-00-0000").equals(getCurrentTime())) {
                } else {
                    Constants.USER.dailyReset();
                    editor.putString("LastDay", getCurrentTime());
                    editor.apply();
                    saveUserToReferances(Constants.USER);
                }

                Intent goMainActivity = new Intent(OpeningActivity.this, MainActivity.class);
                startActivity(goMainActivity);
                OpeningActivity.this.finish();
            }

        } catch (Exception e) {

        }


        button_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton_gender = findViewById(radioGroup_gender.getCheckedRadioButtonId());

                if (editText_name.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "isim boş bırakılmaz", Toast.LENGTH_LONG).show();
                else if (editText_surname.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "soyisim boş bırakılmaz", Toast.LENGTH_LONG).show();
                else if (editText_height.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "boy boş bırakılmaz", Toast.LENGTH_LONG).show();
                else if (editText_weight.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "kilo boş bırakılmaz", Toast.LENGTH_LONG).show();
                else if (editText_age.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "yaş boş bırakılmaz", Toast.LENGTH_LONG).show();
                else if (radioGroup_gender.getCheckedRadioButtonId() == -1)
                    Toast.makeText(getApplicationContext(), "cinsiyet seciniz", Toast.LENGTH_LONG).show();
                else {

                    Constants.USER = new UserModel(
                            editText_name.getText().toString(),
                            editText_surname.getText().toString(),
                            Float.parseFloat(editText_height.getText().toString()),
                            Float.parseFloat(editText_weight.getText().toString()),
                            Integer.parseInt(editText_age.getText().toString()),
                            radioButton_gender.getTag().toString()
                    );
                    saveUserToReferances(Constants.USER);


                    Intent goMainActivity = new Intent(OpeningActivity.this, MainActivity.class);
                    goMainActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(goMainActivity);
                    OpeningActivity.this.finish();
                }
            }
        });
    }

    public static final String DATE_FORMAT_1 = "dd-MM-yyyy";

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);

    }


    public void saveUserToReferances(UserModel userModel) {

        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        editor.putString("USER_MODEL", json);
        editor.apply();
    }

    public UserModel getUserFromReferances() {
        String jsonPreferences;
        UserModel model = new UserModel();
        jsonPreferences = sharedPreferences.getString("USER_MODEL", "");
        if (jsonPreferences == "") {
            return null;
        }
        Type type = new TypeToken<UserModel>() {
        }.getType();
        model = new Gson().fromJson(jsonPreferences, type);
        return model;
    }
}
