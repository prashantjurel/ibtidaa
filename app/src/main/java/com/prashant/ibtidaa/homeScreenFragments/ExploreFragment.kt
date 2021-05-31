package com.prashant.ibtidaa.homeScreenFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.prashant.ibtidaa.R
import com.prashant.ibtidaa.exploreFragments.GhazalFragment

class ExploreFragment : Fragment() {

    private lateinit var ghazalLayout: RelativeLayout


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater.inflate(R.layout.fragment_explore, container, false)
        ghazalLayout = view.findViewById(R.id.explore_ghazal_layout)

        ghazalLayout.setOnClickListener(View.OnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GhazalFragment.newInstance()).commit()
        })
        return view
    }


}