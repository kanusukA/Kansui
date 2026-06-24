package com.example.kasui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kasui.Data.request.MediaManager
import com.example.kasui.Presentation.NavScreen
import com.example.kasui.databinding.ActivityMainBinding
import com.example.kasui.viewmodels.MainViewModel
import kotlinx.coroutines.launch

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
        supportActionBar?.hide()

        checkAudioPermission()





        enableEdgeToEdge()

        setContent {
            val mainViewModel: MainViewModel = viewModel()
            

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