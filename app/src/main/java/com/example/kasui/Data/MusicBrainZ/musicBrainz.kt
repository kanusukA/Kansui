package com.example.kasui.Data.MusicBrainZ

//import com.example.kasui.Data.NetworkManager
import com.example.kasui.Data.structure.MusicBrainz.CoverArtResponse
import com.example.kasui.Data.structure.MusicBrainz.MbReleaseDetail
import com.example.kasui.Data.structure.MusicBrainz.MbSearchRelease
//import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Dispatcher
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import kotlin.time.Duration.Companion.milliseconds


interface MbServices {
//    @GET("search")
//    suspend fun searchRelease(
//        @Query("query") query: String,
//        @Query("type") type: String = "release"
//    ) :
}


object musicBrainz {

    private val mutex = Mutex()

    private var _apiLock = false
    private val _apiScope = CoroutineScope(Dispatchers.IO)

    private val json = Json { prettyPrint = true }

    private suspend fun setApiLock() {
        mutex.withLock {
            _apiLock = true // JUST TO MAKE SURE ;)
            _apiScope.async {
                _apiLock = true
                delay(timeMillis = 1500)
                _apiLock = false
            }
        }
    }

    private suspend fun getApiLockValidation(): Boolean {
        if (_apiLock) {
            for (x in 0..5) {
                delay(2000.milliseconds)
                if (!_apiLock) {
                    break
                }
            }
            if (_apiLock) {
                return false
            }
        }
        setApiLock()
        return true

    }

    private suspend fun getMusicApiRequest(
        requestType: MbRequestType
    ): Request? {
        // THE REQUEST PARAMETER IS TAKEN FROM HERE

        val validation = getApiLockValidation()

        if (!validation) {
            println("REQUEST FAILED API BUSY! i.e. API LOCK IS SET")
            return null
        }

        var request: HttpUrl.Builder?

        when (requestType) {
            is MbRequestType.GetRelease -> {
                request = "https://musicbrainz.org/ws/2/release/${requestType.id}".toHttpUrlOrNull()
                    ?.newBuilder()
                request?.encodedQuery("inc=recordings+media+artist-credits+annotation+tags&fmt=json")
                request?.scheme("https")
            }

            is MbRequestType.SearchRelease -> {
                request = "https://musicbrainz.org/ws/2/release".toHttpUrlOrNull()?.newBuilder()
                request?.scheme("https")
                request?.addQueryParameter("query", "release:${requestType.searchRelease}")
            }

            is MbRequestType.GetCoverArt -> {
                request = "https://coverartarchive.org/release/${requestType.id}".toHttpUrlOrNull()
                    ?.newBuilder()

            }
        }


        if (request == null) {
            println("Failed to create URL")
            return null
        }

        println("REQUEST MADE ")

        return Request.Builder()
            .url(request.build())
            .header(
                "User-Agent",
                "Kansui/0.0.1 ( kanusuka@gmail.com )"
            )
            .header("Accept", "application/json")
            .build()


    }

    // API CALL

    suspend fun searchReleases(
        title: String,
        artist: String?,

        ): MbSearchRelease? {
        val client = OkHttpClient()

        val request =
            getMusicApiRequest(requestType = MbRequestType.SearchRelease(title)) ?: return null

        try {
            val response = client.newCall(request).execute()
            val result = response.body.string()
            println("RESPONSE : ${result}")
//            return Gson().fromJson(result, MbSearchRelease::class.java)
            return json.decodeFromString<MbSearchRelease>(result)
        } catch (e: IOException) {
            println("ERROR PROCESSING REQUEST : ${e.message} \n ${e.printStackTrace()}")
        } catch (e: Exception) {
            println(
                "ERROR IN JSON CONVERSION : ${e.cause} ${e.message} \n ${
                    e.stackTraceToString()
                }"
            )
        }

        return null
    }

    suspend fun getReleaseCover(
        id: String,
    ): CoverArtResponse? {

        val client = OkHttpClient()

        val request = getMusicApiRequest(requestType = MbRequestType.GetCoverArt(id)) ?: return null

        try {
            val response = client.newCall(request).execute()
            val result = response.body.string()
            println("RESPONSE : ${result}")
//            return Gson().fromJson(result, CoverArtResponse::class.java)
            return json.decodeFromString<CoverArtResponse>(result)
        } catch (e: IOException) {
            println("ERROR PROCESSING REQUEST : ${e.message} \n ${e.printStackTrace()}")
        } catch (e: Exception) {
            println("ERROR PHRASING COVER IMAGE : ${e.stackTraceToString()}")
        }
        return null
    }

    suspend fun getRelease(
        id: String,

        ): MbReleaseDetail? {
        val client = OkHttpClient()

        val request = getMusicApiRequest(requestType = MbRequestType.GetRelease(id)) ?: return null

        try {
            val response = client.newCall(request).execute()
            val result = response.body.string()
            println("RESPONSE : ${result}")
//            return Gson().fromJson(result, MbReleaseDetail::class.java)
            return json.decodeFromString<MbReleaseDetail>(result)
        } catch (e: IOException) {
            println("ERROR PROCESSING REQUEST : ${e.message} \n ${e.printStackTrace()}")
        } catch (e: Exception) {
            println("ERROR : ${e.stackTraceToString()}")
        }
        return null
    }

    private sealed class MbRequestType {
        data class SearchRelease(val searchRelease: String) : MbRequestType()
        data class GetRelease(val id: String) : MbRequestType()
        data class GetCoverArt(val id: String) : MbRequestType()
    }

}