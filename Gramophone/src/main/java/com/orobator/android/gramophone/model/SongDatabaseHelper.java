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

import com.orobator.android.gramophone.model.LibraryContract.AlbumEntry;
import com.orobator.android.gramophone.model.LibraryContract.ArtistEntry;
import com.orobator.android.gramophone.model.LibraryContract.SongEntry;

import java.util.HashSet;

public class SongDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SongDatabaseHelper";
    private static final String DATABASE_NAME = "Library.db";
    private static final int VERSION = 1;
    private static final String BIG_INT_TYPE = " BIG_INT";
    private static final String INT_TYPE = " INT";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_SONG_ENTRIES =
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
    private static final String SQL_CREATE_ALBUM_ENTRIES =
            "CREATE TABLE " + AlbumEntry.TABLE_NAME + " (" +
                    AlbumEntry._ID + " INTEGER PRIMARY KEY," +
                    AlbumEntry.COLUMN_NAME_ALBUM_NAME + TEXT_TYPE + COMMA_SEP +
                    AlbumEntry.COLUMN_NAME_ALBUM_ARTIST + TEXT_TYPE + ")";
    private static final String SQL_CREATE_ARTIST_ENTRIES =
            "CREATE TABLE " + ArtistEntry.TABLE_NAME + " (" +
                    ArtistEntry._ID + " INTEGER PRIMARY KEY," +
                    ArtistEntry.COLUMN_NAME_ARTIST_NAME + TEXT_TYPE + ")";
    private static final String SQL_DELETE_SONG_ENTRIES =
            "DROP TABLE IF EXISTS " + SongEntry.TABLE_NAME;
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

    public AlbumCursor queryAlbums() {
        Cursor wrapped = getReadableDatabase()
                .query(
                        AlbumEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        AlbumEntry.COLUMN_NAME_ALBUM_NAME + " asc");
        return new AlbumCursor(wrapped);
    }

    public ArtistCursor queryArtists() {
        Cursor wrapped = getReadableDatabase()
                .query(
                        ArtistEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        ArtistEntry.COLUMN_NAME_ARTIST_NAME + " asc");
        return new ArtistCursor(wrapped);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SONG_ENTRIES);
        db.execSQL(SQL_CREATE_ALBUM_ENTRIES);
        db.execSQL(SQL_CREATE_ARTIST_ENTRIES);

        // Populate
        long startTime = System.currentTimeMillis();
        int songCount = createSongsAndArtists(db);

        long endTime = System.currentTimeMillis();
        double duration1 = (endTime - startTime) / 1000.0;

        startTime = System.currentTimeMillis();
        int albumCount = createAlbums(db);
        endTime = System.currentTimeMillis();
        double duration2 = (endTime - startTime) / 1000.0;
        Log.i(TAG, "Created database of " + songCount + " songs in " + duration1 + " seconds");
        Log.i(TAG, "Created database of " + albumCount + " albums in " + duration2 + " seconds");
        Log.i(TAG, "Total time : " + (duration1 + duration2) + " seconds");
//        db.close(); Not sure if i need this? Probably not because if you're
//                    creating a db, you're not going to be done with it here


    }

    public int createSongsAndArtists(SQLiteDatabase db) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String mProjection[] =
                {
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

        HashSet<String> artistSet = new HashSet<String>();

        while (!mCursor.isAfterLast()) {
            retriever.setDataSource(mCursor.getString(mCursor.getColumnIndex(AudioColumns.DATA)));
            ContentValues cv = new ContentValues();

            String albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            if (albumName == null) {
                albumName = "<unknown>";
            }

            cv.put(SongEntry.COLUMN_NAME_ALBUM, albumName);
            cv.put(SongEntry.COLUMN_NAME_ALBUM_ARTIST, retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));

            String artistName = mCursor.getString(mCursor.getColumnIndex(AudioColumns.ARTIST));

            cv.put(SongEntry.COLUMN_NAME_ARTIST, artistName);
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

            if (!artistSet.contains(artistName)) {
                artistSet.add(artistName);
                ContentValues values = new ContentValues();
                values.put(ArtistEntry.COLUMN_NAME_ARTIST_NAME, artistName);
                db.insert(ArtistEntry.TABLE_NAME, null, values);
            }


            Log.i(TAG, "Processed " + mCursor.getPosition() + " songs");
            mCursor.moveToNext();
        }

        retriever.release();
        int count = mCursor.getCount();
        mCursor.close();
        return count;
    }

    public int createAlbums(SQLiteDatabase db) {
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String mProjection[] =
                {
                        AudioColumns.ARTIST,
                        AudioColumns.DATA
                };

        String mSelection = AudioColumns.IS_MUSIC + "=1 AND "
                + AudioColumns.ALBUM + " != ''";

        Cursor mCursor = sContext
                .getContentResolver()
                .query(
                        uri,
                        mProjection,
                        mSelection,
                        null,
                        AudioColumns.ALBUM_KEY
                );

        mCursor.moveToFirst();
        Log.i(TAG, "Found " + mCursor.getCount() + " albums");
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        HashSet<Album> albumSet = new HashSet<Album>();

        while (!mCursor.isAfterLast()) {
            retriever.setDataSource(mCursor.getString(mCursor.getColumnIndex(AudioColumns.DATA)));

            Album album = new Album();
            String albumArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);

            if (albumArtist == null) {
                String songArtist = mCursor.getString(mCursor.getColumnIndex(AudioColumns.ARTIST));
                album.setAlbumArtist(songArtist);
            } else {
                album.setAlbumArtist(albumArtist);
            }
            String albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            if (albumName == null || "".equals(albumName)) {
                albumName = "<unknown>";
            }
            album.setAlbumName(albumName);

            if (!albumSet.contains(album)
                    && !album.getAlbumName().equals("<unknown>")) {
                albumSet.add(album);
                ContentValues values = new ContentValues();
                values.put(AlbumEntry.COLUMN_NAME_ALBUM_ARTIST, album.getAlbumArtist());
                values.put(AlbumEntry.COLUMN_NAME_ALBUM_NAME, album.getAlbumName());
                db.insert(AlbumEntry.TABLE_NAME, null, values);
                Log.i(TAG, "Processed " + albumSet.size() + " albums");
            }

            mCursor.moveToNext();
        }
        int count = mCursor.getCount();
        mCursor.close();
        return count;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement schema changes and data massage here when upgrading
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

    public static class AlbumCursor extends CursorWrapper {
        public AlbumCursor(Cursor cursor) {
            super(cursor);
        }

        public Album getAlbum() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            Album album = new Album();
            album.setAlbumName(getString(getColumnIndex(AlbumEntry.COLUMN_NAME_ALBUM_NAME)));
            album.setAlbumArtist(getString(getColumnIndex(AlbumEntry.COLUMN_NAME_ALBUM_ARTIST)));
            return album;
        }
    }

    public static class ArtistCursor extends CursorWrapper {
        public ArtistCursor(Cursor cursor) {
            super(cursor);
        }

        public String getArtist() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            return getString(getColumnIndex(ArtistEntry.COLUMN_NAME_ARTIST_NAME));
        }
    }


}
