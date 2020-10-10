package com.mert.kackal.ui.posts;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mert.kackal.Constants;
import com.mert.kackal.R;
import com.mert.kackal.adapters.PostAdapter;
import com.mert.kackal.api.FireBaseClient;
import com.mert.kackal.models.PostModel;
import com.mert.kackal.models.UserModel;

import java.util.List;

public class PostsFragment extends Fragment {
    public View view;

    private FloatingActionButton post_fab;
    public RecyclerView recyclerView_posts;
    public ProgressBar progressBar_posts;
    private PostAdapter postAdapter;
    private List<PostModel> postModels;
    private UserModel user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        user = Constants.USER;
        view = inflater.inflate(R.layout.fragment_posts, container, false);
        recyclerView_posts = view.findViewById(R.id.recyclerview_posts);
        progressBar_posts = view.findViewById(R.id.progress_posts);
        post_fab = view.findViewById(R.id.post_fab);

        new FireBaseClient.GetPostsTask(this).execute();

        post_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //faba tıklandıgında
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                AddPostFragment llf = new AddPostFragment();
                ft.replace(container.getId(), llf);
                ft.commit();
            }
        });

        return view;
    }

    public void setPostList(List<PostModel> postModels) {
        this.postModels = postModels;
        postAdapter = new PostAdapter(postModels, this);
        recyclerView_posts.setAdapter(postAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView_posts.setLayoutManager(layoutManager);
    }

    public void updateList() {

        postAdapter.notifyDataSetChanged();
    }
    public void deletePost(PostModel postModel){
        postModels.remove(postModels.indexOf(postModel));
        updateList();
    }

    public void setListItemImage(int pos, Bitmap bmp) {
        postModels.get(pos).setPost_image(bmp);

    }
//    public void replaceFragments(Class fragmentClass) {
//        Fragment fragment = null;
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, fragment)
//                .commit();
//    }
}
//Snackbar
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();