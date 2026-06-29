package com.example.kasui.Data.structure.MusicBrainz


import com.google.gson.annotations.SerializedName

import kotlinx.serialization.Serializable

@Serializable
data class MbSearchRelease(
    val count: Int?,
    val releases: List<mbSearchReleaseItem>?
)

@Serializable
data class mbSearchReleaseItem(
    val title: String?,
    val id: String,
    @SerializedName("status-id") val statusId: String? = null,
    @SerializedName("artist-credit-id") val artistCreditId: String? = null,
    @SerializedName("artist-credit") val artistCredit: List<mbSearchArtistCredit> = emptyList(),
    @SerializedName("track-count") val trackCount: Int,
)

@Serializable
data class mbSearchArtistCredit(
    val name: String,
    val artist: mbSearchArtist
)

@Serializable
data class mbSearchArtist(
    val id: String,
    val name: String,
    @SerializedName("sort-name") val sortName: String,
    val disambiguation: String? = null
)


// MAIN RELEASE CLASS
@Serializable
data class MbReleaseDetail(
    val id: String,
    val title: String? = null,
    val date: String? = null,
    val country: String? = null,
    val barcode: String? = null,
    val packaging: String? = null,
    val disambiguation: String? = null,
    val quality: String? = null,
    val status: String? = null,
    val annotation: String? = null,
    val tags: List<Tag> = emptyList(),

    @SerializedName("status-id") val statusId: String? = null,
    @SerializedName("packaging-id") val packagingId: String? = null,
    @SerializedName("text-representation") val textRepresentation: TextRepresentation? = null,
    @SerializedName("artist-credit") val artistCredit: List<ArtistCredit> = emptyList(),
    @SerializedName("release-events") val releaseEvents: List<ReleaseEvent> = emptyList(),
    @SerializedName("cover-art-archive") val coverArtArchive: CoverArtArchive? = null,
    val media: List<Media> = emptyList()
)

@Serializable
data class ArtistCredit(
    val name: String,
    val joinphrase: String? = null, // e.g., " feat. "
    val artist: Artist
)

@Serializable
data class Tag(
    val count: Int,
    val name: String
)

@Serializable
data class Artist(
    val id: String,
    val name: String,
    val country: String? = null,
    val type: String? = null,
    val disambiguation: String? = null,
    @SerializedName("sort-name") val sortName: String,
    @SerializedName("type-id") val typeId: String? = null
)

@Serializable
data class ReleaseEvent(
    val date: String? = null,
    val area: Area? = null
)

@Serializable
data class Area(
    val id: String,
    val name: String? = null,
    val type: String? = null,
    val disambiguation: String? = null,
    @SerializedName("sort-name") val sortName: String? = null,
    @SerializedName("type-id") val typeId: String? = null,
    @SerializedName("iso-3166-1-codes") val iso31661Codes: List<String> = emptyList()
)

@Serializable
data class TextRepresentation(
    val language: String? = null,
    val script: String? = null
)

@Serializable
data class CoverArtArchive(
    val count: Int = 0,
    val artwork: Boolean = false,
    val front: Boolean = false,
    val back: Boolean = false,
    val darkened: Boolean = false
)

@Serializable
data class Media(
    val id: String,
    val format: String? = null, // e.g., "Digital Media", "CD"
    @SerializedName("track-count") val trackCount: Int,
    val tracks: List<Track> = emptyList()
)

@Serializable
data class Track(
    val id: String,
    val position: Int,
    val number: String, // Kept as String because vinyl tracks use "A1", "B1"
    val title: String,
    val length: Long? = null, // Track duration in milliseconds
    @SerializedName("artist-credit") val artistCredit: List<ArtistCredit> = emptyList(),
    val recording: Recording? = null
)

@Serializable
data class Recording(
    val id: String,
    val title: String,
    val length: Long? = null,
    val video: Boolean = false,
    val disambiguation: String? = null,
    @SerializedName("first-release-date") val firstReleaseDate: String? = null,
    @SerializedName("artist-credit") val artistCredit: List<ArtistCredit> = emptyList()
)

@Serializable
data class CoverArtResponse(
    val release: String? = null,
    val images: List<CoverArtImage> = emptyList()
)

@Serializable
data class CoverArtImage(
    val id: Long,
    val image: String, // <-- This is the URL to the highest/original quality image
    val front: Boolean = false,
    val back: Boolean = false,
    val approved: Boolean = false,
    val comment: String? = null,
    val edit: Long? = null,
    val types: List<String> = emptyList(),
    val thumbnails: Thumbnails? = null
)

@Serializable
data class Thumbnails(
    @SerializedName("1200") val size1200: String? = null,
    @SerializedName("500") val size500: String? = null,
    @SerializedName("250") val size250: String? = null,
    val large: String? = null,
    val small: String? = null
)