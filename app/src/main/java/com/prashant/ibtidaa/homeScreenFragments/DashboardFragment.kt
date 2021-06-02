package com.prashant.ibtidaa.homeScreenFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.EpisodeListAdapter
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.EpisodeListHelper
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.SeasonListAdapter
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.SeasonListHelper
import com.prashant.ibtidaa.MusicPlayer.MusicPlayer
import com.prashant.ibtidaa.R
import com.prashant.ibtidaa.Submission.SubmitActivity
import com.prashant.ibtidaa.common.loginSignup.SignUpScreenActivity
import com.prashant.ibtidaa.observer.DashboardObserver
import java.util.*

class DashboardFragment : Fragment(), EpisodeListAdapter.OnNoteListener,
    SeasonListAdapter.OnNoteListener {
    private var podcastRecycler: RecyclerView? = null
    private var seasonTwoRecycler: RecyclerView? = null
    private var seasonThreeRecycler: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private lateinit var btnSubmit: ImageButton
    private lateinit var btnSignUp: ImageButton
    private var bottomNavigationView: BottomNavigationView? = null
    private var podcastList = ArrayList<SeasonListHelper>()
    private var episodesListSeason2 = ArrayList<EpisodeListHelper>()
    private var episodesListSeason3 = ArrayList<EpisodeListHelper>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dash_board, container, false)
        lifecycle.addObserver(DashboardObserver())

        //Hooks
        btnSubmit = view.findViewById(R.id.btnSubmit)
        podcastRecycler = view.findViewById(R.id.podcast_recycler)
        seasonTwoRecycler = view.findViewById(R.id.season_two_recycler)
        seasonThreeRecycler = view.findViewById(R.id.season_three_recycler)
        bottomNavigationView = view.findViewById(R.id.bottom_navigation)
        btnSignUp = view.findViewById(R.id.btnSingnUp)

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
                        view.getContext(), R.style.BottomSheetDialogTheme);

                View bottomSheetView = LayoutInflater.from(getContext()).inflate(R.layout.activity_submit,
                        (RelativeLayout)view.findViewById(R.id.bottom_sheet_container));

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                */
        /*Fragment fragment = new SubmitFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
        /*
            }
        });*/

        btnSubmit.setOnClickListener {
            val intent = Intent(activity, SubmitActivity::class.java)
            startActivity(intent)
        }
        btnSignUp.setOnClickListener {
            val intent = Intent(activity, SignUpScreenActivity::class.java)
            startActivity(intent)
        }

        //calls to Recyclers
        podcastRecycler(view)
        seasonTwoRecycler(view)
        seasonThreeRecycler(view)
        return view
    }

    //Recycler Views Functions
    private fun podcastRecycler(view: View) {
        podcastRecycler!!.setHasFixedSize(true)
        podcastRecycler!!.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        //SEASON 1
        podcastList.add(SeasonListHelper("https://i.imgur.com/4pILS2t.png", "Saaz", "Season 1"))
        podcastList.add(
            SeasonListHelper(
                "https://i.imgur.com/GgcAnOP.png",
                "Saaz, Manik, Mitali and more",
                "Season 2"
            )
        )
        podcastList.add(SeasonListHelper("https://i.imgur.com/FrxTj2N.png", "Saaz", "Season 3"))
        adapter = SeasonListAdapter(podcastList, this, "podcast")
        podcastRecycler!!.adapter = adapter
    }

    private fun seasonTwoRecycler(view: View) {
        seasonTwoRecycler!!.setHasFixedSize(true)
        seasonTwoRecycler!!.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        //SEASON 2
        episodesListSeason2.add(
            EpisodeListHelper(
                "Yaar Bas",
                "Ziya Zameer | Saaz",
                "https://i.imgur.com/AN4njxw.jpg"
            )
        )
        episodesListSeason2.add(
            EpisodeListHelper(
                "Suna Hai Log Usse",
                "Ahmed Faraz | Manik ",
                "https://i.imgur.com/CnlrwGC.jpg"
            )
        )
        episodesListSeason2.add(
            EpisodeListHelper(
                "Wo Humsafar Tha Magar",
                "Naseer Turaabi | Mitali Tiwari",
                "https://i.imgur.com/yeFW7dc.jpg"
            )
        )
        episodesListSeason2.add(
            EpisodeListHelper(
                "Humesha Der Kar Deta Hoon Main",
                "Muneer Niyazi | Ashoka",
                "https://i.imgur.com/Z96ynqa.jpg"
            )
        )
        episodesListSeason2.add(
            EpisodeListHelper(
                "Tum Yaad Aa Gae",
                "Anjum rahbar | Anukriti",
                "https://i.imgur.com/uZGygEM.jpg"
            )
        )
        episodesListSeason2.add(
            EpisodeListHelper(
                "Ab Aaram Bhi Nahi'n Aata",
                "Ghulam Muhammad Qaasir | Saaz",
                "https://i.imgur.com/pDTttY0.jpg"
            )
        )
        episodesListSeason2.add(
            EpisodeListHelper(
                "Zameeno'n Ki Taraf Chalne Lage",
                "Ameer Imam | Saaz",
                "https://i.imgur.com/yKEUfLZ.jpg"
            )
        )
        episodesListSeason2.add(
            EpisodeListHelper(
                "Tere Sivaa Kuch Bhi Nahi'n",
                "Bashir Badr | Roohvaani",
                "https://i.imgur.com/RT24NDz.jpg"
            )
        )
        episodesListSeason2.add(
            EpisodeListHelper(
                "Ye Kabhi Socha Na Tha",
                "Adeem Hashmi | Saaz",
                "https://i.imgur.com/2itCg62.jpg"
            )
        )
        episodesListSeason2.add(
            EpisodeListHelper(
                "Koshish Ke Bawajood Bhi",
                "Khurram Afaq | Saaz",
                "https://i.imgur.com/dEJPWGu.jpg"
            )
        )
        adapter = EpisodeListAdapter(episodesListSeason2, this, "season2")
        seasonTwoRecycler!!.adapter = adapter
    }

    private fun seasonThreeRecycler(view: View) {
        seasonThreeRecycler!!.setHasFixedSize(true)
        seasonThreeRecycler!!.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)

        //Season3
        episodesListSeason3.add(
            EpisodeListHelper(
                "Yaar Bas",
                "Ziya Zameer | Saaz",
                "https://i.imgur.com/AN4njxw.jpg"
            )
        )
        episodesListSeason3.add(
            EpisodeListHelper(
                "Suna Hai Log Usse",
                "Ahmed Faraz | Manik ",
                "https://i.imgur.com/CnlrwGC.jpg"
            )
        )
        episodesListSeason3.add(
            EpisodeListHelper(
                "Wo Humsafar Tha Magar",
                "Naseer Turaabi | Mitali Tiwari",
                "https://i.imgur.com/yeFW7dc.jpg"
            )
        )
        episodesListSeason3.add(
            EpisodeListHelper(
                "Humesha Der Kar Deta Hoon Main",
                "Muneer Niyazi | Ashoka",
                "https://i.imgur.com/Z96ynqa.jpg"
            )
        )
        episodesListSeason3.add(
            EpisodeListHelper(
                "Tum Yaad Aa Gae",
                "Anjum rahbar | Anukriti",
                "https://i.imgur.com/uZGygEM.jpg"
            )
        )
        episodesListSeason3.add(
            EpisodeListHelper(
                "Ab Aaram Bhi Nahi'n Aata",
                "Ghulam Muhammad Qaasir | Saaz",
                "https://i.imgur.com/pDTttY0.jpg"
            )
        )
        episodesListSeason3.add(
            EpisodeListHelper(
                "Zameeno'n Ki Taraf Chalne Lage",
                "Ameer Imam | Saaz",
                "https://i.imgur.com/yKEUfLZ.jpg"
            )
        )
        episodesListSeason3.add(
            EpisodeListHelper(
                "Tere Sivaa Kuch Bhi Nahi'n",
                "Bashir Badr | Roohvaani",
                "https://i.imgur.com/RT24NDz.jpg"
            )
        )
        episodesListSeason3.add(
            EpisodeListHelper(
                "Ye Kabhi Socha Na Tha",
                "Adeem Hashmi | Saaz",
                "https://i.imgur.com/2itCg62.jpg"
            )
        )
        episodesListSeason3.add(
            EpisodeListHelper(
                "Koshish Ke Bawajood Bhi",
                "Khurram Afaq | Saaz",
                "https://i.imgur.com/dEJPWGu.jpg"
            )
        )
        adapter = EpisodeListAdapter(episodesListSeason3, this, "season3")
        seasonThreeRecycler!!.adapter = adapter
    }

    override fun onClickNote(position: Int, tag: String, view: View) {
        when (tag) {
            "podcast" -> {
                podcastList[position]
                val seasonEpisodeListFragment: Fragment = SeasonEpisodeListFragment()
                val bundle = Bundle()
                bundle.putString("AlbumArt", podcastList[position].seasonAlbumArt)
                bundle.putString("Title", podcastList[position].title)
                bundle.putString("Author", podcastList[position].author)
                seasonEpisodeListFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, seasonEpisodeListFragment).commit()
            }
            "season2" -> {
                episodesListSeason2[position]
                val intent2 = Intent(context, MusicPlayer::class.java)
                intent2.putExtra("Title", episodesListSeason2[position].getTitle())
                intent2.putExtra("Author", episodesListSeason2[position].getAuthor())
                intent2.putExtra("ImageUrl", episodesListSeason2[position].getImageUrl())
                startActivity(intent2)
            }
            "season3" -> {
                episodesListSeason3[position]
                val intent3 = Intent(context, MusicPlayer::class.java)
                intent3.putExtra("Title", episodesListSeason3[position].getTitle())
                intent3.putExtra("Author", episodesListSeason3[position].getAuthor())
                intent3.putExtra("ImageUrl", episodesListSeason3[position].getImageUrl())
                startActivity(intent3)
            }
        }
    }

    private fun episodeBottomPopUpDialog(view: View) {

        /*FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, episodeInfoFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
        val bottomSheetDialog = BottomSheetDialog(
            view.context, R.style.BottomSheetDialogeTheme
        )
        val bottomSheetView = LayoutInflater.from(context).inflate(
            R.layout.fragment_episode_info,
            view.findViewById<View>(R.id.bottom_sheet_container) as RelativeLayout
        )
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
        /* Intent intent = new Intent(getContext(), MusicPlayer.class);
                intent.putExtra("Title",episodesListSeason1.get(position).getTitle());
                intent.putExtra("Author",episodesListSeason1.get(position).getAuthor());
                intent.putExtra("ImageUrl",episodesListSeason1.get(position).getImageUrl());
                startActivity(intent);*/
    }
}