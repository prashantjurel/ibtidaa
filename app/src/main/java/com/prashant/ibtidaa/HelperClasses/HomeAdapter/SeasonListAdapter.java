package com.prashant.ibtidaa.HelperClasses.HomeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prashant.ibtidaa.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SeasonListAdapter extends RecyclerView.Adapter<SeasonListAdapter.SeasonListHelperViewHolder> {

    private ArrayList<SeasonListHelper> seasonsList;
    private OnNoteListener mOnNoteListener;
    private String tag;

    public SeasonListAdapter(ArrayList<SeasonListHelper> seasonsList,OnNoteListener mOnNoteListener,String tag) {
        this.seasonsList = seasonsList;
        this.mOnNoteListener = mOnNoteListener;
        this.tag = tag;
    }

    static class SeasonListHelperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView albumArtRecycler;
        private TextView textAuthor;
        private OnNoteListener onNoteListener;
        private String tag;

        SeasonListHelperViewHolder(@NonNull View itemView, OnNoteListener onNoteListener, String tag) {
            super(itemView);
            //Hooks
            albumArtRecycler = itemView.findViewById(R.id.albumArtRecycler);
            textAuthor = itemView.findViewById(R.id.textAuthor);
            this.onNoteListener = onNoteListener;
            this.tag = tag;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onNoteListener.onClickNote(getAdapterPosition(),tag,view);
        }

    }

    @NonNull
    @NotNull
    @Override
    public SeasonListHelperViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SeasonListHelperViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.dashboard_cardview_layout,
                        parent,
                        false
                ),mOnNoteListener,tag
        );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SeasonListHelperViewHolder holder, int position) {
        Picasso.get().load(seasonsList.get(position).getSeasonAlbumArt()).into(holder.albumArtRecycler);
        holder.textAuthor.setText(seasonsList.get(position).getAuthor());
    }


    @Override
    public int getItemCount() {
        return seasonsList.size();
    }

    public interface OnNoteListener{
        void onClickNote(int position, String tag,View view);
    }
}
