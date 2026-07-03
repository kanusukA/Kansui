package com.example.kasui.Data.structure.album

import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.EditorialNotes

class Album(
    val id: Long,
    val href: String,
    val albumAttributes: AlbumAttributes,
    val albumRelationships: AlbumRelationships?,
    val albumViews: AlbumViews?
)

data class AlbumAttributes(
    // Required
    val artistName: String,
    val albumName: String,
    val artwork: Artwork,
    val genreNames: List<String>,
    val isSingle: Boolean,
    val isCompilation: Boolean,
    val isComplete: Boolean,
    val url: String,
    var trackCount: Int,

    //Maybe
    val audioVariants: List<String>? = null,
    val artistUrl: String? = null,
    val contentRating: String? = null,
    val editorialNotes: EditorialNotes? = null,
    val inFavorites: Boolean? = null,
    val recordLabel: String? = null,
    val releaseDate: String? = null,

    )

data class AlbumRelationships(
    val artists: MutableList<Long>,
    val genres: MutableList<Long>,
    val tracks: MutableList<Long>,
    val library: MutableList<Long>,
    val recordLabels: MutableList<Long>,
)

data class AlbumViews(
    val otherVersions: List<String>,
    val appearsOn: List<String>,
)