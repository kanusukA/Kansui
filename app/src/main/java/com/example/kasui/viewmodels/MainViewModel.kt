package com.example.kasui.viewmodels

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.LastFm.LASTFM_STATE
import com.example.kasui.Data.LastFm.LastFmManager
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Presentation.NavManager
import com.example.kasui.Presentation.NavRoutes
import com.example.kasui.Presentation.WelcomeNavStage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.TIRAMISU, version = 15)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    val lastfmState = LastFmManager.lastFmState.map {
        if (it == LASTFM_STATE.SIGNED_IN && NavManager.navStates.value == NavRoutes.WelcomeLogin()) {
            NavManager.changeNavState(NavRoutes.WelcomeSetupAlbum())
        }
        it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        LASTFM_STATE.SIGNED_OUT
    )
    val username: StateFlow<String> = LastFmManager.username.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )
    val mediaManagerState = MediaManager.mediaState
    val rawAlbums = MediaManager.rawAlbumList

    val loginCoroutine = CoroutineScope(Dispatchers.IO)


    fun initMain() {
        viewModelScope.launch(Dispatchers.IO) {
            LastFmManager.loadSessionKey(application.applicationContext)

        }
        viewModelScope.launch(Dispatchers.IO) {
            MediaManager.fetchMusicFiles(application.applicationContext)
        }
    }


    fun loginLastFm(username: String, password: String) {
        if (lastfmState.value == LASTFM_STATE.SIGNED_IN) {
            println("ALREADY LOGGED IN")
            return
        }
        if (lastfmState.value == LASTFM_STATE.LOGGING_IN) {
            println("ALREADY ATTEMPTING A LOGIN")
        }

        loginCoroutine.launch {
            LastFmManager.initLastFm(context = application.applicationContext, username, password)
        }

    }


}




