package com.mert.kackal.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.mert.kackal.Constants;
import com.mert.kackal.OpeningActivity;
import com.mert.kackal.R;
import com.mert.kackal.models.UserModel;

public class SettingsFragment extends Fragment {

    private EditText editText_name;
    private EditText editText_surname;
    private EditText editText_weight;
    private EditText editText_height;
    private EditText editText_age;
    private EditText editText_extra_kcal;
    private EditText editText_email;
    private EditText editText_decline_kcal;
    private Button button_save;
    private Button button_reset;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RadioGroup radioGroup_gender;
    private RadioButton radioButton_male;
    private RadioButton radioButton_female;

    private UserModel user = Constants.USER;
    private View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        findViews();
        sharedPreferences = getActivity().getSharedPreferences("SharedReferances", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setViews();
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveViews();
                startActivity(new Intent(getActivity(), OpeningActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.settingsReset();
                saveUser();
                startActivity(new Intent(getActivity(), OpeningActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });
        return view;
    }

    private void findViews() {
        editText_name = view.findViewById(R.id.set_name);
        editText_surname = view.findViewById(R.id.set_surname);
        editText_weight = view.findViewById(R.id.set_weight);
        editText_height = view.findViewById(R.id.set_height);
        editText_age = view.findViewById(R.id.set_age);
        editText_extra_kcal = view.findViewById(R.id.set_extra_kcal);
        editText_decline_kcal = view.findViewById(R.id.set_decline_kcal);
        editText_email = view.findViewById(R.id.set_email);
        button_save = view.findViewById(R.id.set_save_button);
        radioGroup_gender = view.findViewById(R.id.set_radio_group_gender);
        radioButton_male = view.findViewById(R.id.set_radio_button_male);
        radioButton_female = view.findViewById(R.id.set_radio_button_female);
        button_reset = view.findViewById(R.id.set_reset_button);
    }


    private void setViews() {

        editText_name.setHint(user.getName());
        editText_surname.setHint(user.getSurname());
        editText_weight.setHint(String.format("%.00f", user.getWeight()));
        editText_height.setHint(String.format("%.00f", user.getHeight()));
        editText_age.setHint(String.valueOf(user.getAge()));
        editText_decline_kcal.setHint(String.format("%.01f", user.getDeclineKcal()));
        editText_extra_kcal.setHint(String.format("%.01f", user.getExtraKcal()));
        editText_email.setHint(user.getEmail());
        String gender = user.getGender();
        if (gender.equals("F")) {
            radioGroup_gender.check(radioButton_female.getId());
        } else {
            radioGroup_gender.check(radioButton_male.getId());
        }


    }

    private boolean isChanged(EditText editText, String s) {
        if (editText.getText().toString().equals(s) || editText.getText().toString().equals(""))
            return false;
        else
            return true;
    }

    private boolean isChanged(EditText editText, float f) {
        if (editText.getText().toString().equals(f) || editText.getText().toString().equals(""))
            return false;
        else
            return true;
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

    private void saveViews() {
        if (isChanged(editText_name, user.getName()))
            user.setName(editText_name.getText().toString());

        if (isChanged(editText_surname, user.getSurname()))
            user.setSurname(editText_surname.getText().toString());

        if (isChanged(editText_weight, user.getWeight()))
            user.setWeight(Float.parseFloat(editText_weight.getText().toString()));

        if (isChanged(editText_height, user.getWeight()))
            user.setHeight(Float.parseFloat(editText_height.getText().toString()));

        if (isChanged(editText_age, user.getAge()))
            user.setAge(Integer.parseInt(editText_age.getText().toString()));

        if (isChanged(editText_extra_kcal, user.getExtraKcal()))
            user.setExtraKcal(Float.parseFloat(editText_extra_kcal.getText().toString()));

        if (isChanged(editText_decline_kcal, user.getDeclineKcal()))
            user.setDeclineKcal(Float.parseFloat(editText_decline_kcal.getText().toString()));

        if (isChanged(editText_email, user.getEmail()))
            user.setEmail(editText_email.getText().toString());


        if (view.findViewById(radioGroup_gender.getCheckedRadioButtonId()).getTag().toString() == user.getGender()) {
        } else
            user.setGender(view.findViewById(radioGroup_gender.getCheckedRadioButtonId()).getTag().toString());


        saveUser();
    }

}
