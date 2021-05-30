package com.prashant.ibtidaa.HomeScreenFragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.EpisodeListAdapter;
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.EpisodeListHelper;
import com.prashant.ibtidaa.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class SeasonEpisodeListFragment extends Fragment implements EpisodeListAdapter.OnNoteListener {

    private MaterialButton shuffleBtn,playBtn;
    private ImageView albumArt,backBtn;
    private TextView seasonTitle, seasonAuthor;
    private RecyclerView episodeListRecycler;
    private ArrayList<EpisodeListHelper> episodeList = new ArrayList<>();
    private RecyclerView.Adapter adapter;

    private int currentApiVersion;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getActivity().getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
        View view = inflater.inflate(R.layout.fragment_season_episode_list, container, false);
        String albumArtUrl = getArguments().getString("AlbumArt");
        String titleTag = getArguments().getString("Title");
        String authorTag = getArguments().getString("Author");

        //Hooks
        shuffleBtn = view.findViewById(R.id.shuffle_btn);
        playBtn = view.findViewById(R.id.play_btn);
        backBtn = view.findViewById(R.id.episodeListView_back);
        episodeListRecycler = view.findViewById(R.id.podcast_episode_recycler);
        albumArt=view.findViewById(R.id.season_albumArt);
        seasonTitle = view.findViewById(R.id.season_title);
        seasonAuthor = view.findViewById(R.id.season_author);
        Picasso.get().load(albumArtUrl).into(albumArt);
        seasonTitle.setText(titleTag);
        seasonAuthor.setText("By: "+authorTag);
        seasonEpisodeListRecycler(view);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
            }
        });


        return view;
    }

    private void seasonEpisodeListRecycler(View view) {
        episodeListRecycler.setHasFixedSize(true);
        episodeListRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL, false));
        //SEASON 2
        episodeList.add(new EpisodeListHelper("Yaar Bas", "Ziya Zameer | Saaz", "https://i.imgur.com/AN4njxw.jpg"));
        episodeList.add(new EpisodeListHelper("Suna Hai Log Usse", "Ahmed Faraz | Manik ", "https://i.imgur.com/CnlrwGC.jpg"));
        episodeList.add(new EpisodeListHelper("Wo Humsafar Tha Magar", "Naseer Turaabi | Mitali Tiwari", "https://i.imgur.com/yeFW7dc.jpg"));
        episodeList.add(new EpisodeListHelper("Humesha Der Kar Deta Hoon Main", "Muneer Niyazi | Ashoka", "https://i.imgur.com/Z96ynqa.jpg"));
        episodeList.add(new EpisodeListHelper("Tum Yaad Aa Gae", "Anjum rahbar | Anukriti", "https://i.imgur.com/uZGygEM.jpg"));
        episodeList.add(new EpisodeListHelper("Ab Aaram Bhi Nahi'n Aata", "Ghulam Muhammad Qaasir | Saaz", "https://i.imgur.com/pDTttY0.jpg"));
        episodeList.add(new EpisodeListHelper("Zameeno'n Ki Taraf Chalne Lage", "Ameer Imam | Saaz", "https://i.imgur.com/yKEUfLZ.jpg"));
        episodeList.add(new EpisodeListHelper("Tere Sivaa Kuch Bhi Nahi'n", "Bashir Badr | Roohvaani", "https://i.imgur.com/RT24NDz.jpg"));
        episodeList.add(new EpisodeListHelper("Ye Kabhi Socha Na Tha", "Adeem Hashmi | Saaz", "https://i.imgur.com/2itCg62.jpg"));
        episodeList.add(new EpisodeListHelper("Koshish Ke Bawajood Bhi", "Khurram Afaq | Saaz", "https://i.imgur.com/dEJPWGu.jpg"));

        adapter = new EpisodeListAdapter(episodeList,this,"season2");
        episodeListRecycler.setAdapter(adapter);

    }

    @Override
    public void onClickNote(int position, String tag, View view) {

    }
}