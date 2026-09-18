package com.example.kasui.Data.structure

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val parentId: Int? = null,
    val parentName: String? = null
)