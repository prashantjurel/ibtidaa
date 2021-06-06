package com.prashant.ibtidaa.exoPlayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.*
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.prashant.ibtidaa.data.remote.EpisodeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicSource @Inject constructor(
    private val episodeDatabase: EpisodeDatabase
){

    var episodes = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData() = withContext(Dispatchers.IO){
        state = State.STATE_INITIALIZING
        val allEpisodes = episodeDatabase.getAllEpisodes()
        episodes = allEpisodes.map{ episodeInfo ->
        MediaMetadataCompat.Builder()
            .putString(METADATA_KEY_ARTIST,episodeInfo.author)
            .putString(METADATA_KEY_MEDIA_ID, episodeInfo.id)
            .putString(METADATA_KEY_TITLE, episodeInfo.title)
            .putString(METADATA_KEY_DISPLAY_TITLE, episodeInfo.title)
            .putString(METADATA_KEY_DISPLAY_ICON_URI, episodeInfo.albumArt)
            .putString(METADATA_KEY_MEDIA_URI, episodeInfo.audio)
            .putString(METADATA_KEY_ALBUM_ART_URI, episodeInfo.albumArt)
            .putString(METADATA_KEY_DISPLAY_SUBTITLE, episodeInfo.author)
            .putString(METADATA_KEY_DISPLAY_DESCRIPTION, episodeInfo.author)
            .build()
        }
        state = State.STATE_INITIALIZED
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        episodes.forEach { song ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(song.getString(METADATA_KEY_MEDIA_URI)))
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = episodes.map { episode ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(episode.getString(METADATA_KEY_MEDIA_URI).toUri())
            .setTitle(episode.description.title)
            .setSubtitle(episode.description.subtitle)
            .setMediaId(episode.description.mediaId)
            .setIconUri(episode.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }

    private val onReadyListeners = mutableListOf<(Boolean) -> Unit>()

    private var state: State = State.STATE_CREATED
        set(value) {
            if (value == State.STATE_INITIALIZED || value == State.STATE_ERROR) {
                synchronized(onReadyListeners) {
                    field = value
                    onReadyListeners.forEach { listener ->
                        listener(state == State.STATE_INITIALIZED)
                    }
                }
            }
            else{
                field = value
            }
        }

    fun whenReady(action:(Boolean)-> Unit):Boolean{
        if (state == State.STATE_CREATED || state == State.STATE_INITIALIZING){
            onReadyListeners +=action
            return false
        }else{
            action(state == State.STATE_INITIALIZED)
            return true
        }
    }
}

enum class State {
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}