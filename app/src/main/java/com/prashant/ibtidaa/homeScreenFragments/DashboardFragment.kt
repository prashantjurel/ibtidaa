package com.prashant.ibtidaa.homeScreenFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.EpisodeListAdapter
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.SeasonListAdapter
import com.prashant.ibtidaa.R
import com.prashant.ibtidaa.Submission.SubmitActivity
import com.prashant.ibtidaa.common.loginSignup.SignUpScreenActivity
import com.prashant.ibtidaa.dataBinding.DashboardDataClass
import com.prashant.ibtidaa.databinding.FragmentDashBoardBinding
import com.prashant.ibtidaa.observer.DashboardObserver


class DashboardFragment : Fragment(), EpisodeListAdapter.OnNoteListener,
    SeasonListAdapter.OnNoteListener {

    private lateinit var binding: FragmentDashBoardBinding
    private lateinit var dashboardViewModel: DashboardViewModel

    private var adapter: RecyclerView.Adapter<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_dash_board, container, false
        )

        val headingObj = DashboardDataClass("Podcast","Season 2","Season 3")
        binding.dashboardHeading = headingObj
        val view = binding.root

        lifecycle.addObserver(DashboardObserver())
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        //onClick Functions for buttons
        binding.btnSubmit.setOnClickListener {
            val intent = Intent(activity, SubmitActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignup.setOnClickListener {
            val intent = Intent(activity, SignUpScreenActivity::class.java)
            startActivity(intent)
        }

        //calls to Recyclers
        podcastRecycler(view)
        return view
    }

    //Recycler Views Functions
    private fun podcastRecycler(view: View) {
        binding.podcastRecycler.setHasFixedSize(true)
        binding.podcastRecycler.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        dashboardViewModel.addPodcastList()
        adapter = SeasonListAdapter(dashboardViewModel.podcastList, this, "podcast")
        binding.podcastRecycler.adapter = adapter
    }


    override fun onClickNote(position: Int, tag: String, view: View) {
        when (tag) {
            "podcast" -> {
                dashboardViewModel.podcastList[position]
                val seasonEpisodeListFragment: Fragment = SeasonEpisodeListFragment()
                val bundle = Bundle()
                bundle.putString("AlbumArt", dashboardViewModel.podcastList[position].seasonAlbumArt)
                bundle.putString("Title", dashboardViewModel.podcastList[position].title)
                bundle.putString("Author", dashboardViewModel.podcastList[position].author)
                seasonEpisodeListFragment.arguments = bundle
                parentFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, seasonEpisodeListFragment).commit()
            }
        }
    }
}