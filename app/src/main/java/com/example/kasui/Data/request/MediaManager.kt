package com.example.kasui.Data.request


import android.content.ContentUris
import android.content.Context
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.tooling.parseSourceInformation
import androidx.core.database.getIntOrNull
import androidx.core.database.getStringOrNull
import androidx.core.net.toFile
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.application
import coil3.Bitmap
import com.example.kasui.Data.MusicBrainZ.musicBrainz
import com.example.kasui.Data.local.AlbumDatabase
import com.example.kasui.Data.local.AlbumRepository
import com.example.kasui.Data.structure.Artwork
import com.example.kasui.Data.structure.Genre
import com.example.kasui.Data.structure.Lyric
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
import com.example.kasui.TagLib
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
import timber.log.Timber
import java.io.File
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

    fun saveTracks(tracks: List<Track>) {
        if (albumRepository != null) {
            albumRepository?.addTracks(tracks)
        }
    }

    fun loadTags(context: Context, tracks: List<Track>, force: Boolean = false): List<Track> {
        _mediaState.update { MediaManagerState.LOADING_INFO }
        var startTime = System.currentTimeMillis()
        println("tags loading ${tracks.size} ${System.currentTimeMillis() - startTime}")
        val listOfIds = mutableListOf<Long>();

        startTime = System.currentTimeMillis()

        val pfds = tracks.mapNotNull {
            if ((it.isTagLoaded && !force) || it.fileType != FILETYPES.FLAC) {
                null
            } else {
                val fd = context.contentResolver.openFileDescriptor(it.uri, "r")
                if (fd != null) {
                    listOfIds.add(it.id)
                }
                fd
            }
        }
        val fds = pfds.map { it.detachFd() }.toIntArray()

        println("loaded fds - ${fds.size} in ${System.currentTimeMillis() - startTime}")

        if (fds.isEmpty()) {
            _mediaState.update { MediaManagerState.FREE }
            return emptyList()
        }

        startTime = System.currentTimeMillis()

        val tags: List<Map<String, String>> = TagLib.stringFromJNI(fds, listOfIds.toLongArray())

        println("parsed tags ${tags.size} in ${System.currentTimeMillis() - startTime}")
        startTime = System.currentTimeMillis()

        val tagged = tags.mapNotNull { tagTrack ->
            val track = tracks.find { it.id.toString() == tagTrack["id"] }
            if (track != null) {
                track.albumName = tagTrack["ALBUM"] ?: track.albumName
                track.name = tagTrack["TITLE"] ?: track.name
                track.artistName = tagTrack["ARTIST"] ?: track.artistName
                track.discNumber = tagTrack["DISCNUMBER"]?.toIntOrNull()
                track.trackNumber = tagTrack["TRACKNUMBER"]?.toIntOrNull()
                if (!track.hasLyrics) {
                    track.lyrics =
                        tagTrack["LYRICS"] ?: tagTrack["SYNCEDLYRICS"] ?: tagTrack["UNSYNCEDLYRICS"]
                    if (track.lyrics != null && (track.lyrics?.isNotEmpty() ?: false)) {
                        track.hasLyrics = true
                    }
                }
                track.releaseDate = tagTrack["DATE"]
                track.publisher = tagTrack["PUBLISHER"] ?: tagTrack["LABEL"]
                track.comment = tagTrack["COMMENT"]
                track.isTagLoaded = true;

            }
            track
        }
        Timber.d("tagged ${tagged.size} in ${System.currentTimeMillis() - startTime}")
        Timber.d(tagged.toString())

        pfds.forEach {
            it.close()
        }

        println("tags loaded")

        _mediaState.update { MediaManagerState.FREE }

        return tagged

    }


    suspend fun fetchMusicFiles(context: Context) {
        val tracks = albumRepository!!.getAllTracksNow()

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
            MediaStore.Audio.Media.MIME_TYPE,
            MediaStore.Audio.Media.DATA
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val insertAlbum = mutableListOf<InsertAlbum>()

        context.contentResolver.query(uri, projection, selection, null, null)?.use { cursor ->

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
            val MIME_TYPE_INDEX = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val DATA_INDEX = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            if (cursor.isLast) {
                println("No Music file found")
                return
            }

            while (cursor.moveToNext()) {
                val musicUri = ContentUris.withAppendedId(uri, cursor.getLong(_ID_Index))
                val albumUri = ContentUris.withAppendedId(uri, cursor.getLong(ALBUM_ID_Index))
                val mime_type = cursor.getStringOrNull(MIME_TYPE_INDEX)

                val path = cursor.getStringOrNull(DATA_INDEX)

                val musicId = cursor.getLong(_ID_Index)
                if (tracks.find { it.id == musicId } != null) {
                    println("SKIPPING TRACK LOADING - ID : $musicId")
                    continue
                }


                val filetype = if (mime_type == "audio/mpeg" || mime_type == "audio/mp3") {
                    FILETYPES.MP3
                } else if (mime_type == "audio/flac" || mime_type == "audio/x-flac") {
                    FILETYPES.FLAC
                } else {
                    FILETYPES.OTHER
                }
                val track = Track(
                    id = cursor.getLong(_ID_Index),
                    uri = musicUri,
                    albumName = cursor.getStringOrNull(ALBUM_Index),
                    artistName = cursor.getStringOrNull(ARTIST_Index),
                    name = cursor.getStringOrNull(TITLE_Index) ?: "Unknow Title",
                    durationInMillis = cursor.getIntOrNull(DURATION_Index) ?: 0,
                    hasLyrics = false,
                    albumId = cursor.getLong(ALBUM_ID_Index),
                    fileType = filetype,

                    )

                if (path != null) {
                    val lrcFilePath = path.substringBeforeLast(".") + ".lrc"
                    val lrcFile = File(lrcFilePath)

                    if (lrcFile.exists()) {
                        track.lyrics = lrcFile.readText()
                        val parsed = parseSyncedLyrics(lrcFile.readText())
                        track.lyricsSynced = parsed
                        track.hasLyrics = true
                        track.isSynced = true

                    }

                }


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

        _insertAlbums.value.forEach {
            albumRepository?.addAlbumNow(it.albumEntity, it.genre, it.tracks)
        }

        _mediaState.update { MediaManagerState.FREE }

        val updatedTracks = albumRepository?.getAllTracksNow()
        if (!updatedTracks.isNullOrEmpty()) {
            val tagged = loadTags(context, updatedTracks)
            albumRepository?.addTracks(tagged)
        }


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

    private val lrcRegex = Regex("\\[(\\d{2}):(\\d{2})\\.(\\d{2,3})](.*)")

    fun parseSyncedLyrics(text: String): List<Lyric> {
        return text.lines()
            .mapNotNull { line ->
                val matchResult = lrcRegex.find(line.trim()) ?: return@mapNotNull null

                val min = matchResult.groupValues[1].toLong()
                val sec = matchResult.groupValues[2].toLong()
                val msStr = matchResult.groupValues[3]
                // Handle both 2-digit (hundredths) and 3-digit (milliseconds) formats
                val ms = if (msStr.length == 2) msStr.toLong() * 10 else msStr.toLong()
                val text = matchResult.groupValues[4].trim()

                val totalMs = (min * 60 * 1000) + (sec * 1000) + ms

                Lyric(totalMs, text)
            }.sortedBy { it.timestamp }

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


enum class FILETYPES {
    MP3,
    FLAC,
    OTHER
}


enum class MediaManagerState {
    LOADING_RAW,
    LOADING_INFO,
    FREE

}