package com.prashant.ibtidaa.homeScreenFragments

import androidx.lifecycle.ViewModel
import com.prashant.ibtidaa.HelperClasses.HomeAdapter.EpisodeListHelper
import java.util.*

class SeasonEpisodeListViewModel (): ViewModel() {

    var episodeList = ArrayList<EpisodeListHelper>()

    fun seasonEpisodeList(seasonValue:String) {
        if (seasonValue == "season2"){
        episodeList.add(
            EpisodeListHelper(
                "Yaar Bas",
                "Ziya Zameer | Saaz",
                "https://i.imgur.com/AN4njxw.jpg"
            )
        )
        episodeList.add(
            EpisodeListHelper(
                "Suna Hai Log Usse",
                "Ahmed Faraz | Manik ",
                "https://i.imgur.com/CnlrwGC.jpg"
            )
        )
        episodeList.add(
            EpisodeListHelper(
                "Wo Humsafar Tha Magar",
                "Naseer Turaabi | Mitali Tiwari",
                "https://i.imgur.com/yeFW7dc.jpg"
            )
        )
        episodeList.add(
            EpisodeListHelper(
                "Humesha Der Kar Deta Hoon Main",
                "Muneer Niyazi | Ashoka",
                "https://i.imgur.com/Z96ynqa.jpg"
            )
        )
        episodeList.add(
            EpisodeListHelper(
                "Tum Yaad Aa Gae",
                "Anjum rahbar | Anukriti",
                "https://i.imgur.com/uZGygEM.jpg"
            )
        )
        episodeList.add(
            EpisodeListHelper(
                "Ab Aaram Bhi Nahi'n Aata",
                "Ghulam Muhammad Qaasir | Saaz",
                "https://i.imgur.com/pDTttY0.jpg"
            )
        )
        episodeList.add(
            EpisodeListHelper(
                "Zameeno'n Ki Taraf Chalne Lage",
                "Ameer Imam | Saaz",
                "https://i.imgur.com/yKEUfLZ.jpg"
            )
        )
        episodeList.add(
            EpisodeListHelper(
                "Tere Sivaa Kuch Bhi Nahi'n",
                "Bashir Badr | Roohvaani",
                "https://i.imgur.com/RT24NDz.jpg"
            )
        )
        episodeList.add(
            EpisodeListHelper(
                "Ye Kabhi Socha Na Tha",
                "Adeem Hashmi | Saaz",
                "https://i.imgur.com/2itCg62.jpg"
            )
        )
        episodeList.add(
            EpisodeListHelper(
                "Koshish Ke Bawajood Bhi",
                "Khurram Afaq | Saaz",
                "https://i.imgur.com/dEJPWGu.jpg"
            )
        )

    }
        if (seasonValue == "season3"){

        }



    }

}