package com.example.kasui.Data.LastFm

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

// /2.0/?method=tag.getinfo&tag=disco&api_key=YOUR_API_KEY&format=json

// PARAMS

//lang (Optional) : The language to return the wiki in, expressed as an ISO 639 alpha-2 code.
//tag (Required) : The tag name
//api_key (Required) : A Last.fm API key.

@Serializable
class LastFmTag(
    val name: String?,
    val url: String?,
    val reach: Int?,
    val taggings: Int?,
    val wiki: LastFmWiki?
)

// /2.0/?method=tag.getsimilar&tag=disco&api_key=YOUR_API_KEY&format=json

//PARAMS
//tag (Required) : The tag name
//api_key (Required) : A Last.fm API key.

@Serializable
class LastFmSimilarTags(
    @SerializedName("tag")
    val tags: List<LastFmTag>?
)

//  /2.0/?method=tag.gettopalbums&tag=disco&api_key=YOUR_API_KEY&format=json

//PARAMS
//tag (Required) : The tag name
//limit (Optional) : The number of results to fetch per page. Defaults to 50.
//page (Optional) : The page number to fetch. Defaults to first page.
//api_key (Required) : A Last.fm API key.

@Serializable
class LastFmTagTopAlbums(
    val tag: String?,
    val albumItem: List<LastFmTagTopAlbumItem>?
)

@Serializable
class LastFmTagTopAlbumItem(
    val rank: Any,
    val name: String?,
    val mbid: String?,
    val url: String?,
    val artist: LastFmTagArtist?,
    val image: List<LastFmImage>?

)

@Serializable
class LastFmTagArtist(
    val name: String?,
    val mbid: String?,
    val url: String?,
    val rank: Any,
    val image: List<LastFmImage>?
)

// /2.0/?method=tag.gettopartists&tag=disco&api_key=YOUR_API_KEY&format=json

// PARAMS
//tag (Required) : The tag name
//limit (Optional) : The number of results to fetch per page. Defaults to 50.
//page (Optional) : The page number to fetch. Defaults to first page.
//api_key (Required) : A Last.fm API key.

@Serializable
class LastFmTagTopArtists(
    val tag: String?,
    val artist: List<LastFmTagArtist>?
)

//  /2.0/?method=tag.getTopTags&api_key=YOUR_API_KEY&format=json

//PARAMS
//api_key (Required) : A Last.fm API key.

@Serializable
class LastFmTopTags(
    @SerializedName("tag")
    val topTags: List<LastFmTag>?
)

//  /2.0/?method=tag.gettoptracks&tag=disco&api_key=YOUR_API_KEY&format=json

//PARAMS
//tag (Required) : The tag name
//limit (Optional) : The number of results to fetch per page. Defaults to 50.
//page (Optional) : The page number to fetch. Defaults to first page.
//api_key (Required) : A Last.fm API key.

@Serializable
class LastFmTopTagTracks(
    val track: List<LastFmTagTrack>?
)

@Serializable
class LastFmTagTrack(
    val name: String?,
    val mbid: String?,
    val url: String?,
    val artist: LastFmTagArtist?,
    val image: List<LastFmImage>?
)