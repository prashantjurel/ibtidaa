package com.prashant.ibtidaa.HelperClasses.HomeAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashant.ibtidaa.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.AdapterAllCategoriesViewHolder> {

    ArrayList<CategoriesHelperClass> mostViewedLocations;

    public CategoriesAdapter(ArrayList<CategoriesHelperClass> mostViewedLocations) {
        this.mostViewedLocations = mostViewedLocations;
    }

    @NonNull
    @Override
    public AdapterAllCategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card_layout, parent, false);
        AdapterAllCategoriesViewHolder lvh = new AdapterAllCategoriesViewHolder(view);
        return lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAllCategoriesViewHolder holder, int position) {
        holder.setCategoryData(mostViewedLocations.get(position));
        CategoriesHelperClass helperClass = mostViewedLocations.get(position);
    }

    @Override
    public int getItemCount() {
        return mostViewedLocations.size();
    }

    public static class AdapterAllCategoriesViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        ImageView imageView;

        public AdapterAllCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_explore);
        }

        public void setCategoryData(CategoriesHelperClass categoriesHelperClass) {
            Picasso.get().load(categoriesHelperClass.getImageView()).into(imageView);
        }
    }
}