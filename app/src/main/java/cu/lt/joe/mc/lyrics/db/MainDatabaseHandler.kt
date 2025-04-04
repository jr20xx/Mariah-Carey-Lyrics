package cu.lt.joe.mc.lyrics.db

import android.content.Context
import android.content.ContextWrapper
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import cu.lt.joe.mc.lyrics.models.Album
import cu.lt.joe.mc.lyrics.models.Song

class MainDatabaseHandler(context: Context) {
    private val dbOpener: DatabaseOpener
    private val context: Context

    init {
        dbOpener = DatabaseOpener(context)
        this.context = context
    }

    val albumsArray: ArrayList<Album>
        get() {
            val albums = ArrayList<Album>()
            val db =
                dbOpener.openOrCreateDatabase(context.applicationInfo.dataDir + "/databases/songs.db", ContextWrapper.MODE_PRIVATE, null)
            val cursor = db.rawQuery("SELECT * FROM $ALBUMS_TABLE_TITLE", null)
            val iId = cursor.getColumnIndex(KEY_ALBUM_ID)
            val iTitle = cursor.getColumnIndex(KEY_ALBUM_TITLE)
            val iYear = cursor.getColumnIndex(KEY_ALBUM_YEAR)
            val iLabel = cursor.getColumnIndex(KEY_ALBUM_LABEL)
            val iWiki = cursor.getColumnIndex(KEY_ALBUM_WIKILINK)
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val albumId = cursor.getLong(iId)
                albums.add(Album(albumId, cursor.getString(iTitle), cursor.getString(iLabel), getSongsForAlbum(albumId), cursor.getInt(iYear), cursor.getString(iWiki)))
                cursor.moveToNext()
            }
            cursor.close()
            db.close()
            return albums
        }

    private fun getSongsForAlbum(albumId: Long): ArrayList<Song> {
        val songs = ArrayList<Song>()
        val db =
            dbOpener.openOrCreateDatabase(context.applicationInfo.dataDir + "/databases/songs.db", ContextWrapper.MODE_PRIVATE, null)
        val cursor =
            db.rawQuery("SELECT * FROM $SONGS_TABLE_TITLE WHERE ALBUM_ID=$albumId", null)
        val iId = cursor.getColumnIndex(KEY_SONG_ID)
        val iTitle = cursor.getColumnIndex(KEY_SONG_TITLE)
        val iLyrics = cursor.getColumnIndex(KEY_SONG_LYRICS)
        val iWriters = cursor.getColumnIndex(KEY_SONG_WRITERS)
        val iLink = cursor.getColumnIndex(KEY_YT_LINK)
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            songs.add(Song(cursor.getLong(iId), cursor.getString(iTitle), cursor.getString(iLyrics), cursor.getString(iWriters), cursor.getString(iLink)))
            cursor.moveToNext()
        }
        cursor.close()
        db.close()
        return songs
    }

    private class DatabaseOpener(base: Context?) : ContextWrapper(base) {
        override fun openOrCreateDatabase(name: String, mode: Int, factory: CursorFactory?, errorHandler: DatabaseErrorHandler?): SQLiteDatabase {
            return openOrCreateDatabase(name, mode, factory)
        }

        override fun openOrCreateDatabase(name: String, mode: Int, factory: CursorFactory?): SQLiteDatabase {
            return SQLiteDatabase.openOrCreateDatabase(name, null)
        }
    }

    companion object {
        private const val ALBUMS_TABLE_TITLE = "ALBUMS_TABLE"
        private const val SONGS_TABLE_TITLE = "SONGS_TABLE"
        private const val KEY_ALBUM_ID = "ID"
        private const val KEY_ALBUM_TITLE = "ALBUM_TITLE"
        private const val KEY_ALBUM_YEAR = "YEAR"
        private const val KEY_ALBUM_LABEL = "RECORDING_LABEL"
        private const val KEY_ALBUM_WIKILINK = "WIKIPEDIA_LINK"
        private const val KEY_SONG_ID = "ID"
        private const val KEY_SONG_TITLE = "TITLE"
        private const val KEY_SONG_LYRICS = "LYRICS"
        private const val KEY_SONG_WRITERS = "WRITERS"
        private const val KEY_YT_LINK = "YT_LINK"
    }
}