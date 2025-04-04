package cu.lt.joe.mc.lyrics.db;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import cu.lt.joe.mc.lyrics.models.Album;
import cu.lt.joe.mc.lyrics.models.Song;

public class MainDatabaseHandler
{
    private static final String ALBUMS_TABLE_TITLE = "ALBUMS_TABLE", SONGS_TABLE_TITLE = "SONGS_TABLE";
    private static final String KEY_ALBUM_ID = "ID", KEY_ALBUM_TITLE = "ALBUM_TITLE", KEY_ALBUM_YEAR = "YEAR", KEY_ALBUM_LABEL = "RECORDING_LABEL", KEY_ALBUM_WIKILINK = "WIKIPEDIA_LINK",
            KEY_SONG_ID = "ID", KEY_SONG_TITLE = "TITLE", KEY_SONG_LYRICS = "LYRICS", KEY_SONG_WRITERS = "WRITERS", KEY_YT_LINK = "YT_LINK";
    private final DatabaseOpener dbOpener;
    private final Context context;

    public MainDatabaseHandler(Context context)
    {
        this.dbOpener = new DatabaseOpener(context);
        this.context = context;
    }

    public ArrayList<Album> getAlbumsArray()
    {
        ArrayList<Album> albums = new ArrayList<>();
        SQLiteDatabase db = dbOpener.openOrCreateDatabase(context.getApplicationInfo().dataDir + "/databases/songs.db", ContextWrapper.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + ALBUMS_TABLE_TITLE, null);
        int iId = cursor.getColumnIndex(KEY_ALBUM_ID);
        int iTitle = cursor.getColumnIndex(KEY_ALBUM_TITLE);
        int iYear = cursor.getColumnIndex(KEY_ALBUM_YEAR);
        int iLabel = cursor.getColumnIndex(KEY_ALBUM_LABEL);
        int iWiki = cursor.getColumnIndex(KEY_ALBUM_WIKILINK);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
        {
            Long Aid = cursor.getLong(iId);
            albums.add(new Album(Aid, cursor.getString(iTitle), cursor.getString(iLabel), getSongsForAlbum(Aid), cursor.getInt(iYear), cursor.getString(iWiki)));
        }
        cursor.close();
        db.close();
        return albums;
    }

    private ArrayList<Song> getSongsForAlbum(Long albumId)
    {
        ArrayList<Song> songs = new ArrayList<>();
        SQLiteDatabase db = dbOpener.openOrCreateDatabase(context.getApplicationInfo().dataDir + "/databases/songs.db", ContextWrapper.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + SONGS_TABLE_TITLE + " WHERE ALBUM_ID=" + albumId, null);
        int iId = cursor.getColumnIndex(KEY_SONG_ID);
        int iTitle = cursor.getColumnIndex(KEY_SONG_TITLE);
        int iLyrics = cursor.getColumnIndex(KEY_SONG_LYRICS);
        int iWriters = cursor.getColumnIndex(KEY_SONG_WRITERS);
        int iLink = cursor.getColumnIndex(KEY_YT_LINK);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            songs.add(new Song(cursor.getLong(iId), cursor.getString(iTitle), cursor.getString(iLyrics), cursor.getString(iWriters), cursor.getString(iLink)));
        cursor.close();
        db.close();
        return songs;
    }

    private static class DatabaseOpener extends ContextWrapper
    {
        public DatabaseOpener(Context base)
        {
            super(base);
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, @Nullable DatabaseErrorHandler errorHandler)
        {
            return openOrCreateDatabase(name, mode, factory);
        }

        @Override
        public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory)
        {
            return SQLiteDatabase.openOrCreateDatabase(name, null);
        }
    }
}