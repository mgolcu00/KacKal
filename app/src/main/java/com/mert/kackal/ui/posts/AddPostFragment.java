package com.mert.kackal.ui.posts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.mert.kackal.Constants;
import com.mert.kackal.MainActivity;
import com.mert.kackal.R;
import com.mert.kackal.api.FireBaseClient;
import com.mert.kackal.models.PostModel;
import com.mert.kackal.models.UserModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPostFragment extends Fragment {
    private EditText post_add_text;
    private ImageView post_add_image;
    private Button post_add_image_button;
    private Button post_add_send_button;
    public ProgressBar post_add_progress;
    private View mView;
    private PostModel postModel;
    private UserModel user;
    private ViewGroup container;
    private static final int ADD_POST_CODE = 4039;
    private boolean isImageAdd = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        this.mView = view;
        this.container = container;
        user = Constants.USER;
        post_add_text = view.findViewById(R.id.post_add_text);
        post_add_image = view.findViewById(R.id.post_add_image);
        post_add_image_button = view.findViewById(R.id.post_add_image_button);
        post_add_send_button = view.findViewById(R.id.post_add_save_button);
        post_add_progress = view.findViewById(R.id.post_add_progress);
        post_add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImagePicker.Companion.with(AddPostFragment.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start(ADD_POST_CODE);
            }
        });
        post_add_send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSend();
            }
        });

        return view;
    }


    public void selectVisibilty(int vis) {
        getView().findViewById(R.id.post_add_linear).setVisibility(vis);
    }

    public void changeFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PostsFragment llf = new PostsFragment();
        ft.replace(container.getId(), llf);
        ft.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        Log.i("Post Add", String.valueOf(resultCode));
        if (requestCode == ADD_POST_CODE)
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();

                post_add_image.setImageURI(uri);
                isImageAdd = true;
                File file = ImagePicker.Companion.getFile(data);
                String filePath = ImagePicker.Companion.getFilePath(data);

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(getActivity(), ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
    }

    public void clickSend() {
        new FireBaseClient.UploadPostImageTask(this, user.getName() + "_" + getCurrentTime(), isImageAdd    )
                .execute(post_add_image);
    }

    public void sendPost(String img_url) {
        postModel = new PostModel(
                getCurrentTime(), user.getName(),
                post_add_text.getText().toString(), img_url
        );
        postModel.setAuthorImageUrl(Constants.USER.getImageUrl());
        new FireBaseClient.AddPostTask(this).execute(postModel);
        //Task
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss - dd.MM.yyyy");
        Date tdy = new Date();
        return dateFormat.format(tdy);

    }


}
