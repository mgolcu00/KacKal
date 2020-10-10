package com.mert.kackal.ui.foods;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mert.kackal.R;
import com.mert.kackal.adapters.FoodsAdapter;
import com.mert.kackal.api.FireBaseClient;
import com.mert.kackal.models.FoodModel;

import java.util.ArrayList;
import java.util.List;

public class FoodsFragment extends Fragment {
    public RecyclerView recyclerView;
    public FoodsAdapter foodsAdapter;
    public List<FoodModel> foodModelList;
    public View view;
    public ProgressBar progressBar_foods;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_foods, container, false);
        progressBar_foods = view.findViewById(R.id.progress_foods);
        recyclerView = view.findViewById(R.id.recyclerview_foods);
        foodModelList = new ArrayList<FoodModel>();
        new FireBaseClient.FirebaseGetFoodsTask(this).execute();
        return view;
    }

    public void setFoodModelList(List<FoodModel> list) {
        this.foodModelList = list;
        foodsAdapter = new FoodsAdapter(view.getContext(), foodModelList, this);
        recyclerView.setAdapter(foodsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }
}
