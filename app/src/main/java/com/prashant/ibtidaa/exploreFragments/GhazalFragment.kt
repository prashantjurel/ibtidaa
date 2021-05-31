package com.prashant.ibtidaa.exploreFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prashant.ibtidaa.R

class GhazalFragment : Fragment() {

    private lateinit var ghazalRecyler: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<GhazalListAdapter.GhazalListHelperViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_ghazal, container, false)

        ghazalRecyler = view.findViewById(R.id.ghazalRecycler)

        ghazalRecycler(view)
        return view
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            GhazalFragment().apply {
            }
    }

    private fun ghazalRecycler(view: View) {

        var ghazalList: ArrayList<GhazalListHelper> = arrayListOf(GhazalListHelper(
            "https://instagram.fixc10-1.fna.fbcdn.net/v/t51.2885-15/e35/128932174_2857086487947834_7846413166768391185_n.jpg?tp=1&_nc_ht=instagram.fixc10-1.fna.fbcdn.net&_nc_cat=106&_nc_ohc=qL4EzzVfP5oAX-bH6M2&edm=AP_V10EBAAAA&ccb=7-4&oh=345bd36c2ebb4bf9dd733c6f01a6f1ed&oe=60BC6F7A&_nc_sid=4f375e"
        ),GhazalListHelper(
            "https://instagram.fdel25-1.fna.fbcdn.net/v/t51.2885-15/sh0.08/e35/s750x750/173834783_207984024433278_433719607886915295_n.jpg?tp=1&_nc_ht=instagram.fdel25-1.fna.fbcdn.net&_nc_cat=106&_nc_ohc=WTgJbB5L380AX8Wfgkk&edm=AP_V10EBAAAA&ccb=7-4&oh=05b706da7e79a1479f68f49ad689a171&oe=60BBA300&_nc_sid=4f375e"
        ),GhazalListHelper(
            "https://instagram.fdel25-1.fna.fbcdn.net/v/t51.2885-15/e35/177505866_520614279114271_8043563651372430672_n.jpg?tp=1&_nc_ht=instagram.fdel25-1.fna.fbcdn.net&_nc_cat=107&_nc_ohc=WfTJp4xM7IoAX-uhKCS&edm=AP_V10EBAAAA&ccb=7-4&oh=85dc23f4ed8fd3c1bfeb2e455df435a2&oe=60BCCBE1&_nc_sid=4f375e"))
        ghazalRecyler.setHasFixedSize(true)
        ghazalRecyler.layoutManager = LinearLayoutManager(
            view.context,
            LinearLayoutManager.VERTICAL,
            false
        )

        ghazalList.add(GhazalListHelper(
            "https://instagram.fdel25-1.fna.fbcdn.net/v/t51.2885-15/e35/185274100_770161977019159_8211843113762684922_n.jpg?tp=1&_nc_ht=instagram.fdel25-1.fna.fbcdn.net&_nc_cat=107&_nc_ohc=ndeq722WGoAAX9OvHJK&tn=NP1Q5KSbswEQwFo-&edm=AP_V10EBAAAA&ccb=7-4&oh=25eb5baecc4627862d6ff9dad134ec9f&oe=60BBB55A&_nc_sid=4f375e"))
        adapter =
            GhazalListAdapter(ghazalList)
        ghazalRecyler.adapter = adapter
    }

}