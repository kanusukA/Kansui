package com.example.kasui.Data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val MusicBrainZAPI = "https://musicbrainz.org/ws/2/"

object NetworkManager {
    private var _retrofit: Retrofit? = null

    private var httpClient = OkHttpClient.Builder().build()

    fun provideRetrofit(): Retrofit {
        if (_retrofit == null) {
            _retrofit = Retrofit.Builder()
                .baseUrl(MusicBrainZAPI)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return _retrofit!!
    }

//    fun provideMusicBrainzAPI :

}