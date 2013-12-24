package com.orobator.android.gramophone.model;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Library {
    private static final String TAG = "Library";
    private static ArrayList<Song> sSongs;
    private static ArrayList<Album> sAlbums;
    private static ArrayList<Artist> sArtists;
    private static ArrayList<String> sGenres;
    private static Library sLibrary;
    private static MediaMetadataRetriever sMetadataRetriever;
    private Context mAppContext;
    private SongDatabaseHelper mHelper;

    // TODO Make your own songs table with an Album_Artist column

    private Library(Context appContext) {
        mAppContext = appContext;
        sMetadataRetriever = new MediaMetadataRetriever();
        mHelper = new SongDatabaseHelper(mAppContext);
    }

    public static Library getLibrary(Context context) {
        if (sLibrary == null) {
            sLibrary = new Library(context);
        }
        return sLibrary;
    }

    public ArrayList<Album> getAlbums(Artist artist) {
        ArrayList<Album> albums = new ArrayList<Album>();

        String mSelection = AudioColumns.IS_MUSIC + "=1 AND "
                + AudioColumns.ALBUM + " != '' AND "
                + AudioColumns.ARTIST + " = ? ";

        String mProjection[] = {AudioColumns.ALBUM};
        String mSelectionArgs[] = {artist.getName()};

        Cursor mCursor = mAppContext
                .getContentResolver()
                .query(
                        Media.EXTERNAL_CONTENT_URI,
                        mProjection,
                        mSelection,
                        mSelectionArgs,
                        AudioColumns.ALBUM_KEY
                );

        Log.d(TAG, "Found " + mCursor.getCount() + " albums");
        mCursor.moveToNext();
        while (!mCursor.isAfterLast()) {
            Album album = new Album();
            album.setAlbumName(mCursor.getString(0));
            album.setAlbumArtist(artist.getName());
            if (!albums.contains(album)) {
                albums.add(album);
            }
            mCursor.moveToNext();
        }
        mCursor.close();

        return albums;

    }

    public ArrayList<Album> getAlbums() {
        if (sAlbums != null) {
            return sAlbums;
        }

        sAlbums = new ArrayList<Album>();

        final StringBuilder mSelection = new StringBuilder();
        mSelection.append(AudioColumns.IS_MUSIC + "=1 AND ");
        mSelection.append(AudioColumns.ALBUM + " != ''");
        String mProjection[] =
                {
                        MediaStore.Audio.AlbumColumns.ALBUM,
                        MediaStore.Audio.AlbumColumns.ARTIST
                };
        Cursor mCursor = mAppContext
                .getContentResolver()
                .query(
                        Media.EXTERNAL_CONTENT_URI,
                        mProjection,
                        mSelection.toString(),
                        null,
                        AudioColumns.ALBUM_KEY
                );

        Log.d(TAG, "Found " + mCursor.getCount() + " albums");
        mCursor.moveToNext();
        while (!mCursor.isAfterLast()) {
            Album album = new Album();
            album.setAlbumName(mCursor.getString(0));
            album.setAlbumArtist(mCursor.getString(1));
            if (!sAlbums.contains(album)) {
                sAlbums.add(album);
            }
            mCursor.moveToNext();
        }
        mCursor.close();

        return sAlbums;
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

    public ArrayList<Artist> getArtists() {
        if (sArtists != null) {
            return sArtists;
        }

        sArtists = new ArrayList<Artist>();

        final StringBuilder mSelection = new StringBuilder();
        mSelection.append(AudioColumns.IS_MUSIC + "=1");
        mSelection.append(" AND " + AudioColumns.ARTIST + " != ''");
        String mProjection[] = {AudioColumns.ARTIST};
        Cursor mCursor = mAppContext
                .getContentResolver()
                .query(
                        Media.EXTERNAL_CONTENT_URI,
                        mProjection,
                        mSelection.toString(),
                        null,
                        AudioColumns.ARTIST_KEY
                );

        mCursor.moveToNext();
        while (!mCursor.isAfterLast()) {
            Artist artist = new Artist();
            artist.setName(mCursor.getString(0));
            if (!sArtists.contains(artist)) {
                sArtists.add(artist);
            }
            mCursor.moveToNext();
        }
        mCursor.close();

        return sArtists;

    }

    public ArrayList<String> getGenres() {
        if (sGenres != null) {
            return sGenres;
        }

        sGenres = new ArrayList<String>();
        String mSelection = MediaStore.Audio.Genres.NAME + " != ''"; //TODO Why isn't this working???
        Cursor mCursor = mAppContext
                .getContentResolver()
                .query(
                        MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Audio.Genres.NAME},
                        mSelection,
                        null,
                        MediaStore.Audio.Genres.NAME);

        mCursor.moveToNext();

        while (!mCursor.isAfterLast()) {
            sGenres.add(mCursor.getString(0));
            mCursor.moveToNext();
        }

        return sGenres;
    }

    public ArrayList<Song> getSongs(Album album) {
        ArrayList<Song> songs = new ArrayList<Song>();

        final StringBuilder mSelection = new StringBuilder();
        mSelection.append(AudioColumns.IS_MUSIC + "=1");
        mSelection.append(" AND " + AudioColumns.ARTIST + " = ?");
        mSelection.append(" AND " + AudioColumns.ALBUM + " = ?");
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

        Cursor mCursor = mAppContext
                .getContentResolver()
                .query(
                        Media.EXTERNAL_CONTENT_URI,
                        mProjection,
                        mSelection.toString(),
                        new String[]{album.getAlbumArtist(), album.getAlbumName()},
                        AudioColumns.TRACK);

        mCursor.moveToNext();
        Log.i(TAG, "Found " + mCursor.getCount() + " songs for album: "
                + album.getAlbumName() + " - " + album.getAlbumArtist());

        while (!mCursor.isAfterLast()) {
            Song song = new Song();
            song.setLocation(mCursor.getString(3));
            song.setAlbum(mCursor.getString(0));
            song.setArtist(mCursor.getString(1));
            song.setComposer(mCursor.getString(2));
            song.setDateModified(new Date(Long.parseLong(mCursor.getString(4))));
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
            song.setDateModified(new Date(Long.parseLong(mCursor.getString(4))));
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
            song.setDateModified(new Date(Long.parseLong(mCursor.getString(4))));
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

    public ArrayList<Song> getSongs() {
        if (sSongs != null) {
            return sSongs;
        }

        sSongs = new ArrayList<Song>();

        final StringBuilder mSelection = new StringBuilder();
        mSelection.append(AudioColumns.IS_MUSIC + "=1");
        mSelection.append(" AND " + AudioColumns.TITLE + " != ''");
        Cursor mCursor = mAppContext
                .getContentResolver()
                .query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        new String[]{AudioColumns.ALBUM, AudioColumns.ARTIST,
                                AudioColumns.COMPOSER, AudioColumns.DISPLAY_NAME,
                                AudioColumns.DATE_MODIFIED, AudioColumns.DURATION,
                                AudioColumns.TITLE, AudioColumns.TRACK,
                                AudioColumns.SIZE, AudioColumns.YEAR},
                        mSelection.toString(), null, Media.DEFAULT_SORT_ORDER);

        mCursor.moveToNext();

        Log.i(TAG, "Found " + mCursor.getCount() + " songs");
        while (!mCursor.isAfterLast()) {
            Song song = new Song();
            song.setLocation(mCursor.getString(3));
            song.setAlbum(mCursor.getString(0));
            song.setArtist(mCursor.getString(1));
            song.setComposer(mCursor.getString(2));
            song.setDateModified(new Date(Long.parseLong(mCursor.getString(4))));
            song.setDuration(Long.parseLong(mCursor.getString(5)));
            song.setTitle(mCursor.getString(6));
            song.setSize(Long.parseLong(mCursor.getString(8)));
            String year = mCursor.getString(9);
            if (year != null) {
                song.setYear(Integer.parseInt(mCursor.getString(9)));
            } else {
                song.setYear(-1);
            }
            sSongs.add(song);
            mCursor.moveToNext();
        }
        mCursor.close();

        return sSongs;

    }

    private void setSongMetadata(Song song, File songFile) {
        song.setAlbum(sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        song.setAlbumArtist(sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));
        song.setArtist(sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

        String bitRate = sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE);
        if (bitRate != null) {
            song.setBitRate(Integer.parseInt(bitRate));
        }

        String trackNumber = sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER);
        if (trackNumber != null) {
            String[] trackInfo = trackNumber.split("/");
            song.setTrackNumber(Integer.parseInt(trackInfo[0]));
            if (trackInfo.length > 1) {
                song.setTrackCount(Integer.parseInt(trackInfo[1]));
            }
        }

        String dateModified = sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        if (dateModified != null) {
            song.setDateModified(new Date(dateModified));
        } else {
            song.setDateModified(new Date());
        }

        song.setCompilationStatus(sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION));
        song.setComposer(sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER));

        String discNumber = sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER);
        if (discNumber != null) {
            String[] discNumberInfo = discNumber.split("/");
            song.setDiscNumber(Integer.parseInt(discNumberInfo[0]));
            if (discNumber.length() > 1) {
                song.setDiscCount(Integer.parseInt(discNumberInfo[1]));
            }
        }

        song.setDuration(Long.parseLong(sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
        song.setGenre(sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));

        String numTracks = sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS);
        if (numTracks != null) {
            song.setNumTracks(Integer.parseInt(numTracks));
        }

        song.setTitle(sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        song.setWriter(sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER));
        String year = sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);
        if (year != null) {
            song.setYear(Integer.parseInt(year));
        }

        song.setSize(songFile.length());
        song.setFileName(songFile.getName());

        song.setHasArtwork(sMetadataRetriever.getEmbeddedPicture() == null);

        //Free resources when done
        sMetadataRetriever.release();
        sMetadataRetriever = null;

    }

}