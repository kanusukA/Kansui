package com.example.kasui.viewmodels

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import com.example.kasui.Data.structure.song.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.milliseconds


class PlayerViewModel(
    val mainViewModel: MainViewModel,

    ) : ViewModel() {

    var playerController: MediaController? = null

    fun currentMediaItemIndex(): Int? {
        return playerController?.currentMediaItemIndex
    }

    val trackQueue = mainViewModel.dbTracks.combine(PlayerListener.trackQueue) { tracks, ids ->
        ids.mapNotNull { id -> tracks.find { it.id.toString() == id.mediaId } }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList()
    )


    val currentTrack = PlayerListener.currentTrackId.map { id ->
        val track = trackQueue.value.firstOrNull { it.id.toString() == id }
        track
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val currentAlbum = currentTrack.map { track ->
        mainViewModel.dbAlbums.value.find { it.album.id == track?.albumId }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )


    val progressMs: Flow<Long> = flow {
        while (true) {
            emit(playerController?.currentPosition ?: 0L)
            delay(500.milliseconds)
        }
    }


    val progress: Flow<Float> = flow {
        while (true) {
            if (playerController?.contentDuration != null && playerController!!.contentDuration > 0) {
                val prog = ((playerController?.currentPosition?.toFloat()
                    ?: 0f) / playerController!!.contentDuration.toFloat())
                emit(
                    prog
                )
            } else {
                emit(0f)
            }
            delay(500.milliseconds)
        }
    }

    val ProgressText = progress.map {
        val sec = (((playerController?.contentDuration ?: 1) - (playerController?.currentPosition
            ?: 1)) / 1000 -
                ((((playerController?.contentDuration ?: 1) - (playerController?.currentPosition
                    ?: 1)) / 60000).toInt() * 60)).coerceIn(
            0,
            60
        )
        if (sec < 10) {
            "${
                (((playerController?.contentDuration ?: 0) - (playerController?.currentPosition ?: 0)) / 60000).coerceIn(
                    0,
                    999
                )
            }:" + "0${sec}"
        } else {
            "${
                (((playerController?.contentDuration ?: 0) - (playerController?.currentPosition ?: 0)) / 60000).coerceIn(
                    0,
                    999
                )
            }:" + "$sec"
        }

    }

    fun setTrackAtHead(track: Track) {
        if (playerController?.mediaItemCount == 0) {
            playerController?.setMediaItem(trackToMediaItem(track))
            playerController?.prepare()
        } else {
            playerController?.stop()
            playerController?.replaceMediaItem(0, trackToMediaItem(track))
            playerController?.prepare()
        }

    }

    // Prepare once all tracks are added
    fun addTrackToQueue(track: Track) {
        playerController?.addMediaItem(trackToMediaItem(track))
    }

    private fun trackToMediaItem(track: Track): MediaItem {
        return MediaItem.Builder().setMediaId(track.id.toString())
            .setUri(track.uri)
            .build()
    }

    fun clearQueue() {
        playerController?.clearMediaItems()
    }

    fun seekToMediaItem(index: Int) {
        playerController?.seekTo(index, 0)
    }

    fun onNext() {
        if (playerController?.hasNextMediaItem() ?: false) {
            playerController?.seekToNextMediaItem()
        }
    }

    fun onPrevious() {
        if (playerController?.hasPreviousMediaItem() ?: false) {
            playerController?.seekToPreviousMediaItem()
        }
    }

    fun getSeekProgressToProgressText(progress: Float): String {
        val duration = (playerController?.contentDuration ?: 1)
        val seekPosition = duration * progress
        val sec = ((duration - seekPosition) / 1000 -
                (((duration - seekPosition) / 60000).toInt() * 60)).toInt().coerceIn(
            0,
            60
        )
        return if (sec < 10) {
            "${
                ((duration - seekPosition) / 60000).toInt().coerceIn(
                    0,
                    999
                )
            }:" + "0${sec}"
        } else {
            "${
                ((duration - seekPosition) / 60000).toInt().coerceIn(
                    0,
                    999
                )
            }:" + "$sec"
        }
    }

    fun seekToProgress(progress: Float) {
        playerController?.seekTo((((playerController?.contentDuration ?: 0L) * progress).toLong()))
    }

    fun removeTrackAt(index: Int) {
        playerController?.removeMediaItem(index)
    }

    fun play() {
        playerController?.play()
    }

    fun pause() {
        playerController?.pause()
    }

    fun stop() {
        playerController?.stop()
    }

    fun prepare() {
        playerController?.prepare()
    }

}