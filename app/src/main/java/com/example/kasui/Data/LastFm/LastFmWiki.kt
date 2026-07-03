package com.example.kasui.Data.LastFm

import kotlinx.serialization.Serializable

@Serializable
class LastFmWiki(
    val published: String?,
    val summary: String?,
    val content: String?
)