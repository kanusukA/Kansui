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
import coil3.Bitmap
import com.example.kasui.Data.MusicBrainZ.musicBrainz
import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Data.structure.album.AlbumAttributes
import com.example.kasui.Data.structure.album.AlbumRelationships
import com.example.kasui.Data.structure.album.AlbumViews
import com.example.kasui.Data.structure.song.Song
import com.example.kasui.Data.structure.song.SongAttributes
import com.example.kasui.Data.structure.song.SongRelationships
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

// ALL INFO FETCHED AND STORED USES ALBUM AS THE STARTING POINT
object MediaManager {

    private var _rawSongList = MutableStateFlow(listOf<Song>())
    val rawSongList: StateFlow<List<Song>> = _rawSongList

    private var _rawAlbumList = MutableStateFlow(listOf<Album>())
    val rawAlbumList: StateFlow<List<Album>> = _rawAlbumList

    // key is the index of _rawSongList
    private var _searchAlbumList = MutableStateFlow(mapOf<Int, List<Album>>())
    val searchAlbumList = _searchAlbumList.asStateFlow()


    private var _mediaState: MutableStateFlow<MediaManagerState> = MutableStateFlow(
        MediaManagerState.FREE
    )
    val mediaState: StateFlow<MediaManagerState> = _mediaState.asStateFlow()

    @RequiresExtension(extension = Build.VERSION_CODES.TIRAMISU, version = 15)
    suspend fun fetchMusicFiles(context: Context) {
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

        val songList = mutableListOf<Song>()
        val albumList = mutableListOf<Album>()

        context.contentResolver.query(uri, projection, selection, null, null)?.use { cursor ->

            cursor.columnNames?.forEach {
                println(it)
            }

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

                var bitmap: Bitmap? = null

                bitmap = fetchArtworkFromTrackUri(context, musicUri)

//                if (bitmap != null) {
//                    //println("${bitmap.width} : ${bitmap.height}")
//                }


                val songAttribute = SongAttributes(
                    name = cursor.getStringOrNull(TITLE_Index) ?: "Unknow Title",
                    durationInMillis = cursor.getIntOrNull(DURATION_Index) ?: 0,
                    genreNames = if (cursor.getStringOrNull(GENRE_Index) != null) listOf(
                        cursor.getString(
                            GENRE_Index
                        )
                    ) else emptyList(),
                    hasLyrics = false,
                    uri = musicUri,
                    albumName = cursor.getStringOrNull(ALBUM_Index),
                    artistName = cursor.getStringOrNull(ARTIST_Index),
                    artistUrl = null,
                    artwork = Artwork(
                        height = 0,
                        width = 0,
                        url = null,
                        uri = null,
                        bitmap = null,
                        bgColor = null,
                        textColor1 = null,
                        textColor2 = null,
                        textColor3 = null,
                        textColor4 = null
                    ),
                    attribution = null,
                    audioVariants = null,
                    composerName = null,
                    contentRating = null,
                    discNumber = cursor.getIntOrNull(DISC_NUMBER_Index),
                    editorialNotes = null,
                    inFavorites = null,
                    releaseDate = cursor.getStringOrNull(DATE_ADDED_Index),
                    trackNumber = cursor.getIntOrNull(TRACK_Index),
                    url = null
                )


                val songRelationship = SongRelationships(
                    albums = listOf(cursor.getLong(ALBUM_ID_Index)),
                    artists = listOf(cursor.getLong(ARTIST_ID_Index)),
                    composers = emptyList(),
                    genres = emptyList(),
                    library = emptyList(),
                    station = emptyList()
                )

                val song = Song(
                    id = cursor.getLong(_ID_Index),
                    href = musicUri.path ?: "",
                    attributes = songAttribute,
                    relationships = songRelationship,
                )

                songList.add(song)

                var index: Int? = null

                albumList.forEachIndexed { i, album ->
                    if (album.id == cursor.getLong(ALBUM_ID_Index)) {
                        index = i
                    }
                }

                if (index != null) {
                    albumList[index].albumRelationships?.tracks?.add(song.id)
                    albumList[index].albumAttributes.trackCount =
                        albumList[index].albumRelationships?.tracks?.size ?: 0
                } else {

                    val artwork = Artwork(
                        height = 600,
                        width = 600,
                        url = null,
                        uri = null,
                        bitmap = bitmap,
                        bgColor = null,
                        textColor1 = null,
                        textColor2 = null,
                        textColor3 = null,
                        textColor4 = null
                    )

                    val albumRelationships = AlbumRelationships(
                        artists = mutableListOf(cursor.getLong(ARTIST_ID_Index)),
                        genres = mutableListOf(cursor.getLong(GENRE_ID_Index)),
                        tracks = mutableListOf(song.id),
                        library = mutableListOf(),
                        recordLabels = mutableListOf()
                    )

                    val albumAttributes = AlbumAttributes(
                        artistName = song.attributes.artistName ?: "Unknow Artist",
                        albumName = song.attributes.albumName ?: "Unknown Album",
                        artwork = artwork,
                        genreNames = listOf(cursor.getStringOrNull(GENRE_Index) ?: ""),
                        isSingle = false,
                        isCompilation = false,
                        isComplete = true,
                        url = "",
                        trackCount = 1,
                        audioVariants = null,
                        artistUrl = null,
                        contentRating = null,
                        editorialNotes = null,
                        inFavorites = null,
                        recordLabel = null,
                        releaseDate = null
                    )

                    val album = Album(
                        id = cursor.getLong(ALBUM_ID_Index),
                        href = "",
                        albumAttributes = albumAttributes,
                        albumRelationships = albumRelationships,
                        albumViews = AlbumViews(emptyList(), emptyList())
                    )

                    albumList.add(album)

                }

            }
        }

