package com.prashant.ibtidaa.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.prashant.ibtidaa.data.entities.EpisodeInfo
import com.prashant.ibtidaa.other.Constants.EPISODE_COLLECTION
import kotlinx.coroutines.tasks.await

class EpisodeDatabase {

    private val firestore = FirebaseFirestore.getInstance()
    private val episodeCollection = firestore.collection(EPISODE_COLLECTION)

    suspend fun getAllEpisodes(): List<EpisodeInfo>{
        return try {
            episodeCollection.get().await().toObjects(EpisodeInfo::class.java)
        }catch (e:Exception){
            emptyList<EpisodeInfo>()
        }
    }
}