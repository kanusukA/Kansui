package com.example.kasui.Data.structure

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val parentId: Int?,
    val parentName: String?
)