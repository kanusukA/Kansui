package com.example.kasui.viewmodels

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.Player.EVENT_MEDIA_ITEM_TRANSITION
import androidx.media3.common.Player.EVENT_TRACKS_CHANGED
import androidx.media3.common.text.CueGroup
import androidx.media3.exoplayer.ExoPlayer
import com.example.kasui.Data.structure.song.Track
import com.example.kasui.viewmodels.PlayerListener._currentTrackId
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.time.Duration.Companion.milliseconds

object PlayerListener : Player.Listener {

    private var _playerState: MutableStateFlow<PlayerStates> = MutableStateFlow(PlayerStates.IDLE)
    val playerState = _playerState.asStateFlow()


    private var _currentTrackId = MutableStateFlow("")
    val currentTrackId = _currentTrackId.asStateFlow()


    private var _trackQueue = MutableStateFlow<List<MediaItem>>(emptyList())
    val trackQueue = _trackQueue.asStateFlow()
    

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        throw error
    }


    override fun onEvents(player: Player, events: Player.Events) {
        super.onEvents(player, events)
        if (player.isPlaying) {
            _playerState.update { PlayerStates.PLAYING }
        } else if (player.isLoading) {
            _playerState.update { PlayerStates.LOADING }
        } else {
            _playerState.update { PlayerStates.PAUSED }
        }

        val items = player.mediaItemCount
        val mediaItems = mutableListOf<MediaItem>()
        for (index in 0..<items) {
            mediaItems.add(player.getMediaItemAt(index))
        }
        _trackQueue.update { mediaItems.toList() }

        if (events.contains(EVENT_MEDIA_ITEM_TRANSITION)) {
            _currentTrackId.update { mediaItems[player.currentMediaItemIndex].mediaId }
        }


//        if (player.mediaItemCount > player.currentMediaItemIndex) {
//            _currentTrackId.update { player.getMediaItemAt(player.currentMediaItemIndex).mediaId }
//        }


    }


}

object Player {

    private var _exoPlayer = MutableStateFlow<ExoPlayer?>(null)
    val exoPlayer = _exoPlayer.asStateFlow()


    val progress: Flow<Float> = flow {
        while (true) {
            if (exoPlayer.value?.contentDuration != null && exoPlayer.value!!.contentDuration > 0) {
                val prog = ((exoPlayer.value?.currentPosition?.toFloat()
                    ?: 0f) / exoPlayer.value!!.contentDuration.toFloat())
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
        val sec = (((exoPlayer.value?.contentDuration ?: 1) - (exoPlayer.value?.currentPosition
            ?: 1)) / 1000 -
                ((((exoPlayer.value?.contentDuration ?: 1) - (exoPlayer.value?.currentPosition
                    ?: 1)) / 60000).toInt() * 60)).coerceIn(
            0,
            60
        )
        if (sec < 10) {
            "${
                (((exoPlayer.value?.contentDuration ?: 0) - (exoPlayer.value?.currentPosition ?: 0)) / 60000).coerceIn(
                    0,
                    999
                )
            }:" + "0${sec}"
        } else {
            "${
                (((exoPlayer.value?.contentDuration ?: 0) - (exoPlayer.value?.currentPosition ?: 0)) / 60000).coerceIn(
                    0,
                    999
                )
            }:" + "$sec"
        }

    }

    fun initPlayer(context: Context) {
        _exoPlayer.update {
            ExoPlayer.Builder(context).build()
        }
        _exoPlayer.value?.addListener(PlayerListener)
        _exoPlayer.value?.playWhenReady = false

    }

    fun setTrackAtHead(track: Track) {
        if (exoPlayer.value?.mediaItemCount == 0) {
            _exoPlayer.value?.setMediaItem(trackToMediaItem(track))
            _exoPlayer.value?.prepare()
        } else {
            _exoPlayer.value?.stop()
            _exoPlayer.value?.replaceMediaItem(0, trackToMediaItem(track))
            _exoPlayer.value?.prepare()
        }

    }

    // Prepare once all tracks are added
    fun addTrackToQueue(track: Track) {
        _exoPlayer.value?.addMediaItem(trackToMediaItem(track))
    }

    private fun trackToMediaItem(track: Track): MediaItem {
        return MediaItem.Builder().setMediaId(track.id.toString())
            .setUri(track.uri)
            .build()
    }

    fun clearQueue() {
        _exoPlayer.value?.clearMediaItems()
    }

    fun seekToMediaItem(index: Int) {
        _exoPlayer.value?.seekTo(index, 0)
    }

    fun onNext() {
        if (_exoPlayer.value?.hasNextMediaItem() ?: false) {
            _exoPlayer.value?.seekToNextMediaItem()
        }
    }

    fun onPrevious() {
        if (_exoPlayer.value?.hasPreviousMediaItem() ?: false) {
            _exoPlayer.value?.seekToPreviousMediaItem()
        }
    }

    fun getSeekProgressToProgressText(progress: Float): String {
        val duration = (exoPlayer.value?.contentDuration ?: 1)
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
        _exoPlayer.value?.seekTo((((_exoPlayer.value?.contentDuration ?: 0L) * progress).toLong()))
    }

    fun removeTrackAt(index: Int) {
        _exoPlayer.value?.removeMediaItem(index)
    }

//    private fun setMediaItem(track: Track) {
//        clearQueue()
////        _trackQueue.update { listOf(trackToMediaItem(track)) }
//        _exoPlayer.value?.setMediaItem(trackToMediaItem(track))
//    }
//
//    private fun replaceMediaItem(track: Track, pos: Int) {
////        _trackQueue.update {
////            it.toMutableList().apply { add(pos, trackToMediaItem(track)) }.toList()
////        }
//        _exoPlayer.value?.replaceMediaItem(pos, trackToMediaItem(track))
//    }
//
//    private fun addMediaToQueue(track: Track) {
////        _trackQueue.update { it.toMutableList().apply { add(trackToMediaItem(track)) }.toList() }
//        _exoPlayer.value?.addMediaItem()
//    }
//
//    fun clearQueue() {
//        _trackQueue.update { emptyList() }
//    }

    fun play() {
        _exoPlayer.value?.play()
    }

    fun pause() {
        _exoPlayer.value?.pause()
    }

    fun stop() {
        _exoPlayer.value?.stop()
    }

    fun prepare() {
        _exoPlayer.value?.prepare()
    }

}

enum class PlayerStates {
    LOADING,
    IDLE,
    PLAYING,
    PAUSED
}