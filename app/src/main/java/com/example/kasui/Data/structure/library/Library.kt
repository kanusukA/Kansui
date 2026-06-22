package com.example.kasui.Data.structure.library

import com.example.kasui.Data.structure.Artwork

class Library (
    val id :Int ,
    val href: String,
    val libraryAttributes: LibraryAttributes,

){
}


data class LibraryAttributes(
    val name: String,
    val canEdit: Boolean,


    val artwork: Artwork?,
    val dateAdded: String?,
    val description : String?,
    val inFavorites : Boolean?,
)

//data class LibraryRelationships(
//
//)