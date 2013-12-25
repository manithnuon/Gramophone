package com.orobator.android.gramophone.model;

import android.provider.BaseColumns;

/**
 * The LibraryContract specifies the schemas of the SQL tables used for
 * accessing collections of music.
 */
public final class LibraryContract {



    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public LibraryContract() {
    }

    /* By implementing the BaseColumns interface, inner classes receive a
       primary key _ID field, which is needed for cursor adapters. */

    public static abstract class SongEntry implements BaseColumns {
        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_NAME_ALBUM = "album";
        public static final String COLUMN_NAME_ALBUM_ARTIST = "album_artist";
        public static final String COLUMN_NAME_ARTIST = "artist";
        public static final String COLUMN_NAME_BIT_RATE = "bit_rate";
        public static final String COLUMN_NAME_COMPOSER = "composer";
        public static final String COLUMN_NAME_DATE_MODIFIED = "modified";
        public static final String COLUMN_NAME_DISC_NUMBER = "disc_number";
        public static final String COLUMN_NAME_DISC_TOTAL = "total_discs";
        public static final String COLUMN_NAME_DURATION = "duration"; // in milliseconds
        public static final String COLUMN_NAME_EQUALIZER_PRESET = "equalizer_preset";
        public static final String COLUMN_NAME_FILE_LOCATION = "location";
        public static final String COLUMN_NAME_GENRE = "genre";
        public static final String COLUMN_NAME_HAS_ARTWORK = "artwork";
        public static final String COLUMN_NAME_LAST_PLAYED = "last_played";
        public static final String COLUMN_NAME_PLAY_COUNT = "play_count";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_SAMPLE_RATE = "sample_rate";
        public static final String COLUMN_NAME_SIZE = "size";
        public static final String COLUMN_NAME_SKIP_COUNT = "skip_count";
        public static final String COLUMN_NAME_SKIP_ON_SHUFFLE = "shuffle_skip";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TRACK_NUMBER = "track_number";
        public static final String COLUMN_NAME_TRACK_TOTAL = "total_tracks";
        public static final String COLUMN_NAME_YEAR = "year";
    }

    public static abstract class AlbumEntry implements BaseColumns {
        public static final String TABLE_NAME = "albums";
        public static final String COLUM_NAME_ALBUM_NAME = "album_name";
        public static final String COLUMN_NAME_ALBUM_ARTIST = "album_artist";
    }

}
