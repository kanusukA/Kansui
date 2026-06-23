package com.example.kasui.Data.request


import android.content.Context
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresExtension
import com.example.kasui.Data.structure.song.Song
import com.example.kasui.Data.structure.song.SongAttributes

class MediaManager(
    val context: Context
) {

    val musicFiles = listOf<Song>()

    @RequiresExtension(extension = Build.VERSION_CODES.TIRAMISU, version = 15)
    fun fetchMusicFiles() {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//            MediaStore.Audio.Media.ALBUM,
//            MediaStore.Audio.Media.ALBUM_ARTIST,
//            MediaStore.Audio.Media.ALBUM_ID,
//            MediaStore.Audio.Media.ARTIST,
//            MediaStore.Audio.Media.ARTIST_ID,
//            MediaStore.Audio.Media.BITRATE,
//            MediaStore.Audio.Media.BITS_PER_SAMPLE,
//            MediaStore.Audio.Media.DATE_ADDED,
//            MediaStore.Audio.Media.DISC_NUMBER,
//            MediaStore.Audio.Media.DURATION,
//            MediaStore.Audio.Media.GENRE,
//            MediaStore.Audio.Media.GENRE_ID,
//            MediaStore.Audio.Media.SAMPLERATE,
//            MediaStore.Audio.Media.TITLE,
//            MediaStore.Audio.Media.TRACK,


        )
//        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
//        context.contentResolver.query(uri, projection, selection, null, null)?.use { cursor ->
//            val songAttributes = SongAttributes(
//                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))
//                    ?: "Unknow Title",
//                durationInMillis = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)) ?: 0,
//                genreNames =
//            )
//        }
    }

}