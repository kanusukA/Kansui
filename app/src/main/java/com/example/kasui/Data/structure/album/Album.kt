package com.example.kasui.Data.structure.album

import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.EditorialNotes

class Album(
    val id: Int,
    val href: String,
    val albumAttributes: AlbumAttributes,
    val albumRelationships: AlbumRelationships,
    val albumViews: AlbumViews
) {
}

data class AlbumAttributes(
    // Required
    val artistName: String,
    val artwork: Artwork,
    val genreNames: List<String>,
    val isSingle: Boolean,
    val isCompilation: Boolean,
    val isComplete: Boolean,
    val url: String,
    val trackCount : Int,

    //Maybe
    val audioVariants: List<String>?,
    val artistUrl: String?,
    val contentRating: String?,
    val editorialNotes: EditorialNotes?,
    val inFavorites : Boolean?,
    val recordLabel : String?,
    val releaseDate: String?,

)

data class AlbumRelationships(
    val artists : List<String>?,
    val genres : List<String>?,
    val tracks: List<String>?,
    val library: List<String>?,
    val recordLabels: List<String>?,
)

data class AlbumViews(
    val otherVersions: List<String>,
    val appearsOn : List<String>,
)