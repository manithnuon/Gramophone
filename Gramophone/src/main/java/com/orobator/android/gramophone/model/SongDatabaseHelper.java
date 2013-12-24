package com.orobator.android.gramophone.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.util.Log;

import com.orobator.android.gramophone.model.LibraryContract.SongEntry;

public class SongDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SongDatabaseHelper";
    private static final String DATABASE_NAME = "Library.db";
    private static final int VERSION = 1;
    private static final String BIG_INT_TYPE = " BIG_INT";
    private static final String INT_TYPE = " INT";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + SongEntry.TABLE_NAME + " (" +
                    SongEntry._ID + " INTEGER PRIMARY KEY," +
                    SongEntry.COLUMN_NAME_ALBUM + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_ALBUM_ARTIST + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_ARTIST + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_BIT_RATE + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_COMPOSER + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_DATE_MODIFIED + BIG_INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_DISC_NUMBER + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_DISC_TOTAL + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_DURATION + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_EQUALIZER_PRESET + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_FILE_LOCATION + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_GENRE + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_HAS_ARTWORK + INT_TYPE + COMMA_SEP + // Should only be 0 or 1
                    SongEntry.COLUMN_NAME_LAST_PLAYED + BIG_INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_PLAY_COUNT + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_RATING + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_SAMPLE_RATE + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_SIZE + BIG_INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_SKIP_COUNT + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_SKIP_ON_SHUFFLE + INT_TYPE + COMMA_SEP + // Should only be 0 or 1
                    SongEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_TRACK_NUMBER + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_TRACK_TOTAL + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_YEAR + INT_TYPE + ")";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LibraryContract.SongEntry.TABLE_NAME;
    private Context sContext;

    public SongDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        sContext = context;
    }

    public SongCursor querySongs() {
        // Equivalent to "select * from song order by song_title asc"
        Cursor wrapped = getReadableDatabase()
                .query(
                        SongEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        SongEntry.COLUMN_NAME_TITLE + " asc");
        return new SongCursor(wrapped);
    }

