package com.example.kasui.Data.structure.song

import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.EditorialNotes

class Song (
    val id: Int,
    val href: String, // Location on device
    val attributes: SongAttributes,
    val relationships: SongRelationships
){}

data class SongAttributes(
    // Maybe
    val albumName : String?,
    val artistName : String?,
    val artistUrl : String?,
    val artwork : Artwork?,
    val attribution : String?, // (Classical music only) The name of the artist or composer to attribute the song with.
    val audioVariants : List<String>?,
    val composerName : String?,
    val contentRating : String?,
    val discNumber : Int?, // Album Disc number
    val editorialNotes : EditorialNotes?,
    val inFavorites : Boolean?,
    val releaseDate : String?,
    val trackNumber : Int?,

    //Required
    val name : String,
    val durationInMillis : Int,
    val genreNames : List<String>,
    val hasLyrics: Boolean,
    val url : String
)


data class SongRelationships(
    val albums : List<String>,
    val artists : List<String>,
    val composers : List<String>,
    val genres : List<String>,
    val library : List<String>,
    val station : List<String>,

)