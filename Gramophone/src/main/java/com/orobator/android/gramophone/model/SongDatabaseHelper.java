package com.orobator.android.gramophone.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SongDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "songs.sqlite";
    private static final int VERSION = 1;
    private static final String TABLE_SONG = "song";
    private static final String COLUMN_SONG_ID = "_id";
    private static final String COLUMN_SONG_TITLE = "title";
    private static final String COLUMN_SONG_DURATION = "duration";
    private static final String COLUMN_SONG_ARTIST = "artist";
    private static final String COLUMN_SONG_ALBUM = "album";
    private static final String COLUMN_SONG_GENRE = "genre";
    private static final String COLUMN_SONG_RATING = "rating";
    private static final String COLUMN_SONG_PLAY_COUNT = "play_count";
    private static final String COLUMN_SONG_SKIP_COUNT = "skip_count";
    private static final String COLUMN_SONG_SIZE = "size";
    private static final String COLUMN_SONG_HAS_ARTWORK = "has_artwork";
    private static final String COLUMN_SONG_DISC_NUMBER = "disc_number";
    private static final String COLUMN_SONG_DISC_COUNT = "disc_count";
    private static final String COLUMN_SONG_TRACK_NUMBER = "track_number";
    private static final String COLUMN_SONG_TRACK_COUNT = "track_count";
    private static final String COLUMN_SONG_YEAR = "year";
    private static final String COLUMN_SONG_NUM_TRACKS = "num_tracks"; //Number of tracks on the album
    private static final String COLUMN_SONG_BIT_RATE = "bit_rate";
    private static final String COLUMN_SONG_SAMPLE_RATE = "sample_rate";
    private static final String COLUMN_SONG_ALBUM_ARTIST = "album_artist";
    private static final String COLUMN_SONG_COMPILATION_STATUS = "compilation_status";
    private static final String COLUMN_SONG_COMPOSER = "composer";
    private static final String COLUMN_SONG_WRITER = "writer";
    private static final String COLUMN_SONG_FILENAME = "filename";
    private static final String COLUMN_SONG_LOCATION = "location";
    private static final String COLUMN_SONG_DATE_MODIFIED = "date_modified";

    public SongDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public SongCursor queryRuns() {
        // Equivalent to "select * from song order by song_title asc"
        Cursor wrapped = getReadableDatabase().query(TABLE_SONG, null, null,
                null, null, null, COLUMN_SONG_TITLE + " asc");
        return new SongCursor(wrapped);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "song" table
        db.execSQL(
                "create table song"
                        + "("
                        + "_id integer primary key autoincrement,"
                        + "title text not null,"
                        + "duration bigint,"
                        + "artist text,"
                        + "album text,"
                        + "genre text,"
                        + "rating integer,"
                        + "play_count integer,"
                        + "skip_count integer,"
                        + "size bigint,"
                        + "has_artwork boolean,"
                        + "disc_number integer,"
                        + "disc_count integer,"
                        + "track_number integer,"
                        + "track_count integer,"
                        + "year integer,"
                        + "num_tracks integer,"
                        + "bit_rate integer,"
                        + "sample_rate integer,"
                        + "album_artist text,"
                        + "compilation_status text,"
                        + "composer text,"
                        + "writer text,"
                        + "filename text,"
                        + "location text,"
                        + "date_modified date)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement schema changes and data massage here when upgrading
    }

    public long insertSong(Song song) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SONG_TITLE, song.getTitle());
        cv.put(COLUMN_SONG_DURATION, song.getDuration());
        cv.put(COLUMN_SONG_ARTIST, song.getArtist());
        cv.put(COLUMN_SONG_ALBUM, song.getAlbum());
        cv.put(COLUMN_SONG_GENRE, song.getGenre());
        cv.put(COLUMN_SONG_RATING, song.getRating());
        cv.put(COLUMN_SONG_PLAY_COUNT, song.getPlayCount());
        cv.put(COLUMN_SONG_SKIP_COUNT, song.getSkipCount());
        cv.put(COLUMN_SONG_SIZE, song.getSize());
        cv.put(COLUMN_SONG_HAS_ARTWORK, song.hasArtwork());
        cv.put(COLUMN_SONG_DISC_NUMBER, song.getDiscNumber());
        cv.put(COLUMN_SONG_DISC_COUNT, song.getDiscCount());
        cv.put(COLUMN_SONG_TRACK_NUMBER, song.getTrackNumber());
        cv.put(COLUMN_SONG_TRACK_COUNT, song.getTrackCount());
        cv.put(COLUMN_SONG_YEAR, song.getYear());
        cv.put(COLUMN_SONG_NUM_TRACKS, song.getNumTracks());
        cv.put(COLUMN_SONG_BIT_RATE, song.getBitRate());
        cv.put(COLUMN_SONG_SAMPLE_RATE, song.getSampleRate());
        cv.put(COLUMN_SONG_ALBUM_ARTIST, song.getAlbumArtist());
        cv.put(COLUMN_SONG_COMPILATION_STATUS, song.getCompilationStatus());
        cv.put(COLUMN_SONG_COMPOSER, song.getComposer());
        cv.put(COLUMN_SONG_WRITER, song.getWriter());
        cv.put(COLUMN_SONG_FILENAME, song.getFileName());
        cv.put(COLUMN_SONG_LOCATION, song.getLocation());
        cv.put(COLUMN_SONG_DATE_MODIFIED, song.getDateModified().toString());

        return getWritableDatabase().insert(TABLE_SONG, null, cv);
    }

    /**
     * A convenience class to wrap a cursor that returns rows from the "song"
     * table. The {@link getSong()} method returns a Song instance representing
     * the current row.
     */
    public static class SongCursor extends CursorWrapper {
        public SongCursor(Cursor cursor) {
            super(cursor);
        }

        /**
         * Returns a Song object configured for the current row, or null if the
         * current row is invalid
         */
        public Song getSong() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            Song song = new Song();

            return song;
        }
    }


}
