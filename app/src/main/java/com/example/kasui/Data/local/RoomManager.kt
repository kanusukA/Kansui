package com.example.kasui.Data.local

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.kasui.Data.structure.Genre
import com.example.kasui.Data.structure.album.Album
import com.example.kasui.Data.structure.album.AlbumEntity
import com.example.kasui.Data.structure.album.AlbumGenreCrossRef
import com.example.kasui.Data.structure.album.AlbumTrackCrossRef
import com.example.kasui.Data.structure.song.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

@Dao
interface AlbumDao {

    @Query("SELECT * FROM albums")
    fun getAllAlbumEntities(): Flow<List<AlbumEntity>>

    @Query("SELECT * FROM genres")
    fun getAllGenres(): Flow<List<Genre>>

    @Query("SELECT * FROM genres WHERE id = :id")
    suspend fun getGenre(id: Long): Genre?

    @Transaction
    @Query("SELECT * FROM album_genre_cross_ref WHERE albumId = :albumId")
    fun getAlbumGenres(albumId: Long): Flow<List<AlbumGenreCrossRef>>

    @Query("SELECT * FROM tracks")
    fun getAllTracks(): Flow<List<Track>>

    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrack(id: Long): Track?

    @Transaction
    @Query("SELECT * FROM album_track_cross_ref WHERE albumId = :albumId")
    fun getAlbumTracks(albumId: Long): Flow<List<AlbumTrackCrossRef>>


    fun getAllAlbums(): Flow<List<Album>> = getAllAlbumEntities().map { albums ->
        albums.map {
            val tracks = getAlbumTracks(it.id).map { trackIds ->
                trackIds.mapNotNull { trackId ->
                    getTrack(trackId.trackId)
                }
            }
            val genres = getAlbumGenres(it.id).map { genreIds ->
                genreIds.mapNotNull { genreId ->
                    getGenre(genreId.genreId)
                }
            }

            Album(
                album = it,
                genres = genres,
                tracks = tracks
            )
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumEntity(albumEntity: AlbumEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenres(genres: List<Genre>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumGenreCrossRef(albumGenreCrossRefs: List<AlbumGenreCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<Track>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumTrackCrossRef(albumTrackCrossRefs: List<AlbumTrackCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: AlbumEntity, genres: List<Genre>, tracks: List<Track>) {
        insertAlbumEntity(album)
        insertGenres(genres)
        val crossRef = genres.map { it ->
            AlbumGenreCrossRef(album.id, it.id)
        }
        insertAlbumGenreCrossRef(crossRef)

        insertTracks(tracks)
        val crossRefTracks = tracks.map {
            AlbumTrackCrossRef(albumId = album.id, trackId = it.id)
        }

        insertAlbumTrackCrossRef(crossRefTracks)

    }

    @Update
    suspend fun updateAlbum(album: AlbumEntity)


}

@Database(
    [(AlbumEntity::class),
        (Genre::class),
        (AlbumGenreCrossRef::class),
        (Track::class),
        (AlbumTrackCrossRef::class)],
    version = 1,
    exportSchema = false
)
@TypeConverters(UriConverter::class, ColorConverter::class)
abstract class AlbumDatabase : RoomDatabase() {

    abstract fun getAlbumDao(): AlbumDao

    companion object {
        @Volatile
        private var INSTANCE: AlbumDatabase? = null

        fun getInstance(context: Context): AlbumDatabase {
            // only one thread of execution at a time can enter this block of code
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlbumDatabase::class.java,
                        "employee_database"
                    ).fallbackToDestructiveMigration(false)
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

class AlbumRepository(private val albumDao: AlbumDao) {

    val albums: Flow<List<Album>> = getAllAlbums()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun addAlbum(nAlbum: AlbumEntity, genres: List<Genre>, tracks: List<Track>) {
        coroutineScope.launch(Dispatchers.IO) {
            albumDao.insertAlbum(nAlbum, genres, tracks)
        }
    }

    fun updateAlbum(nAlbum: AlbumEntity) {
        coroutineScope.launch(Dispatchers.IO) {
            albumDao.updateAlbum(nAlbum)
        }
    }

    fun getAllAlbums(): Flow<List<Album>> {
        return albumDao.getAllAlbums()
    }

}