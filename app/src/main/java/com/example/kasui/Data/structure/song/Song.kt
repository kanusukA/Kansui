package com.example.kasui.Data.structure.song

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kasui.Data.request.FILETYPES
import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.EditorialNotes
import com.example.kasui.Data.structure.Genre
import com.example.kasui.Data.structure.Lyric


@Entity(tableName = "tracks")
data class Track(
    @PrimaryKey val id: Long,
    var albumName: String?,
    var artistName: String?,
    var artistUrl: String? = null,
    var attribution: String? = null, // (Classical music only) The name of the artist or composer to attribute the song with.
//    val audioVariants: List<String>?,
    val composerName: String? = null,
    val contentRating: String? = null,
    var discNumber: Int? = null, // Album Disc number
//    val editorialNotes: EditorialNotes?,
    val inFavorites: Boolean? = null,
    var releaseDate: String? = null,
    var trackNumber: Int? = null,
    val url: String? = null,
    var publisher: String? = null,
    var comment: String? = null,

    var lyrics: String? = null,
    var lyricsSynced: List<Lyric> = emptyList(),

    //Required
    var name: String,
    var durationInMillis: Int,
//    val genreNames: List<Genre>,
    var hasLyrics: Boolean,
    var isSynced: Boolean = false,
    val uri: Uri,
    val albumId: Long,

    var isTagLoaded: Boolean = false,

    var fileType: FILETYPES = FILETYPES.OTHER
)

class Song(
    val id: Long,
    val href: String, // Location on device
    val attributes: SongAttributes,
    val relationships: SongRelationships
) {}

data class SongAttributes(
    // Maybe
    val albumName: String?,
    val artistName: String?,
    val artistUrl: String?,
    val artwork: Artwork?,
    val attribution: String?, // (Classical music only) The name of the artist or composer to attribute the song with.
    val audioVariants: List<String>?,
    val composerName: String?,
    val contentRating: String?,
    val discNumber: Int?, // Album Disc number
    val editorialNotes: EditorialNotes?,
    val inFavorites: Boolean?,
    val releaseDate: String?,
    val trackNumber: Int?,
    val url: String?,

    //Required
    val name: String,
    val durationInMillis: Int,
    val genreNames: List<String>,
    val hasLyrics: Boolean,
    val uri: Uri

)

// Long contains ID
data class SongRelationships(
    val albums: List<Long>,
    val artists: List<Long>,
    val composers: List<Long>,
    val genres: List<Long>,
    val library: List<Long>,
    val station: List<Long>,

    )