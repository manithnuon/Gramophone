package com.orobator.android.gramophone.model;

import android.provider.BaseColumns;

/**
 * The LibraryContract specifies the schemas of the SQL tables used for
 * accessing collections of music.
 */
public final class LibraryContract {
    private static final String DATE_TYPE = " DATE";
    private static final String INT_TYPE = " INT";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE" + SongEntry.TABLE_NAME + " (" +
                    SongEntry._ID + " INTEGER PRIMARY KEY," +
                    SongEntry.COLUMN_NAME_ALBUM + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_ALBUM_ARTIST + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_ARTIST + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_BIT_RATE + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_COMPOSER + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_DATE_MODIFIED + DATE_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_DISC_NUMBER + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_DISC_TOTAL + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_DURATION + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_EQUALIZER_PRESET + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_FILE_LOCATION + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_GENRE + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_LAST_PLAYED + DATE_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_PLAY_COUNT + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_RATING + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_SAMPLE_RATE + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_SIZE + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_SKIP_ON_SHUFFLE + INT_TYPE + COMMA_SEP + //Should only be 0 or 1
                    SongEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_TRACK_NUMBER + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_TRACK_TOTAL + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_YEAR + INT_TYPE + COMMA_SEP;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SongEntry.TABLE_NAME;


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public LibraryContract() {
    }

    /* By implementing the BaseColumns interface, inner classes receive a
       primary key _ID field, which is needed for cursor adapters. */

    public static abstract class SongEntry implements BaseColumns {
        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_NAME_ALBUM = "album";
        public static final String COLUMN_NAME_ALBUM_ARTIST = "album artist";
        public static final String COLUMN_NAME_ARTIST = "artist";
        public static final String COLUMN_NAME_BIT_RATE = "bit rate";
        public static final String COLUMN_NAME_COMPOSER = "composer";
        public static final String COLUMN_NAME_DATE_MODIFIED = "modified";
        public static final String COLUMN_NAME_DISC_NUMBER = "disc number";
        public static final String COLUMN_NAME_DISC_TOTAL = "total discs";
        public static final String COLUMN_NAME_DURATION = "duration"; // in milliseconds
        public static final String COLUMN_NAME_EQUALIZER_PRESET = "equalizer preset";
        public static final String COLUMN_NAME_FILE_LOCATION = "location";
        public static final String COLUMN_NAME_GENRE = "genre";
        public static final String COLUMN_NAME_LAST_PLAYED = "last played";
        public static final String COLUMN_NAME_PLAY_COUNT = "play count";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_SAMPLE_RATE = "sample rate";
        public static final String COLUMN_NAME_SIZE = "size";
        public static final String COLUMN_NAME_SKIP_ON_SHUFFLE = "shuffle skip";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TRACK_NUMBER = "track number";
        public static final String COLUMN_NAME_TRACK_TOTAL = "total tracks";
        public static final String COLUMN_NAME_YEAR = "year";

    }

    /**
     * Resources:
     * SQL Tables: http://developer.android.com/training/basics/data-storage/databases.html
     * MetaDataRetriever: http://developer.android.com/reference/android/media/MediaMetadataRetriever.html
     * MediaPlayer: http://developer.android.com/reference/android/media/MediaPlayer.html
     * MediaPlayer.TrackInfo: http://developer.android.com/reference/android/media/MediaPlayer.TrackInfo.html
     * MediaStore: http://developer.android.com/reference/android/provider/MediaStore.html
     */



    /* I'm probably going to need a couple of different table types.
    * Table type 1: Songs
    * Table type 2: Artists
    * Table type 3: Genres
    * Table type 4: Albums
    * Table type 5: Playlists
    * */

}
