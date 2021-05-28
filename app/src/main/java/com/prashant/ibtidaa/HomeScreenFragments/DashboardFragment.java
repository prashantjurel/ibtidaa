package com.prashant.ibtidaa.HomeScreenFragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.EpisodeListAdapter;
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.EpisodeListHelper;
import com.prashant.ibtidaa.MusicPlayer.MusicPlayer;
import com.prashant.ibtidaa.R;
import com.prashant.ibtidaa.Submission.SubmitActivity;
import com.prashant.ibtidaa.common.loginSignup.SignUpScreenActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements EpisodeListAdapter.OnNoteListener {
    private RecyclerView seasonOneRecycler, seasonTwoRecycler, seasonThreeRecycler;
    private RecyclerView.Adapter adapter;
    private ImageButton btnSubmit, btnSignUp;
    private BottomNavigationView bottomNavigationView;
    private int currentApiVersion;

    ArrayList<EpisodeListHelper> episodesListSeason1 = new ArrayList<>();
    ArrayList<EpisodeListHelper> episodesListSeason2 = new ArrayList<>();
    ArrayList<EpisodeListHelper> episodesListSeason3 = new ArrayList<>();

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
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT) {
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
        View view = inflater.inflate(R.layout.activity_dash_board, container, false);

        //Hooks
        btnSubmit = view.findViewById(R.id.btnSubmit);
        seasonOneRecycler = view.findViewById(R.id.season_one_recycler);
        seasonTwoRecycler = view.findViewById(R.id.season_two_recycler);
        seasonThreeRecycler = view.findViewById(R.id.season_three_recycler);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        btnSignUp = view.findViewById(R.id.btnSingnUp);

        /*// Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("AlbumArts");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
*/
        //Submit Button CLick Event
       /* submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        view.getContext(), R.style.BottomSheetDialogeTheme);

                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.activity_submit,
                        (RelativeLayout)view.findViewById(R.id.bottom_sheet_container));

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                *//*Fragment fragment = new SubmitFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*//*
            }
        });*/

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SubmitActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SignUpScreenActivity.class);
                startActivity(intent);
            }
        });

        //calls to Recyclers
        seasonOneRecycler(view);
        seasonTwoRecycler(view);
        seasonThreeRecycler(view);
        return view;
    }
    //Recycler Views Functions
    private void seasonOneRecycler(View view) {
        seasonOneRecycler.setHasFixedSize(true);
        seasonOneRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false));

        //SEASON 1
        episodesListSeason1.add(new EpisodeListHelper("Muhabbat Kyun Nahi Karte", "Farhat Ehsaas |Saaz", "https://i.imgur.com/TnHWsAI.jpg"));
        episodesListSeason1.add(new EpisodeListHelper("Jis Ladki se Soch Humari Milti hai", "Fakhr Abbas । Saaz", "https://i.imgur.com/62gN9ur.jpeg"));
        episodesListSeason1.add(new EpisodeListHelper("Bichadte Waqt Rona Chahiye Tha", "Fehmi Badayuni | Saaz", "https://i.imgur.com/VK4LfjS.jpg"));
        episodesListSeason1.add(new EpisodeListHelper("Main Zinda Hoon", "Ahmad Farhad | Saaz", "https://i.imgur.com/cUi3Hbu.jpg"));
        episodesListSeason1.add(new EpisodeListHelper("Farz Karo", "Ibn-e-Insha | Saaz", "https://i.imgur.com/H3HyJVF.jpg"));

        adapter = new EpisodeListAdapter(episodesListSeason1,this,"season1");
        seasonOneRecycler.setAdapter(adapter);
    }

    private void seasonTwoRecycler(View view) {
        seasonTwoRecycler.setHasFixedSize(true);
        seasonTwoRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false));
        //SEASON 2
        episodesListSeason2.add(new EpisodeListHelper("Yaar Bas", "Ziya Zameer | Saaz", "https://i.imgur.com/AN4njxw.jpg"));
        episodesListSeason2.add(new EpisodeListHelper("Suna Hai Log Usse", "Ahmed Faraz | Manik ", "https://i.imgur.com/CnlrwGC.jpg"));
        episodesListSeason2.add(new EpisodeListHelper("Wo Humsafar Tha Magar", "Naseer Turaabi | Mitali Tiwari", "https://i.imgur.com/yeFW7dc.jpg"));
        episodesListSeason2.add(new EpisodeListHelper("Humesha Der Kar Deta Hoon Main", "Muneer Niyazi | Ashoka", "https://i.imgur.com/Z96ynqa.jpg"));
        episodesListSeason2.add(new EpisodeListHelper("Tum Yaad Aa Gae", "Anjum rahbar | Anukriti", "https://i.imgur.com/uZGygEM.jpg"));
        episodesListSeason2.add(new EpisodeListHelper("Ab Aaram Bhi Nahi'n Aata", "Ghulam Muhammad Qaasir | Saaz", "https://i.imgur.com/pDTttY0.jpg"));
        episodesListSeason2.add(new EpisodeListHelper("Zameeno'n Ki Taraf Chalne Lage", "Ameer Imam | Saaz", "https://i.imgur.com/yKEUfLZ.jpg"));
        episodesListSeason2.add(new EpisodeListHelper("Tere Sivaa Kuch Bhi Nahi'n", "Bashir Badr | Roohvaani", "https://i.imgur.com/RT24NDz.jpg"));
        episodesListSeason2.add(new EpisodeListHelper("Ye Kabhi Socha Na Tha", "Adeem Hashmi | Saaz", "https://i.imgur.com/2itCg62.jpg"));
        episodesListSeason2.add(new EpisodeListHelper("Koshish Ke Bawajood Bhi", "Khurram Afaq | Saaz", "https://i.imgur.com/dEJPWGu.jpg"));

        adapter = new EpisodeListAdapter(episodesListSeason2,this,"season2");
        seasonTwoRecycler.setAdapter(adapter);
    }

    private void seasonThreeRecycler(View view) {

        seasonThreeRecycler.setHasFixedSize(true);
        seasonThreeRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL, false));

        //Season3
        episodesListSeason3.add(new EpisodeListHelper("Yaar Bas", "Ziya Zameer | Saaz", "https://i.imgur.com/AN4njxw.jpg"));
        episodesListSeason3.add(new EpisodeListHelper("Suna Hai Log Usse", "Ahmed Faraz | Manik ", "https://i.imgur.com/CnlrwGC.jpg"));
        episodesListSeason3.add(new EpisodeListHelper("Wo Humsafar Tha Magar", "Naseer Turaabi | Mitali Tiwari", "https://i.imgur.com/yeFW7dc.jpg"));
        episodesListSeason3.add(new EpisodeListHelper("Humesha Der Kar Deta Hoon Main", "Muneer Niyazi | Ashoka", "https://i.imgur.com/Z96ynqa.jpg"));
        episodesListSeason3.add(new EpisodeListHelper("Tum Yaad Aa Gae", "Anjum rahbar | Anukriti", "https://i.imgur.com/uZGygEM.jpg"));
        episodesListSeason3.add(new EpisodeListHelper("Ab Aaram Bhi Nahi'n Aata", "Ghulam Muhammad Qaasir | Saaz", "https://i.imgur.com/pDTttY0.jpg"));
        episodesListSeason3.add(new EpisodeListHelper("Zameeno'n Ki Taraf Chalne Lage", "Ameer Imam | Saaz", "https://i.imgur.com/yKEUfLZ.jpg"));
        episodesListSeason3.add(new EpisodeListHelper("Tere Sivaa Kuch Bhi Nahi'n", "Bashir Badr | Roohvaani", "https://i.imgur.com/RT24NDz.jpg"));
        episodesListSeason3.add(new EpisodeListHelper("Ye Kabhi Socha Na Tha", "Adeem Hashmi | Saaz", "https://i.imgur.com/2itCg62.jpg"));
        episodesListSeason3.add(new EpisodeListHelper("Koshish Ke Bawajood Bhi", "Khurram Afaq | Saaz", "https://i.imgur.com/dEJPWGu.jpg"));

        adapter = new EpisodeListAdapter(episodesListSeason3,this,"season3");
        seasonThreeRecycler.setAdapter(adapter);
    }
    @Override
    public void onClickNote(int position,String tag,View view) {
        switch(tag){
            case "season1":
                episodesListSeason1.get(position);
                Intent intent1 = new Intent(getContext(), MusicPlayer.class);
                intent1.putExtra("Title",episodesListSeason1.get(position).getTitle());
                intent1.putExtra("Author",episodesListSeason1.get(position).getAuthor());
                intent1.putExtra("ImageUrl",episodesListSeason1.get(position).getImageUrl());
                startActivity(intent1);

                /*Bundle bundle = new Bundle();
                bundle.putString("Title",episodesListSeason1.get(position).getTitle());
                bundle.putString("Author",episodesListSeason1.get(position).getAuthor());
                bundle.putString("ImageUrl",episodesListSeason1.get(position).getImageUrl());
                Fragment episodeInfoFragment = new EpisodeInfoFragment();
                episodeInfoFragment.setArguments(bundle);
                episodeButtomPopUpDialog(view);*/

                break;
            case "season2":
                episodesListSeason2.get(position);
                Intent intent2 = new Intent(getContext(), MusicPlayer.class);
                intent2.putExtra("Title",episodesListSeason2.get(position).getTitle());
                intent2.putExtra("Author",episodesListSeason2.get(position).getAuthor());
                intent2.putExtra("ImageUrl",episodesListSeason2.get(position).getImageUrl());
                startActivity(intent2);
                break;
            case "season3":
                episodesListSeason3.get(position);
                Intent intent3 = new Intent(getContext(), MusicPlayer.class);
                intent3.putExtra("Title",episodesListSeason3.get(position).getTitle());
                intent3.putExtra("Author",episodesListSeason3.get(position).getAuthor());
                intent3.putExtra("ImageUrl",episodesListSeason3.get(position).getImageUrl());
                startActivity(intent3);
                break;
        }

    }

    private void episodeButtomPopUpDialog(View view) {

                /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, episodeInfoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        view.getContext(), R.style.BottomSheetDialogeTheme);
                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_episode_info,
                        (RelativeLayout)view.findViewById(R.id.bottom_sheet_container));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
               /* Intent intent = new Intent(getContext(), MusicPlayer.class);
                intent.putExtra("Title",episodesListSeason1.get(position).getTitle());
                intent.putExtra("Author",episodesListSeason1.get(position).getAuthor());
                intent.putExtra("ImageUrl",episodesListSeason1.get(position).getImageUrl());
                startActivity(intent);*/
    }

}