        _rawSongList.update { songList }
        _rawAlbumList.update { albumList }
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

    fun fetchSongsFromAlbum(album: Album): List<Song> {
        val songs = mutableListOf<Song>()
        album.albumRelationships?.tracks?.forEach { id ->
            _rawSongList.value.forEach { song ->
                if (song.id == id) {
                    songs.add(song)
                    return@forEach
                }
            }
        }
        return songs
    }

    val artworkFetcher = CoroutineScope(Dispatchers.IO)


    // MUSIC BRAINZ

    suspend fun syncAlbumLibraryWithMusicBrainZ() {
        _mediaState.update { MediaManagerState.LOADING_RAW }
        val syncList: MutableMap<Int, List<Album>> = mutableMapOf()

        _rawAlbumList.value.forEachIndexed { index, album ->

            delay(1200.milliseconds)

            val searchReleases = musicBrainz.searchReleases(album.albumAttributes.albumName, null)

            if (searchReleases?.releases != null && searchReleases.releases.isNotEmpty()) {
                var count = 2
                if (searchReleases.releases.size < 2) count = searchReleases.releases.size
                val resultList = mutableListOf<Album>()
                for (releaseIndex in 0..<count) {
                    val release = searchReleases.releases[releaseIndex]

                    val coverArt = musicBrainz.getReleaseCover(release.id)

                    val artwork = Artwork(
                        height = 0,
                        width = 0,
                        url = if (!coverArt?.images.isNullOrEmpty()) coverArt.images[0].image else null
                    )

                    val attributes = AlbumAttributes(
                        artistName = if (release.artistCredit.isNotEmpty()) release.artistCredit[0].name else "Unknown",
                        albumName = release.title ?: " Unknown Album",
                        artwork = artwork,
                        genreNames = emptyList(),
                        isSingle = release.trackCount == 1,
                        isCompilation = false,
                        isComplete = false,
                        url = "",
                        trackCount = release.trackCount
                    )

                    resultList.add(
                        Album(
                            id = album.id,
                            href = release.id,
                            albumAttributes = attributes,
                            albumRelationships = null,
                            albumViews = null
                        )
                    )


                }

                syncList[index] = resultList
            }

        }

        _searchAlbumList.update { syncList.toMap() }
        _mediaState.update { MediaManagerState.FREE }

    }

}

enum class MediaManagerState {
    LOADING_RAW,
    LOADING_INFO,
    FREE

}