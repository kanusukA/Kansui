package com.example.kasui.viewmodels

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kasui.Data.request.MediaManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@RequiresExtension(extension = Build.VERSION_CODES.TIRAMISU, version = 15)
class MainViewModel(application: Application) : AndroidViewModel(application) {
    init {
        viewModelScope.launch(Dispatchers.IO) {
            MediaManager.fetchMusicFiles(application.applicationContext)
            MediaManager.syncAlbumLibraryWithMusicBrainZ()
        }
    }
}