//    public AlbumCursor queryAlbums() {
//        String tableName = SongEntry.TABLE_NAME;
//        String columns[] =
//                {
//                        SongEntry.COLUMN_NAME_ALBUM,
//                        SongEntry.COLUMN_NAME_ALBUM_ARTIST,
//                };
//        String selection = null;
//
//
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        long startTime = System.currentTimeMillis();
        // Create the "song" table
        db.execSQL(SQL_CREATE_ENTRIES);

        // Populate
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String mProjection[] =
                {
                        AudioColumns.ALBUM,
                        AudioColumns.ARTIST,
                        AudioColumns.COMPOSER,
                        AudioColumns.DATA,
                        AudioColumns.DATE_MODIFIED,
                        AudioColumns.DISPLAY_NAME,
                        AudioColumns.DURATION,
                        AudioColumns.TITLE,
                        AudioColumns.TRACK,
                        AudioColumns.SIZE,
                        AudioColumns.YEAR
                };
        String mSelection = AudioColumns.IS_MUSIC + "=1"
                + " AND " + AudioColumns.TITLE + " != ''";

        Cursor mCursor = sContext
                .getContentResolver()
                .query(
                        uri,
                        mProjection,
                        mSelection,
                        null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER
                );

        mCursor.moveToNext();
        Log.i(TAG, "Found " + mCursor.getCount() + " songs");
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        int count = 0;

        while (!mCursor.isAfterLast()) {
            retriever.setDataSource(mCursor.getString(mCursor.getColumnIndex(AudioColumns.DATA)));
            ContentValues cv = new ContentValues();
            cv.put(SongEntry.COLUMN_NAME_ALBUM, mCursor.getString(mCursor.getColumnIndex(AudioColumns.ALBUM)));
            cv.put(SongEntry.COLUMN_NAME_ALBUM_ARTIST, retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));
            cv.put(SongEntry.COLUMN_NAME_ARTIST, mCursor.getString(mCursor.getColumnIndex(AudioColumns.ARTIST)));
            cv.put(SongEntry.COLUMN_NAME_BIT_RATE, Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)));
            cv.put(SongEntry.COLUMN_NAME_COMPOSER, mCursor.getString(mCursor.getColumnIndex(AudioColumns.COMPOSER)));
            cv.put(SongEntry.COLUMN_NAME_DATE_MODIFIED, mCursor.getLong(mCursor.getColumnIndex(AudioColumns.DATE_MODIFIED))); // seconds since 1970 (type long)

            if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER) == null) {
                cv.put(SongEntry.COLUMN_NAME_DISC_NUMBER, 0);
                cv.put(SongEntry.COLUMN_NAME_DISC_TOTAL, 0);
            } else {
                String discNums[] = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER).split("/");
                try {
                    cv.put(SongEntry.COLUMN_NAME_DISC_NUMBER, Integer.parseInt(discNums[0]));
                } catch (NumberFormatException nfe) {
                    cv.put(SongEntry.COLUMN_NAME_DISC_NUMBER, 1);
                }
                if (discNums.length > 1) {
                    cv.put(SongEntry.COLUMN_NAME_DISC_TOTAL, Integer.parseInt(discNums[1]));
                } else {
                    cv.put(SongEntry.COLUMN_NAME_DISC_TOTAL, -1);
                }
            }

            cv.put(SongEntry.COLUMN_NAME_DURATION, mCursor.getLong(mCursor.getColumnIndex(AudioColumns.DURATION))); // length in ms (type long)
            cv.put(SongEntry.COLUMN_NAME_EQUALIZER_PRESET, "");
            cv.put(SongEntry.COLUMN_NAME_FILE_LOCATION, mCursor.getString(mCursor.getColumnIndex(AudioColumns.DATA)));
            cv.put(SongEntry.COLUMN_NAME_GENRE, retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
            cv.put(SongEntry.COLUMN_NAME_HAS_ARTWORK, retriever.getEmbeddedPicture() == null ? 0 : 1);
            cv.put(SongEntry.COLUMN_NAME_LAST_PLAYED, 0L);
            cv.put(SongEntry.COLUMN_NAME_PLAY_COUNT, 0);
            cv.put(SongEntry.COLUMN_NAME_RATING, 0);
            cv.put(SongEntry.COLUMN_NAME_SAMPLE_RATE, 0);
            cv.put(SongEntry.COLUMN_NAME_SIZE, Long.parseLong(mCursor.getString(mCursor.getColumnIndex(AudioColumns.SIZE))));
            cv.put(SongEntry.COLUMN_NAME_SKIP_COUNT, 0);
            cv.put(SongEntry.COLUMN_NAME_SKIP_ON_SHUFFLE, 0);
            cv.put(SongEntry.COLUMN_NAME_TITLE, mCursor.getString(mCursor.getColumnIndex(AudioColumns.TITLE)));

            if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER) == null) {
                cv.put(SongEntry.COLUMN_NAME_TRACK_NUMBER, 0);
                cv.put(SongEntry.COLUMN_NAME_TRACK_TOTAL, 0);
            } else {
                String trackNums[] = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER).split("/");
                try {
                    cv.put(SongEntry.COLUMN_NAME_TRACK_NUMBER, Integer.parseInt(trackNums[0]));
                } catch (NumberFormatException nfe) {
                    cv.put(SongEntry.COLUMN_NAME_TRACK_NUMBER, 1);
                }
                if (trackNums.length > 1) {
                    cv.put(SongEntry.COLUMN_NAME_TRACK_TOTAL, Integer.parseInt(trackNums[1]));
                } else {
                    cv.put(SongEntry.COLUMN_NAME_TRACK_TOTAL, -1);
                }
            }

            int year;
            if (mCursor.getString(mCursor.getColumnIndex(AudioColumns.YEAR)) == null) {
                year = -1;
            } else {
                year = Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(AudioColumns.YEAR)));
            }
            cv.put(SongEntry.COLUMN_NAME_YEAR, year);
            db.insert(SongEntry.TABLE_NAME, null, cv);
            mCursor.moveToNext();
            count++;
            Log.i(TAG, "Finished " + count + " songs");
        }

        retriever.release();
//        db.close(); Not sure if i need this? Probably not because if you're
//                    creating a db, you're not going to be done with it here

        long endTime = System.currentTimeMillis();
        double duration = (endTime - startTime) / 1000.0;
        Log.i(TAG, "Created database of " + mCursor.getCount() + " songs in " + duration + " seconds");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement schema changes and data massage here when upgrading
    }

