package com.mert.kackal.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mert.kackal.Constants;
import com.mert.kackal.R;
import com.mert.kackal.api.DownloadImageTask;
import com.mert.kackal.api.FireBaseClient;
import com.mert.kackal.models.FoodModel;
import com.mert.kackal.models.PostModel;
import com.mert.kackal.ui.foods.FoodsFragment;
import com.mert.kackal.ui.posts.PostsFragment;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostsViewHolder> {

    private List<PostModel> postModels;
    private LayoutInflater layoutInflater;
    private PostsFragment postsFragment;

    public PostAdapter(List<PostModel> postModels, PostsFragment postsFragment) {
        this.postModels = postModels;
        this.postsFragment = postsFragment;
        layoutInflater = LayoutInflater.from(postsFragment.getContext());
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_post, parent, false);
        final PostAdapter.PostsViewHolder holder = new PostAdapter.PostsViewHolder(view);
        holder.post_like_button.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                holder.postModel.likePost();
                new FireBaseClient.LikePostTask(postsFragment).execute(holder.postModel);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
//                holder.postModel.dislikePost();

            }
        });
        holder.post_delete_btton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(postsFragment.getActivity());

                // Setting Dialog Title
                alertDialog2.setTitle("Post Silinecek ");

                // Setting Dialog Message
                alertDialog2.setMessage("Postu silmek istedğinizi onaylıyor musunuz ?");

                // Setting Icon to Dialog
                alertDialog2.setIcon(R.drawable.ic_baseline_delete_forever_24);

                // Setting Positive "Yes" Btn
                alertDialog2.setPositiveButton("SİL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                new FireBaseClient.DeletePostTask(postsFragment).execute(holder.postModel);
                            }
                        });
                // Setting Negative "NO" Btn
                alertDialog2.setNegativeButton("VAZGEÇ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                dialog.cancel();
                            }
                        });

                // Showing Alert Dialog
                alertDialog2.show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        PostModel post = postModels.get(position);
//        postModels.get(position);
        holder.setData(post, position);

    }

    @Override
    public int getItemCount() {
        return postModels.size();
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View itemView;

        private TextView post_author;
        private TextView post_date;
        private TextView post_text;
        private TextView post_like_count;
        private ImageView post_image;
        private ImageView post_author_image;

        private ImageButton post_delete_btton;
        private LikeButton post_like_button;

        private PostModel postModel;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            post_author = itemView.findViewById(R.id.post_author);
            post_date = itemView.findViewById(R.id.post_date);
            post_text = itemView.findViewById(R.id.post_text);
            post_image = itemView.findViewById(R.id.post_image);
            post_author_image = itemView.findViewById(R.id.post_author_image);
            post_like_count = itemView.findViewById(R.id.post_like_count);
            post_like_button = itemView.findViewById(R.id.post_like_button);
            post_delete_btton = itemView.findViewById(R.id.post_delete_button);
        }


        public void setData(PostModel post, int pos) {
            this.postModel = post;
            post_author.setText(post.getAuthorName());
            post_date.setText(post.getDateTime());
            post_text.setText(post.getText());
            post_like_count.setText(String.valueOf(post.getLikeCount()));

            Log.i("post adpter", String.valueOf(post));
            if (post.getPost_image() == null) {


                if (post.getImageUrl() == null) {
                    post_image.setVisibility(View.GONE);
                } else {
                    new DownloadImageTask(post_image, post).execute(post.getImageUrl());
                }


            } else {
                post_image.setImageBitmap(post.getPost_image());
            }
            new DownloadImageTask(post_author_image).execute(post.getAuthorImageUrl());

        }

        @Override
        public void onClick(View v) {

        }


    }
}
