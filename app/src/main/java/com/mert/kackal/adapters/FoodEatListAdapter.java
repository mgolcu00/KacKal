package com.mert.kackal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mert.kackal.Constants;
import com.mert.kackal.R;
import com.mert.kackal.api.DownloadImageTask;
import com.mert.kackal.models.FoodModel;
import com.mert.kackal.ui.foods.FoodsFragment;
import com.mert.kackal.ui.home.HomeFragment;

import java.util.List;

public class FoodEatListAdapter extends RecyclerView.Adapter<FoodEatListAdapter.FoodsEatListViewHolder> {
    private List<FoodModel> foodModelList;
    private LayoutInflater layoutInflater;
    private View view;

    public FoodEatListAdapter(List<FoodModel> foodModelList, View view) {
        this.foodModelList = foodModelList;
        this.view = view;
        layoutInflater = LayoutInflater.from(view.getContext());
    }

    @NonNull
    @Override
    public FoodsEatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_home_eat_foods, parent, false);
        FoodEatListAdapter.FoodsEatListViewHolder holder = new FoodEatListAdapter.FoodsEatListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodsEatListViewHolder holder, int position) {
        FoodModel selectedProduct = foodModelList.get(position);
        holder.setData(selectedProduct, position);
    }

    @Override
    public int getItemCount() {
        return foodModelList.size();
    }


    public class FoodsEatListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View itemV;
        private FoodModel foodModel;
        private TextView foodName;
        private TextView foodKcal;
        private ImageView foodImage;

        public FoodsEatListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemV = itemView;
            foodName = itemView.findViewById(R.id.item_eat_list_food_name);
            foodKcal = itemView.findViewById(R.id.item_eat_list_food_kcal);
            foodImage = itemView.findViewById(R.id.item_eat_list_food_image);

        }

        public void setData(FoodModel data, int pos) {
            this.foodModel = data;
            foodName.setText(data.getFoodName());
            foodKcal.setText(String.format("%.01f kcal", data.getFoodEatedKcal()));
            new DownloadImageTask((ImageView) this.foodImage).execute(data.getFoodImageUrl());
        }

        @Override
        public void onClick(View v) {

        }
    }
}