//    public long insertSong(Song song) {
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_SONG_TITLE, song.getTitle());
//        cv.put(COLUMN_SONG_DURATION, song.getDuration());
//        cv.put(COLUMN_SONG_ARTIST, song.getArtist());
//        cv.put(COLUMN_SONG_ALBUM, song.getAlbum());
//        cv.put(COLUMN_SONG_GENRE, song.getGenre());
//        cv.put(COLUMN_SONG_RATING, song.getRating());
//        cv.put(COLUMN_SONG_PLAY_COUNT, song.getPlayCount());
//        cv.put(COLUMN_SONG_SKIP_COUNT, song.getSkipCount());
//        cv.put(COLUMN_SONG_SIZE, song.getSize());
//        cv.put(COLUMN_SONG_HAS_ARTWORK, song.hasArtwork());
//        cv.put(COLUMN_SONG_DISC_NUMBER, song.getDiscNumber());
//        cv.put(COLUMN_SONG_DISC_COUNT, song.getDiscTotal());
//        cv.put(COLUMN_SONG_TRACK_NUMBER, song.getTrackNumber());
//        cv.put(COLUMN_SONG_TRACK_COUNT, song.getTrackCount());
//        cv.put(COLUMN_SONG_YEAR, song.getYear());
//        cv.put(COLUMN_SONG_NUM_TRACKS, song.getNumTracks());
//        cv.put(COLUMN_SONG_BIT_RATE, song.getBitRate());
//        cv.put(COLUMN_SONG_SAMPLE_RATE, song.getSampleRate());
//        cv.put(COLUMN_SONG_ALBUM_ARTIST, song.getAlbumArtist());
//        cv.put(COLUMN_SONG_COMPILATION_STATUS, song.getCompilationStatus());
//        cv.put(COLUMN_SONG_COMPOSER, song.getComposer());
//        cv.put(COLUMN_SONG_WRITER, song.getWriter());
//        cv.put(COLUMN_SONG_FILENAME, song.getFileName());
//        cv.put(COLUMN_SONG_LOCATION, song.getLocation());
//        cv.put(COLUMN_SONG_DATE_MODIFIED, song.getDateModified().toString());
//
//        return getWritableDatabase().insert(TABLE_SONG, null, cv);
//    }

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

            long id = getLong(getColumnIndex(SongEntry._ID));
            song.setSongID(id);

            String album = getString(getColumnIndex(SongEntry.COLUMN_NAME_ALBUM));
            song.setAlbum(album);

            String albumArtist = getString(getColumnIndex(SongEntry.COLUMN_NAME_ALBUM_ARTIST));
            song.setAlbumArtist(albumArtist);

            String artist = getString(getColumnIndex(SongEntry.COLUMN_NAME_ARTIST));
            song.setArtist(artist);

            int bitRate = getInt(getColumnIndex(SongEntry.COLUMN_NAME_BIT_RATE));
            song.setBitRate(bitRate);

            String composer = getString(getColumnIndex(SongEntry.COLUMN_NAME_COMPOSER));
            song.setComposer(composer);

            long dateModified = getLong(getColumnIndex(SongEntry.COLUMN_NAME_DATE_MODIFIED));
            song.setDateModified(dateModified);

            int discNumber = getInt(getColumnIndex(SongEntry.COLUMN_NAME_DISC_NUMBER));
            song.setDiscNumber(discNumber);

            int discTotal = getInt(getColumnIndex(SongEntry.COLUMN_NAME_DISC_TOTAL));
            song.setDiscTotal(discTotal);

            long duration = getLong(getColumnIndex(SongEntry.COLUMN_NAME_DURATION));
            song.setDuration(duration);

            String equalizerPreset = getString(getColumnIndex(SongEntry.COLUMN_NAME_EQUALIZER_PRESET));
            song.setEqualizerPreset(equalizerPreset);

            String fileLocation = getString(getColumnIndex(SongEntry.COLUMN_NAME_FILE_LOCATION));
            song.setLocation(fileLocation);

            String genre = getString(getColumnIndex(SongEntry.COLUMN_NAME_GENRE));
            song.setGenre(genre);

            int iHasArtwork = getInt(getColumnIndex(SongEntry.COLUMN_NAME_HAS_ARTWORK));
            boolean hasArtwork = iHasArtwork == 1;
            song.setHasArtwork(hasArtwork);

            long lastPlayed = getLong(getColumnIndex(SongEntry.COLUMN_NAME_LAST_PLAYED));
            song.setLastPlayed(lastPlayed);

            int playCount = getInt(getColumnIndex(SongEntry.COLUMN_NAME_PLAY_COUNT));
            song.setPlayCount(playCount);

            int rating = getInt(getColumnIndex(SongEntry.COLUMN_NAME_RATING));
            song.setRating(rating);

            int sampleRate = getInt(getColumnIndex(SongEntry.COLUMN_NAME_SAMPLE_RATE));
            song.setSampleRate(sampleRate);

            long size = getLong(getColumnIndex(SongEntry.COLUMN_NAME_SIZE));
            song.setSize(size);

            int skipCount = getInt(getColumnIndex(SongEntry.COLUMN_NAME_SKIP_COUNT));
            song.setSkipCount(skipCount);

            int iSkipOnShuffle = getInt(getColumnIndex(SongEntry.COLUMN_NAME_SKIP_ON_SHUFFLE));
            boolean skipOnShuffle = iSkipOnShuffle == 1;
            song.setSkipOnShuffle(skipOnShuffle);

            String title = getString(getColumnIndex(SongEntry.COLUMN_NAME_TITLE));
            song.setTitle(title);

            int trackNumber = getInt(getColumnIndex(SongEntry.COLUMN_NAME_TRACK_NUMBER));
            song.setTrackNumber(trackNumber);

            int trackTotal = getInt(getColumnIndex(SongEntry.COLUMN_NAME_TRACK_TOTAL));
            song.setTrackCount(trackTotal);

            int year = getInt(getColumnIndex(SongEntry.COLUMN_NAME_YEAR));
            song.setYear(year);

            return song;
        }
    }


}
