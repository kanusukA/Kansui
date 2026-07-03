package com.example.kasui.Data.LastFm

import kotlinx.serialization.Serializable

//  /2.0/?method=artist.getinfo&artist=Cher&api_key=YOUR_API_KEY&format=json

// PARAMS
//artist (Required (unless mbid)] : The artist name
//mbid (Optional) : The musicbrainz id for the artist
//lang (Optional) : The language to return the biography in, expressed as an ISO 639 alpha-2 code.
//autocorrect[0|1] (Optional) : Transform misspelled artist names into correct artist names, returning the correct version instead. The corrected artist name will be returned in the response.
//username (Optional) : The username for the context of the request. If supplied, the user's playcount for this artist is included in the response.
//api_key (Required) : A Last.fm API key.


@Serializable
class LastFmArtist(
    val name: String?,
    val mbid: String?,
    val url: String?,
    val image: List<LastFmImage>?,
    val stats: LastFmArtistStats?,
    val similar: List<LastFmSimilarArtist>?,
    val tags: List<LastFmTag>?,
    val bio: LastFmWiki?
)


@Serializable
class LastFmSimilarArtist(
    val name: String?,
    val url: String?,
    val image: List<LastFmImage>?
)

@Serializable
class LastFmArtistStats(
    val listeners: Int?,
    val plays: Int?
)