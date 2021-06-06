package com.prashant.ibtidaa.homeScreenFragments

import androidx.lifecycle.ViewModel
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.SeasonListHelper
import java.util.*

class DashboardViewModel : ViewModel() {

    var podcastList = ArrayList<SeasonListHelper>()

    fun addPodcastList() {
        podcastList.add(SeasonListHelper("https://i.imgur.com/4pILS2t.png", "Saaz", "Season 1"))
        podcastList.add(
            SeasonListHelper(
                "https://i.imgur.com/GgcAnOP.png",
                "Saaz, Manik, Mitali and more",
                "Season 2"
            )
        )
        podcastList.add(SeasonListHelper("https://i.imgur.com/FrxTj2N.png", "Saaz", "Season 3"))
    }

}