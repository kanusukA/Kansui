package com.example.kasui.Data.LastFm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import kotlin.time.Duration.Companion.milliseconds


const val LastFmApi = "https://ws.audioscrobbler.com/2.0"

object LastFmManager {

    private val lockMutex = Mutex()
    private var _apiLock = false;

    private var lockCoroutine = CoroutineScope(Dispatchers.IO)

    private fun getLock() {
        lockCoroutine.launch {
            lockMutex.withLock {
                _apiLock = true
                delay(500.milliseconds)
                _apiLock = false
            }
        }
    }

    suspend fun initLastFm(username: String, password: String) {
        val client = OkHttpClient()
        if (_apiLock) {
            println("API LOCKED")
            return
        }
        getLock()

        val url = LastFmApi.toHttpUrlOrNull()?.newBuilder()
            

    }

}