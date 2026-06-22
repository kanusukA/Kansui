package com.example.kasui.Data.structure.artist

import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.EditorialNotes

class Artist(
    val id: Int,
    val href: String,
    val artistAttributes: ArtistAttributes,
    val artistRelationships: ArtistRelationships

) {
}

data class ArtistAttributes(
    //Required
    val name: String,
    val url: String,

    // Maybe
    val artwork: Artwork?,
    val editorialNotes: EditorialNotes?,
    val genreNames: List<String>?,
    val inFavorites: List<String>?,

)

data class ArtistRelationships(
    val albums : List<String>,
    val genres: List<String>,
    val playlists: List<String>,

)