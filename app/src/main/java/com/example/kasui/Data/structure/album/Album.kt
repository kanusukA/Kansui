package com.example.kasui.Data.structure.album

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.EditorialNotes
import com.example.kasui.Data.structure.Genre


@Entity(tableName = "albums")
data class AlbumEntity(
    @PrimaryKey val id: Long,
    val href: String,
    val artistName: String,
    val albumName: String,
    val genreNames: List<Genre>,
    val isSingle: Boolean,
    val isCompilation: Boolean,
    val isComplete: Boolean,
    val url: String,
    val trackCount: Int,
    @Embedded(prefix = "artwork_") val artwork: Artwork,

    val audioVariants: List<String>? = null,
    val artistUrl: String? = null,
    val contentRating: String? = null,
    val editorialNotes: EditorialNotes? = null,
    val inFavorites: Boolean? = null,

    val releaseDate: String? = null,
    val recordLabel: String? = null
)

@Entity(primaryKeys = ["albumId", "trackId"])
data class AlbumTrackCross(
    val albumId: Long,
    val trackId: Long
)

data class kAlbum(
    @PrimaryKey val albumEntity: AlbumEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(AlbumTrackCross::class)
    )

)


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