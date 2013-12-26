package com.orobator.android.gramophone.model;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import com.orobator.android.gramophone.model.SongDatabaseHelper.AlbumCursor;
import com.orobator.android.gramophone.model.SongDatabaseHelper.ArtistCursor;
import com.orobator.android.gramophone.model.SongDatabaseHelper.GenreCursor;
import com.orobator.android.gramophone.model.SongDatabaseHelper.SongCursor;

import java.util.ArrayList;

public class Library {
    private static final String TAG = "Library";
    private static Library sLibrary;
    private Context mAppContext;
    private SongDatabaseHelper mHelper;

    private Library(Context appContext) {
        mAppContext = appContext;
        mHelper = new SongDatabaseHelper(mAppContext);
    }

    public static Library getLibrary(Context context) {
        if (sLibrary == null) {
            sLibrary = new Library(context);
        }
        return sLibrary;
    }

    public AlbumCursor getAlbums() {
        return mHelper.queryAlbums();
    }

    public ArtistCursor getArtists() {
        return mHelper.queryArtists();
    }

    public SongCursor getSongs() {
        return mHelper.querySongs();
    }

    public SongCursor getSongsForAlbum(Album album) {
        return mHelper.querySongsForAlbum(album);
    }

    public GenreCursor getGenres() {
        return mHelper.queryGenres();
    }


    public ArrayList<Artist> getArtists(String genre) {
        ArrayList<Artist> artists = new ArrayList<Artist>();

        String mSelection = AudioColumns.IS_MUSIC + "=1 AND "
                + AudioColumns.ARTIST + " != ''"
                + MediaStore.Audio.Genres.NAME + " =?";

        String mProjection[] = {AudioColumns.ARTIST};
        String selectionArgs[] = {genre};
        Cursor mCursor = mAppContext
                .getContentResolver()
                .query(
                        Media.EXTERNAL_CONTENT_URI,
                        mProjection,
                        mSelection,
                        selectionArgs,
                        AudioColumns.ARTIST_KEY);

        mCursor.moveToNext();
        while (!mCursor.isAfterLast()) {
            Artist artist = new Artist();
            artist.setName(mCursor.getString(0));
            if (!artists.contains(artist)) {
                artists.add(artist);
            }
            mCursor.moveToNext();
        }

        mCursor.close();

        return artists;

    }

    public ArrayList<Song> getSongs(String genre) {
        ArrayList<Song> songs = new ArrayList<Song>();

        final StringBuilder mSelection = new StringBuilder();
        mSelection.append(AudioColumns.IS_MUSIC + "=1");
        mSelection.append(" AND " + AudioColumns.TITLE + " != ''");
        mSelection.append(" AND " + MediaStore.Audio.Genres.NAME + " = ?");

        Log.d(TAG, "Selection: " + mSelection.toString());

        String mProjection[] =
                {
                        AudioColumns.ALBUM,
                        AudioColumns.ARTIST,
                        AudioColumns.COMPOSER,
                        AudioColumns.DISPLAY_NAME,
                        AudioColumns.DATE_MODIFIED,
                        AudioColumns.DURATION,
                        AudioColumns.TITLE,
                        AudioColumns.TRACK,
                        AudioColumns.SIZE,
                        AudioColumns.YEAR
                };

        String selectionArgs[] = {genre};
        Cursor mCursor = mAppContext
                .getContentResolver()
                .query(
                        MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                        mProjection,
                        mSelection.toString(),
                        selectionArgs,
                        Media.DEFAULT_SORT_ORDER);

        mCursor.moveToNext();

        Log.i(TAG, "Found " + mCursor.getCount() + " songs");
        while (!mCursor.isAfterLast()) {
            Song song = new Song();
            song.setLocation(mCursor.getString(3));
            song.setAlbum(mCursor.getString(0));
            song.setArtist(mCursor.getString(1));
            song.setComposer(mCursor.getString(2));
            song.setDateModified(Long.parseLong(mCursor.getString(4)));
            song.setDuration(Long.parseLong(mCursor.getString(5)));
            song.setTitle(mCursor.getString(6));
            song.setSize(Long.parseLong(mCursor.getString(8)));
            String year = mCursor.getString(9);
            if (year != null) {
                song.setYear(Integer.parseInt(mCursor.getString(9)));
            } else {
                song.setYear(-1);
            }
            songs.add(song);
            mCursor.moveToNext();
        }
        mCursor.close();

        return songs;
    }

    public ArrayList<Song> getSongs(Artist artist) {
        ArrayList<Song> songs = new ArrayList<Song>();

        String mSelection = AudioColumns.IS_MUSIC + "=1 AND "
                + AudioColumns.TITLE + " != ''"
                + AudioColumns.ARTIST + "=?";

        String mProjection[] =
                {
                        AudioColumns.ALBUM,
                        AudioColumns.ARTIST,
                        AudioColumns.COMPOSER,
                        AudioColumns.DISPLAY_NAME,
                        AudioColumns.DATE_MODIFIED,
                        AudioColumns.DURATION,
                        AudioColumns.TITLE,
                        AudioColumns.TRACK,
                        AudioColumns.SIZE,
                        AudioColumns.YEAR
                };

        String selectionArgs[] = {artist.getName()};
        Cursor mCursor = mAppContext
                .getContentResolver()
                .query(
                        Media.EXTERNAL_CONTENT_URI,
                        mProjection,
                        mSelection, selectionArgs, Media.DEFAULT_SORT_ORDER);

        mCursor.moveToNext();

        Log.i(TAG, "Found " + mCursor.getCount() + " songs");
        while (!mCursor.isAfterLast()) {
            Song song = new Song();
            song.setLocation(mCursor.getString(3));
            song.setAlbum(mCursor.getString(0));
            song.setArtist(mCursor.getString(1));
            song.setComposer(mCursor.getString(2));
            song.setDateModified(Long.parseLong(mCursor.getString(4)));
            song.setDuration(Long.parseLong(mCursor.getString(5)));
            song.setTitle(mCursor.getString(6));
            song.setSize(Long.parseLong(mCursor.getString(8)));
            String year = mCursor.getString(9);
            if (year != null) {
                song.setYear(Integer.parseInt(mCursor.getString(9)));
            } else {
                song.setYear(-1);
            }
            songs.add(song);
            mCursor.moveToNext();
        }
        mCursor.close();

        return songs;
    }


}