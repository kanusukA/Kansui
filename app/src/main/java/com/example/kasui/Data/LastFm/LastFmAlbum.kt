package com.example.kasui.Data.LastFm

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


//  /2.0/?method=album.getinfo&api_key=YOUR_API_KEY&artist=Cher&album=Believe&format=json

// PARAMS
//artist (Required (unless mbid)] : The artist name
//album (Required (unless mbid)] : The album name
//mbid (Optional) : The musicbrainz id for the album
//autocorrect[0|1] (Optional) : Transform misspelled artist names into correct artist names, returning the correct version instead. The corrected artist name will be returned in the response.
//username (Optional) : The username for the context of the request. If supplied, the user's playcount for this album is included in the response.
//lang (Optional) : The language to return the biography in, expressed as an ISO 639 alpha-2 code.
//api_key (Required) : A Last.fm API key.
@Serializable
class LastFmAlbum(
    val album: LastFmAlbumInfo?
)

@Serializable
class LastFmAlbumInfo(
    val name: String?,
    val artist: String?,
    val id: String?,
    val mbid: String?,
    val url: String?,
    @SerializedName("releasedate")
    val releaseDate: String?,
    val image: List<LastFmImage>?,
    val listeners: String?,
    val playcount: String?,
    val tags: LastFmTopTags,
    val tracks: LastFmAlbumTracks?,
    val wiki: LastFmWiki?
)

@Serializable
class LastFmAlbumTracks(
    val track: List<LastFmTrackItem>?
)

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


