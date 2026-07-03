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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.Data.MusicBrainZ.musicBrainz
import com.example.kasui.Presentation.NavScreen
import com.example.kasui.Presentation.screens.home.WelcomeScreen
import com.example.kasui.databinding.ActivityMainBinding
import com.example.kasui.viewmodels.MainViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.time.Duration.Companion.milliseconds

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val audioPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    // 2. Register the permission launcher callback
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            println("Permission Granted")
        } else {
            // Permission denied

        }
    }

    fun checkAudioPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                audioPermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(audioPermission)
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

            LaunchedEffect(Unit) {
                mainViewModel.initMain()
            }

            val coroutineScope = rememberCoroutineScope()

//            LaunchedEffect(Unit) {
//                delay(1500.milliseconds)
//                async {
//                    musicBrainz.searchReleases(
//                        "I let it in and it took everything", null,
//                        {
//                            println("Failed API REQUEST 0")
//                        },
//                        { result ->
//                            if (!result.releases.isNullOrEmpty()) {
//                                coroutineScope.launch {
////                                    musicBrainz.getRelease(
////                                        result.releases.get(1).id,
////                                        onFailure = {},
////                                        onSuccess = {}
////                                    )
//                                    musicBrainz.getReleaseCover(
//                                        result.releases.get(1).id,
//                                        onFailure = {},
//                                        onSuccess = { coverArtResponse ->
//                                            println(coverArtResponse)
//
//                                        }
//                                    )
//
//                                }
//
//
//                            }
//
//
//                        })
//                }
//
//
//            }
            //WelcomeScreen()
            NavScreen()
        }


//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Example of a call to a native method
//        binding.sampleText.text = stringFromJNI()


    }

    /**
     * A native method that is implemented by the 'kasui' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'kasui' library on application startup.
        init {
            System.loadLibrary("kasui")
        }
    }
}