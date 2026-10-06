package com.example.kasui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.databinding.ActivityMainBinding
import com.example.kasui.MainActivity
import com.example.kasui.Data.MusicBrainZ.musicBrainz
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Presentation.NavScreen
import com.example.kasui.Presentation.screens.home.WelcomeScreen

import com.example.kasui.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val permissionForAudio = MutableStateFlow(false)

    private val audioPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    // 2. Register the permission launcher callback
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionForAudio.update { isGranted }
    }

    fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                audioPermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(audioPermission)

        } else {
            permissionForAudio.update { true }
        }
    }

    @RequiresExtension(extension = Build.VERSION_CODES.TIRAMISU, version = 15)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        supportActionBar?.hide()

        checkAudioPermission()

        enableEdgeToEdge()

        setContent {

            val mainViewModel: MainViewModel = viewModel()

            // fetch and tag when permission is granted
            val permission by permissionForAudio.collectAsStateWithLifecycle()

            LaunchedEffect(permission) {
                if (permission) {
                    mainViewModel.initMain(applicationContext)
                }
            }





            val coroutineScope = rememberCoroutineScope()


            //WelcomeScreen()
            NavScreen(mainViewModel)
        }


    }

    /**
     * A native method that is implemented by the 'kasui' native library,
     * which is packaged with this application.
     */
//    external fun stringFromJNI(): String
//
//    companion object {
//        // Used to load the 'kasui' library on application startup.
//        init {
//            System.loadLibrary("kasui")
//        }
//    }
}

object TagLib {

    external fun stringFromJNI(fd: IntArray, id: LongArray): List<Map<String, String>>

    init {
        System.loadLibrary("kasui")
    }
}