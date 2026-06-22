package com.example.kasui.Data.structure.playlist

import com.example.kasui.Data.structure.Artwork

class Playlist(
    val id: Int,
    val href: String,
    val playlistAttributes: PlaylistAttributes,
    val playlistRelationships: PlaylistRelationships
) {
}

data class PlaylistAttributes(
    // Required
    val curatorName: String,
    val isChart: Boolean,
    val name: String,
    val playlistType : String,
    /*Editorial: A playlist created by  curator.
    External: A playlist created by a external curator or brand.
    Personal-mix: A personalized playlist for an  user.
    Replay: A personalized  Replay playlist for an  user.
    User-shared: A playlist created and shared by an  user.
    Possible Values: editorial, external, personal-mix, replay, user-shared */
    val url: String,

    //Maybe
    val lastModifiedDate: String?,
    val description: String?,
    val trackTypes: List<String>?,
    val inFavorites: Boolean?,
    val artwork: Artwork?
)

data class PlaylistRelationships(
    val curator : List<String>?,
    val library: List<String>?,
    val tracks: List<String>?
)