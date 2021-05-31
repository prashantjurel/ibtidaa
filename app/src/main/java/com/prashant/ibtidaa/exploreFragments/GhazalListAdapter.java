package com.prashant.ibtidaa.exploreFragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashant.ibtidaa.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GhazalListAdapter extends RecyclerView.Adapter<GhazalListAdapter.GhazalListHelperViewHolder> {

    private ArrayList<GhazalListHelper> ghazalList;

    public GhazalListAdapter(ArrayList<GhazalListHelper> ghazalList) {
        this.ghazalList = ghazalList;
    }

    static class GhazalListHelperViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageGhazal;

        GhazalListHelperViewHolder(@NonNull View itemView) {
            super(itemView);
            //Hooks
            imageGhazal = itemView.findViewById(R.id.ghazalImage);
        }
    }

    @NonNull
    @NotNull
    @Override
    public GhazalListHelperViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new GhazalListHelperViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.ghazal_card_view,
                        parent,
                        false
                )
        );
    }


    @Override
    public void onBindViewHolder(@NonNull @NotNull GhazalListHelperViewHolder holder, int position) {
        Picasso.get().load(ghazalList.get(position).getImgUrl()).into(holder.imageGhazal);
    }


    @Override
    public int getItemCount() {
        return ghazalList.size();
    }

}
