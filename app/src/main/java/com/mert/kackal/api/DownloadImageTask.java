package com.mert.kackal.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mert.kackal.R;
import com.mert.kackal.models.PostModel;
import com.mert.kackal.ui.posts.PostsFragment;

import java.io.InputStream;
import java.net.URL;

//have Bug!!
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;
    private Bitmap bmp;
    private PostModel pm;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    public DownloadImageTask(ImageView bmImage, PostModel pm) {
        this.bmImage = bmImage;
        this.pm = pm;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        bmImage.setImageResource(R.drawable.ic_loading_svg_512);
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (result == null) {
            if(pm != null){
                bmImage.setVisibility(View.GONE);
                return;
            }
            bmImage.setImageResource(R.drawable.ic_vegetable_512);

        } else {
            bmp = result;
            bmImage.setImageBitmap(result);
            if (pm != null)
                pm.setPost_image(result);
        }

    }
}
