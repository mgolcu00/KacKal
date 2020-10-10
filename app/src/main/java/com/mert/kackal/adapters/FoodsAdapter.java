package com.mert.kackal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mert.kackal.Constants;
import com.mert.kackal.R;
import com.mert.kackal.api.DownloadImageTask;
import com.mert.kackal.models.FoodModel;
import com.mert.kackal.ui.foods.FoodCustomDialog;
import com.mert.kackal.ui.foods.FoodsFragment;

import java.util.List;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.FoodsViewHolder> {

    private List<FoodModel> foodModelList;
    private LayoutInflater layoutInflater;
    private FoodsFragment foodsFragment;

    public FoodsAdapter(Context context, List<FoodModel> foodModelList, FoodsFragment foodsFragment) {
        this.foodModelList = foodModelList;
        this.foodsFragment = foodsFragment;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public FoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_food, parent, false);
        FoodsViewHolder holder = new FoodsViewHolder(view, foodsFragment);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodsViewHolder holder, int position) {
        FoodModel selectedProduct = foodModelList.get(position);
        holder.setData(selectedProduct, position);
    }


    @Override
    public int getItemCount() {
        return foodModelList.size();
    }

    public class FoodsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView foodImage;
        private TextView foodKcal;
        private TextView foodName;
        private TextView foodWeight;
        private TextView foodDesc;
        private View itemV;
        private float foodKcal_value;
        private FoodsFragment foodsFragment;
        private FoodModel selectedFoodModel;

        public FoodsViewHolder(@NonNull View itemView, FoodsFragment foodsFragment) {
            super(itemView);
            this.foodsFragment = foodsFragment;
            foodImage = itemView.findViewById(R.id.item_image);
            foodName = itemView.findViewById(R.id.item_foodName);
            foodKcal = itemView.findViewById(R.id.item_foodKcal);
            foodWeight = itemView.findViewById(R.id.item_foodWeight);
            foodDesc = itemView.findViewById(R.id.item_foodDesc);
            itemView.setOnClickListener(this);
            itemV = itemView;
        }

        public void setData(FoodModel data, int pos) {
            selectedFoodModel = data;
            this.foodName.setText(data.getFoodName());
            this.foodKcal.setText(data.getFoodCalStr());
            this.foodWeight.setText(data.getFoodWeightStr());
            this.foodKcal_value = data.getFoodCal();
            this.foodDesc.setText(data.getFoodDesc());
            new DownloadImageTask((ImageView) this.foodImage).execute(data.getFoodImageUrl());
        }

        @Override
        public void onClick(View v) {

            FoodCustomDialog foodCustomDialog = new FoodCustomDialog(selectedFoodModel, Constants.USER);
            foodCustomDialog.show(foodsFragment.getActivity().getSupportFragmentManager(), "Dialog");

//            Toast.makeText(itemV.getContext(), foodKcal.getText().toString(), Toast.LENGTH_LONG).show();
        }
    }
}
