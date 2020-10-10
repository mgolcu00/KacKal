package com.mert.kackal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mert.kackal.api.DownloadImageTask;
import com.mert.kackal.api.FireBaseClient;
import com.mert.kackal.models.UserModel;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView drawer_name_surname;
    private UserModel user = Constants.USER;
    private ImageView profile_image;
    private static final int MAIN_CODE = 4091;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        drawer_name_surname = (TextView) headerView.findViewById(R.id.drawer_name_surname);
        drawer_name_surname.setText(user.getName() + " " + user.getSurname() + "  (" + user.getAge() + ") ");
        TextView drawer_email = headerView.findViewById(R.id.drawer_email);
        profile_image = headerView.findViewById(R.id.profile_image);
        if (user.getImageUrl() != null) {
            new DownloadImageTask(profile_image).execute(user.getImageUrl());
        }
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(MainActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(MAIN_CODE);

            }
        });
        drawer_email.setText(user.getEmail());
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_foods, R.id.nav_add_food, R.id.nav_posts, R.id.nav_add_post)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //Navigation Drawer

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAIN_CODE)
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            profile_image.setImageURI(uri);
            File file = ImagePicker.Companion.getFile(data);
            String filePath = ImagePicker.Companion.getFilePath(data);
            new FireBaseClient.UploatUserProfilePhotoTask(user.getName(), MainActivity.this).execute(profile_image);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUserPhoto(String imgUrl) {
        new DownloadImageTask(profile_image).execute(imgUrl);
        Constants.USER.setImageUrl(imgUrl);
        saveUser();
    }

    public void saveUser() {
        Constants.USER = user;
        saveUserToReferances(Constants.USER);
    }

    public void saveUserToReferances(UserModel userModel) {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedReferances", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userModel);
        editor.putString("USER_MODEL", json);
        editor.commit();
    }
}
