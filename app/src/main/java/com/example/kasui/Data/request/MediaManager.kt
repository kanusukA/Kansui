package com.example.kasui.Data.request


import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresExtension
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.application
import coil3.Bitmap
import com.example.kasui.Data.MusicBrainZ.musicBrainz
import com.example.kasui.Data.local.AlbumDatabase
import com.example.kasui.Data.local.AlbumRepository
import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.Genre
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Data.structure.album.AlbumAttributes
import com.example.kasui.Data.structure.album.AlbumEntity
import com.example.kasui.Data.structure.album.AlbumRelationships
import com.example.kasui.Data.structure.album.AlbumViews
import com.example.kasui.Data.structure.album.InsertAlbum
import com.example.kasui.Data.structure.song.Song
import com.example.kasui.Data.structure.song.SongAttributes
import com.example.kasui.Data.structure.song.SongRelationships
import com.example.kasui.Data.structure.song.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

// ALL INFO FETCHED AND STORED USES ALBUM AS THE STARTING POINT
object MediaManager {

    private var albumRepository: AlbumRepository? = null


    fun initRepo(context: Context) {
        val albumDb = AlbumDatabase.getInstance(context)
        albumRepository = AlbumRepository(albumDb.getAlbumDao())

    }

    private var _insertAlbums = MutableStateFlow(listOf<InsertAlbum>())
    val insertAlbums = _insertAlbums.asStateFlow()


    private var _mediaState: MutableStateFlow<MediaManagerState> = MutableStateFlow(
        MediaManagerState.FREE
    )
    val mediaState: StateFlow<MediaManagerState> = _mediaState.asStateFlow()


    fun saveAlbums(albums: List<InsertAlbum>) {
        if (albumRepository != null) {
            albums.forEach {
                albumRepository?.addAlbum(it.albumEntity, it.genre, it.tracks)
            }
        }
    }


