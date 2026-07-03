package com.example.kasui.Data.LastFm

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

//  /2.0/?method=track.getInfo&api_key=YOUR_API_KEY&artist=cher&track=believe&format=json

//PARAMS
//mbid (Optional) : The musicbrainz id for the track
//track (Required (unless mbid)] : The track name
//artist (Required (unless mbid)] : The artist name
//username (Optional) : The username for the context of the request. If supplied, the user's playcount for this track and whether they have loved the track is included in the response.
//autocorrect[0|1] (Optional) : Transform misspelled artist and track names into correct artist and track names, returning the correct version instead. The corrected artist and track name will be returned in the response.
//api_key (Required) : A Last.fm API key.

@Serializable
class LastFmTrack(
    val track: LastFmTrackItem
)

@Serializable
class LastFmTrackItem(
    val id: String?,
    val name: String?,
    val mbid: String?,
    val url: String?,
    val duration: Int?,
    val listeners: Int?,
    val playcount: Int?,
    val artist: LastFmTrackArtist?,
    val album: LastFmTrackAlbum?,
    val topTags: LastFmTopTags?,
    val wiki: LastFmWiki?,
    val attr: LastFmTrackAttr?
)

@Serializable
class LastFmTrackAttr(
    val rank: Int?
)

@Serializable
class LastFmTrackArtist(
    val name: String?,
    val mbid: String?,
    val url: String?
)

@Serializable
class LastFmTrackAlbum(
    val artist: String?,
    val title: String?,
    val mbid: String?,
    val url: String?,
    val image: List<LastFmImage>?
)


//  /2.0/?method=track.search&track=Believe&api_key=YOUR_API_KEY&format=json

// PARAMS
//limit (Optional) : The number of results to fetch per page. Defaults to 30.
//page (Optional) : The page number to fetch. Defaults to first page.
//track (Required) : The track name
//artist (Optional) : Narrow your search by specifying an artist.
//api_key (Required) : A Last.fm API key.


@Serializable
class LastFmSearchTrack(
    val results: LastFmSearchTrackResult?
)

@Serializable
class LastFmSearchTrackResult(
    val totalResults: String?,
    @SerializedName("trackmatches")
    val trackMatches: LastFmSearchTrackMatches?
)

@Serializable
class LastFmSearchTrackMatches(
    val track: List<LastFmSearchTrackItem>?
)

@Serializable
data class LastFmSearchTrackItem(
    val name: String,
    val artist: String,
    val url: String,
    val listeners: String? = null,
    val image: List<LastFmImage> = emptyList()
)