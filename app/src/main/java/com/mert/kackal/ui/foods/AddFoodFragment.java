package com.mert.kackal.ui.foods;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mert.kackal.R;
import com.mert.kackal.api.FireBaseClient;
import com.mert.kackal.models.FoodModel;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddFoodFragment extends Fragment {
    private EditText txtFoodName;
    private EditText txtFoodKcal;
    private EditText txtFoodWeight;
    private EditText txtFoodDesc;

    private Button btnAddImage;
    private Button btnSaveFood;

    public ImageView imgFood;

    public ProgressBar progressBar;

    private FoodModel foodModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addfood, container, false);
        txtFoodName = view.findViewById(R.id.add_food_name);
        txtFoodKcal = view.findViewById(R.id.add_food_kcal);
        txtFoodWeight = view.findViewById(R.id.add_food_weight);
        txtFoodDesc = view.findViewById(R.id.add_food_desc);
        btnAddImage = view.findViewById(R.id.add_food_image_button);
        btnSaveFood = view.findViewById(R.id.save_food_button);
        imgFood = view.findViewById(R.id.add_food_image);
        progressBar = view.findViewById(R.id.add_progressbar);
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSave();

            }
        });
        return view;
    }

    public void clickSave() {
        new FireBaseClient.UploadFoodImageTask(txtFoodName.getText().toString(), this).execute(imgFood);
    }

    public void saveFood(String img_url1) {
        foodModel = new FoodModel(
                txtFoodName.getText().toString(),
                Float.parseFloat(txtFoodKcal.getText().toString()),
                Float.parseFloat(txtFoodWeight.getText().toString()),
                img_url1,
                txtFoodDesc.getText().toString()
        );

        foodModel.setFoodEatedKcal(0);
        Log.i("Model Uri", foodModel.getFoodImageUrl());

        new FireBaseClient.AddFoodDataTask(this).execute(foodModel);
    }

    public void clearInputs() {
        txtFoodName.setText("");
        txtFoodKcal.setText("");
        txtFoodWeight.setText("");
        txtFoodDesc.setText("");
        imgFood.setImageResource(R.drawable.ic_vegetable_512);
    }

    public void selectVisibilty(int vis) {
        getView().findViewById(R.id.foodinfo_linear).setVisibility(vis);
        imgFood.setVisibility(vis);
        btnSaveFood.setVisibility(vis);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        this.imgFood.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                this.imgFood.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
}
