package com.example.kasui.Data.structure.album

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.EditorialNotes
import com.example.kasui.Data.structure.Genre
import com.example.kasui.Data.structure.song.Track
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: Long,
    val href: String,
    val artistName: String,
    val albumName: String,
//    val genreNames: List<Genre>,
    val isSingle: Boolean,
    val isCompilation: Boolean,
    val isComplete: Boolean,
    val url: String,
    @Embedded(prefix = "artwork_") val artwork: Artwork,

//    val audioVariants: List<String>? = null,
    val artistUrl: String? = null,
    val contentRating: String? = null,
//    val editorialNotes: EditorialNotes? = null,
    val inFavorites: Boolean? = null,

    val releaseDate: String? = null,
    val recordLabel: String? = null
)

@Entity(
    tableName = "album_genre_cross_ref",
    primaryKeys = ["albumId", "genreId"],
    indices = [
        Index(value = ["albumId"]),
        Index(value = ["genreId"])
    ]
)
data class AlbumGenreCrossRef(
    val albumId: Long,
    val genreId: Long
)

@Entity(
    tableName = "album_track_cross_ref",
    primaryKeys = ["albumId", "trackId"],
    indices = [
        Index(value = ["albumId"]),
        Index(value = ["trackId"])
    ]
)
data class AlbumTrackCrossRef(
    val albumId: Long,
    val trackId: Long
)

// VIEW ONLY
data class Album(
    @Embedded val album: AlbumEntity,
    @Relation(
        parentColumn = "genreId",
        entityColumn = "albumId",
        associateBy = Junction(AlbumGenreCrossRef::class)
    )
    val genres: Flow<List<Genre>>,

    @Relation(
        parentColumn = "albumId",
        entityColumn = "trackId",
        associateBy = Junction(AlbumTrackCrossRef::class)
    )
    val tracks: Flow<List<Track>>

)

data class InsertAlbum(
    val albumEntity: AlbumEntity,
    val tracks: MutableList<Track>,
    val genre: MutableList<Genre>
)


//@Entity(primaryKeys = ["albumId", "trackId"])
//data class AlbumTrackCross(
//    val albumId: Long,
//    val trackId: Long
//)
//
//data class kAlbum(
//    @PrimaryKey val albumEntity: AlbumEntity,
//    @Relation(
//        parentColumn = "id",
//        entityColumn = "id",
//        associateBy = Junction(AlbumTrackCross::class)
//    )
//
//)


//class Album(
//    val id: Long,
//    val href: String,
//    val albumAttributes: AlbumAttributes,
//    val albumRelationships: AlbumRelationships?,
//    val albumViews: AlbumViews?
//)


class AlbumAttributes(
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