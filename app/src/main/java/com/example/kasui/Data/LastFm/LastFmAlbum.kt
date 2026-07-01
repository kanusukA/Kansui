package com.example.kasui.Data.LastFm

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
class LastFmSearchAlbumResult(
    val results: LastFmSearchAlbum?
)

@Serializable
class LastFmSearchAlbum(
    @SerializedName("opensearch:totalResults")
    val total: Int?,
    @SerializedName("albummatches")
    val albumMatches: LastFmSearchAlbumMatch?
)

@Serializable
class LastFmSearchAlbumMatch(
    val album: List<LastFmSearchAlbumItem>?
)

@Serializable
class LastFmSearchAlbumItem(
    val name: String?,
    val artist: String?,
    val id: Int?,
    val url: String?,
    val image: List<LastFmImage>?
)

@Serializable
class LastFmImage(
    @SerializedName("#text")
    val url: String?,
    val size: String?,
)


