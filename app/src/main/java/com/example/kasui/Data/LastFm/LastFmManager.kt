package com.example.kasui.Data.LastFm

import android.R.id.input
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.kasui.Data.local.LAST_API_KEY
import com.example.kasui.Data.local.LAST_API_SECRET
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okio.ByteString.Companion.encodeUtf8
import okio.IOException
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.text.trim
import kotlin.time.Duration.Companion.milliseconds


const val LastFmApi = "https://ws.audioscrobbler.com/2.0/"

private val Context.dataStore by preferencesDataStore(name = "user_settings_critical")

object LastFmManager {


    private val SESSION_KEY = stringPreferencesKey("session_key")


    @Serializable
    private class Session(
        val name: String,
        val key: String,
        val subscriber: Int
    )

    @Serializable
    private class SessionKey(
        val session: Session
    )

    private var _currentSession: MutableStateFlow<SessionKey?> = MutableStateFlow(null)

    val username = _currentSession.map { session ->
        session?.session?.name ?: ""
    }

    private var _lastFmState: MutableStateFlow<LASTFM_STATE> =
        MutableStateFlow(LASTFM_STATE.SIGNED_OUT)
    val lastFmState = _lastFmState.asStateFlow()

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


    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(this.toByteArray(Charsets.UTF_8))
        return digest.joinToString("") { "%02x".format(it) }
    }

    suspend fun saveSessionKey(context: Context, key: String) {
        context.dataStore.edit { preferences ->
            preferences[SESSION_KEY] = key
        }
    }

    suspend fun loadSessionKey(context: Context) {

        println("LOADING SESSION...")
        val session = context.dataStore.data.map { preferences ->
            preferences[SESSION_KEY]
        }
        session.collect { sessionString ->
            if (!sessionString.isNullOrEmpty()) {
                _currentSession.update {
                    Gson().fromJson(sessionString, SessionKey::class.java)
                }
                _lastFmState.update { LASTFM_STATE.SIGNED_IN }
            }
        }

        return

    }

    suspend fun initLastFm(context: Context, username: String, password: String) {

        _lastFmState.update { LASTFM_STATE.LOGGING_IN }

        val client = OkHttpClient()
        if (_apiLock) {
            println("API LOCKED")
            return
        }
        getLock()

        val api_sig =
            "api_key${
                LAST_API_KEY.encodeUtf8().utf8()
            }methodauth.getMobileSessionpassword${
                password.encodeUtf8().utf8()
            }username${username.trim().encodeUtf8().utf8()}${
                LAST_API_SECRET.encodeUtf8().utf8()
            }".md5()


        val form = FormBody.Builder()
            .add("api_key", LAST_API_KEY)
            .add("method", "auth.getMobileSession")
            .add("password", password)
            .add("username", username.trim())
            .add("api_sig", api_sig)
            .add("format", "json")
            .build()


        val url = LastFmApi.toHttpUrlOrNull()?.newBuilder()
        url?.scheme("https")


        val request = Request.Builder()
            .post(form)
            .url(url!!.build())
            .build()

        println(request.body)

        try {
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val result = response.body.string()
                _currentSession.update { Gson().fromJson(result, SessionKey::class.java) }
                saveSessionKey(context, result)
                println(_currentSession)
                _lastFmState.update { LASTFM_STATE.SIGNED_IN }
            } else {
                println("LAST FM LOGIN ATTEMPT FAILED : ${response.message} \n ${response.code} \n ${response.body.string()} ")
                _lastFmState.update { LASTFM_STATE.FAILED_LOGIN_USERNAME }
            }


        } catch (e: IOException) {
            println("NETWORK ERROR : \n ${e.stackTraceToString()}")
            _lastFmState.update { LASTFM_STATE.NETWORK_ERROR }
        } catch (e: Exception) {
            println("LOCAL ERROR : \n ${e.stackTraceToString()}")
            _lastFmState.update { LASTFM_STATE.NETWORK_ERROR }
        }
    }

    suspend fun fetchAlbumResults(
        title: String,
        artist: String? = null,
        limit: Int = 20
    ): LastFmSearchAlbum? {
        val client = OkHttpClient()

        if (_currentSession.value == null) {
            println("NO SESSION KEY FOUND!")
            return null
        }

        if (_apiLock) {
            println("API LOCKED")
            return null
        }
        getLock()

        val url = LastFmApi.toHttpUrlOrNull()?.newBuilder()
        url?.addQueryParameter("method", "album.search")
        url?.addQueryParameter("album", title)
        url?.addQueryParameter("api_key", LAST_API_KEY)
        url?.addQueryParameter("limit", limit.toString())
        url?.addQueryParameter("format", "json")

        if (url == null) {
            println("Unable to create url")
            return null
        }

        val request = Request.Builder()
            .url(url.build())
            .get()
            .build()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val resultJson = response.body.string()
                println("Result Found : $resultJson")
                return Gson().fromJson(resultJson, LastFmSearchAlbumResult::class.java).results

            } else {
                println("LAST ALBUM SEARCH FAILED : ${response.message} \n ${response.code} \n ${response.body.string()} ")
            }
        } catch (e: IOException) {
            println("NETWORK ERROR : \n ${e.stackTraceToString()}")
            _lastFmState.update { LASTFM_STATE.NETWORK_ERROR }
        } catch (e: Exception) {
            println("LOCAL ERROR : \n ${e.stackTraceToString()}")
            _lastFmState.update { LASTFM_STATE.NETWORK_ERROR }
        }
        return null


    }


}

enum class LASTFM_STATE {
    LOGGING_IN,
    SIGNED_OUT,
    SIGNED_IN,
    FAILED_LOGIN_PASSWORD,
    FAILED_LOGIN_USERNAME,
    NETWORK_ERROR
}