    fun fetchMusicFiles(context: Context) {
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        _mediaState.update { MediaManagerState.LOADING_RAW }
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ARTIST,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media.BITRATE,
            MediaStore.Audio.Media.BITS_PER_SAMPLE,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DISC_NUMBER,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.GENRE,
            MediaStore.Audio.Media.GENRE_ID,
            MediaStore.Audio.Media.SAMPLERATE,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.TRACK,
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val insertAlbum = mutableListOf<InsertAlbum>()

        context.contentResolver.query(uri, projection, selection, null, null)?.use { cursor ->

//            cursor.columnNames?.forEach {
//                println(it)
//            }

            val _ID_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val ALBUM_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val ALBUM_ARTIST_Index =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ARTIST)
            val ALBUM_ID_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val ARTIST_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val ARTIST_ID_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)
            val BITRATE_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BITRATE)
            val BITS_PER_SAMPLE_Index =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BITS_PER_SAMPLE)
            val DATE_ADDED_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
            val DISC_NUMBER_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISC_NUMBER)
            val DURATION_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val GENRE_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE)
            val GENRE_ID_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.GENRE_ID)
            val SAMPLERATE_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SAMPLERATE)
            val TITLE_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val TRACK_Index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)

            if (cursor.isLast) {
                println("No Music file found")
                return
            }

            while (cursor.moveToNext()) {
                val musicUri = ContentUris.withAppendedId(uri, cursor.getLong(_ID_Index))
                val albumUri = ContentUris.withAppendedId(uri, cursor.getLong(ALBUM_ID_Index))

//                if (tracksLoaded.contains(cursor.getLong(_ID_Index))) {
//                    continue
//                }
                val track = Track(
                    id = cursor.getLong(_ID_Index),
                    uri = musicUri,
                    albumName = cursor.getStringOrNull(ALBUM_Index),
                    artistName = cursor.getStringOrNull(ARTIST_Index),
                    name = cursor.getStringOrNull(TITLE_Index) ?: "Unknow Title",
                    durationInMillis = cursor.getIntOrNull(DURATION_Index) ?: 0,
                    hasLyrics = false,
                    albumId = cursor.getLong(ALBUM_ID_Index)
                )


                var index: Int? = null

                insertAlbum.forEachIndexed { i, album ->
                    if (album.albumEntity.id == cursor.getLong(ALBUM_ID_Index)) {
                        index = i
                    }
                }

                if (index != null) {
                    // add song to album
                    insertAlbum[index].tracks.add(track)

                } else {

                    val artwork = Artwork(
                        height = 600,
                        width = 600,
                        url = null,
                        uri = musicUri,
                        bitmap = null,
                        bgColor = null,
                        textColor1 = null,
                        textColor2 = null,
                        textColor3 = null,
                        textColor4 = null
                    )

                    val albumEntity = AlbumEntity(
                        id = cursor.getLong(ALBUM_ID_Index),
                        href = albumUri.toString(),
                        artistName = track.artistName ?: "Unknow Artist",
                        albumName = track.albumName ?: "Unknown Album",
                        artwork = artwork,
                        isSingle = false,
                        isComplete = false,
                        isCompilation = false,
                        url = "",
                    )

                    val genres = mutableListOf(
                        Genre(
                            name = cursor.getStringOrNull(GENRE_Index) ?: "",
                            id = cursor.getLong(GENRE_ID_Index)
                        )
                    )

                    val tracks = mutableListOf(track)

                    insertAlbum.add(
                        InsertAlbum(albumEntity, tracks, genres)
                    )


                }

            }
        }
        _insertAlbums.update { insertAlbum }
        _mediaState.update { MediaManagerState.FREE }

    }

    fun fetchArtworkFromTrackUri(context: Context, musicUri: Uri): android.graphics.Bitmap? {
        // return context.contentResolver.loadThumbnail(musicUri, Size(500, 500), null)
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, musicUri)
            val rawBytes = retriever.embeddedPicture // Extracts raw unpadded compressed image bytes

            if (rawBytes != null) {
                BitmapFactory.decodeByteArray(rawBytes, 0, rawBytes.size)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        } finally {
            retriever.release() // Always free up system resources
        }
    }


    @Deprecated(message = "USE TRACK INSTEAD")
    fun fetchSongsFromAlbum(album: Album): List<Song> {
        val songs = mutableListOf<Song>()
//        album.albumRelationships?.tracks?.forEach { id ->
//            _rawSongList.value.forEach { song ->
//                if (song.id == id) {
//                    songs.add(song)
//                    return@forEach
//                }
//            }
//        }
        return songs
    }


    val artworkFetcher = CoroutineScope(Dispatchers.IO)


    // MUSIC BRAINZ
    @Deprecated(message = "MUSICBRAINZ is not used!")
    suspend fun syncAlbumLibraryWithMusicBrainZ() {
//        _mediaState.update { MediaManagerState.LOADING_RAW }
//        val syncList: MutableMap<Int, List<Album>> = mutableMapOf()
//
//        _rawAlbumList.value.forEachIndexed { index, album ->
//
//            delay(1200.milliseconds)
//
//            val searchReleases = musicBrainz.searchReleases(album.albumAttributes.albumName, null)
//
//            if (searchReleases?.releases != null && searchReleases.releases.isNotEmpty()) {
//                var count = 2
//                if (searchReleases.releases.size < 2) count = searchReleases.releases.size
//                val resultList = mutableListOf<Album>()
//                for (releaseIndex in 0..<count) {
//                    val release = searchReleases.releases[releaseIndex]
//
//                    val coverArt = musicBrainz.getReleaseCover(release.id)
//
//                    val artwork = Artwork(
//                        height = 0,
//                        width = 0,
//                        url = if (!coverArt?.images.isNullOrEmpty()) coverArt.images[0].image else null
//                    )
//
//                    val attributes = AlbumAttributes(
//                        artistName = if (release.artistCredit.isNotEmpty()) release.artistCredit[0].name else "Unknown",
//                        albumName = release.title ?: " Unknown Album",
//                        artwork = artwork,
//                        genreNames = emptyList(),
//                        isSingle = release.trackCount == 1,
//                        isCompilation = false,
//                        isComplete = false,
//                        url = "",
//                        trackCount = release.trackCount
//                    )
//
//                    resultList.add(
//                        Album(
//                            id = album.id,
//                            href = release.id,
//                            albumAttributes = attributes,
//                            albumRelationships = null,
//                            albumViews = null
//                        )
//                    )
//
//
//                }
//
//                syncList[index] = resultList
//            }

//        }
//
//        _searchAlbumList.update { syncList.toMap() }
//        _mediaState.update { MediaManagerState.FREE }

    }

}

enum class MediaManagerState {
    LOADING_RAW,
    LOADING_INFO,
    FREE

}