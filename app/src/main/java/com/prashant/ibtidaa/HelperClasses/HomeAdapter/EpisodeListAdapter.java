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

import java.util.List;

public class EpisodeListAdapter extends RecyclerView.Adapter<EpisodeListAdapter.EpisodeListHelperViewHolder> {

    private List<EpisodeListHelper> episodeLists;
    private OnNoteListener mOnNoteListener;
    private String tag;

    public EpisodeListAdapter(List<EpisodeListHelper> episodeLists, OnNoteListener onNoteListener,String tag) {
        this.episodeLists = episodeLists;
        this.mOnNoteListener = onNoteListener;
        this.tag = tag;
    }

    @NonNull
    @Override
    public EpisodeListHelperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EpisodeListHelperViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.dashboard_cardview_layout,
                        parent,
                        false
                ),mOnNoteListener,tag
        );
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeListHelperViewHolder holder, int position) {
        Picasso.get().load(episodeLists.get(position).getImageUrl()).into(holder.albumArtRecycler);
        holder.textTitle.setText(episodeLists.get(position).getTitle());
        holder.textAuthor.setText(episodeLists.get(position).getAuthor());
    }



    @Override
    public int getItemCount() {
        return episodeLists.size();
    }

    static class EpisodeListHelperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView albumArtRecycler;
        private TextView textTitle,textAuthor;
        private OnNoteListener onNoteListener;
        private String tag;

        EpisodeListHelperViewHolder(@NonNull View itemView, OnNoteListener onNoteListener,String tag) {
            super(itemView);
            //Hooks
            albumArtRecycler = itemView.findViewById(R.id.albumArtRecycler);
            textTitle = itemView.findViewById(R.id.textTitle);
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

    public interface OnNoteListener{
        void onClickNote(int position, String tag,View view);
    }

}
