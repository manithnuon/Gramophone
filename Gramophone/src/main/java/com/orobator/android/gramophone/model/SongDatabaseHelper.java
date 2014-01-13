package com.orobator.android.gramophone.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.util.Log;

import com.orobator.android.gramophone.model.LibraryContract.AlbumEntry;
import com.orobator.android.gramophone.model.LibraryContract.ArtistEntry;
import com.orobator.android.gramophone.model.LibraryContract.GenreEntry;
import com.orobator.android.gramophone.model.LibraryContract.SongEntry;

import org.michaelevans.colorart.library.ColorArt;

import java.io.Serializable;
import java.util.HashSet;

/**
 * SongDatabaseHelper is the class used to query the database of songs
 */
public class SongDatabaseHelper extends SQLiteOpenHelper {
    public static final String UPDATE_TITLE = "update_title";
    public static final String UPDATE_ARTIST = "update_artist";
    public static final String UPDATE_ALBUM = "update_album";
    public static final String UPDATE_ALBUM_ARTIST = "update_album_artist";
    public static final String UPDATE_GENRE = "update_genre";
    public static final String UPDATE_HAS_ARTWORK = "update_has_artwork";
    public static final String UPDATE_COMPILATION = "update_compilation";
    public static final String UPDATE_COMPOSER = "update_composer";
    public static final String UPDATE_DISC_NUM = "update_disc_num";
    public static final String UPDATE_DISC_COUNT = "update_disc_count";
    public static final String UPDATE_LOCATION = "update_location";
    public static final String UPDATE_PLAY_COUNT = "update_play_count";
    public static final String UPDATE_RATING = "update_rating";
    public static final String UPDATE_SKIP_COUNT = "update_skip_count";
    public static final String UPDATE_TRACK_NUM = "update_track_num";
    public static final String UPDATE_TRACK_COUNT = "update_track_count";
    public static final String UPDATE_WRITER = "update_writer";
    public static final String UPDATE_YEAR = "update_year";
    private static final int VERSION = 1;
    private static final String TAG = "SongDatabaseHelper";
    private static final String DATABASE_NAME = "Library.db";
    private static final String BIG_INT_TYPE = " BIG_INT";
    private static final String INT_TYPE = " INT";
    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ARTIST_ENTRIES =
            "CREATE TABLE " + ArtistEntry.TABLE_NAME + " (" +
                    ArtistEntry._ID + " INTEGER PRIMARY KEY," +
                    ArtistEntry.COLUMN_NAME_ARTIST_NAME + TEXT_TYPE + ")";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ALBUM_ENTRIES =
            "CREATE TABLE " + AlbumEntry.TABLE_NAME + " (" +
                    AlbumEntry._ID + " INTEGER PRIMARY KEY," +
                    AlbumEntry.COLUMN_NAME_ALBUM_NAME + TEXT_TYPE + COMMA_SEP +
                    AlbumEntry.COLUMN_NAME_ALBUM_ARTIST + TEXT_TYPE + ")";
    private static final String SQL_CREATE_SONG_ENTRIES =
            "CREATE TABLE " + SongEntry.TABLE_NAME + " (" +
                    SongEntry._ID + " INTEGER PRIMARY KEY," +
                    SongEntry.COLUMN_NAME_ALBUM + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_ALBUM_ARTIST + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_ARTIST + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_BIT_RATE + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_COLOR_BACKGROUND + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_COLOR_PRIMARY + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_COLOR_SECONDARY + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_COLOR_DETAIL + INT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_COMPILATION + TEXT_TYPE + COMMA_SEP +
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
                    SongEntry.COLUMN_NAME_WRITER + TEXT_TYPE + COMMA_SEP +
                    SongEntry.COLUMN_NAME_YEAR + INT_TYPE + ")";
    private static final String SQL_CREATE_GENRE_ENTRIES =
            "CREATE TABLE " + GenreEntry.TABLE_NAME + " (" +
                    GenreEntry._ID + " INTEGER PRIMARY KEY," +
                    GenreEntry.COLUMN_NAME_GENRE_NAME + ")";
    private static final String SQL_DELETE_SONG_ENTRIES =
            "DROP TABLE IF EXISTS " + SongEntry.TABLE_NAME;
    private static SongCursor allSongs;
    private Context sContext;

    public SongDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        sContext = context;
    }

    public void deleteSong(long songId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(SongEntry.TABLE_NAME, "_ID=?", new String[]{Long.toString(songId)});
    }

    public void updateSongMetadata(long songId, String whatToUpdate, String newValue, String songPath) {
        SQLiteDatabase db = getWritableDatabase();

        String id = Long.toString(songId);
        ContentValues cv = new ContentValues();
        String where = SongEntry._ID + "=?";
        String whereArgs[] = {id};

        switch (whatToUpdate) {
            case UPDATE_TITLE:
                cv.put(SongEntry.COLUMN_NAME_TITLE, newValue);
                break;
            case UPDATE_ARTIST:
                cv.put(SongEntry.COLUMN_NAME_ARTIST, newValue);
                break;
            case UPDATE_ALBUM:
                cv.put(SongEntry.COLUMN_NAME_ALBUM, newValue);
                break;
            case UPDATE_ALBUM_ARTIST:
                cv.put(SongEntry.COLUMN_NAME_ALBUM_ARTIST, newValue);
                break;
            case UPDATE_GENRE:
                cv.put(SongEntry.COLUMN_NAME_GENRE, newValue);
                break;
            case UPDATE_HAS_ARTWORK:
                // TODO do nothing for now
                return;
            case UPDATE_COMPILATION:
                cv.put(SongEntry.COLUMN_NAME_COMPILATION, newValue);
                break;
            case UPDATE_COMPOSER:
                cv.put(SongEntry.COLUMN_NAME_COMPOSER, newValue);
                break;
            case UPDATE_DISC_NUM:
                cv.put(SongEntry.COLUMN_NAME_DISC_NUMBER, Integer.getInteger(newValue));
                break;
            case UPDATE_DISC_COUNT:
                cv.put(SongEntry.COLUMN_NAME_DISC_TOTAL, Integer.getInteger(newValue));
                break;
            case UPDATE_LOCATION:
                // TODO do nothing for now
                return;
            case UPDATE_PLAY_COUNT:
                cv.put(SongEntry.COLUMN_NAME_PLAY_COUNT, Integer.getInteger(newValue));
                break;
            case UPDATE_RATING:
                cv.put(SongEntry.COLUMN_NAME_RATING, Integer.getInteger(newValue));
                break;
            case UPDATE_SKIP_COUNT:
                cv.put(SongEntry.COLUMN_NAME_SKIP_COUNT, Integer.getInteger(newValue));
                break;
            case UPDATE_TRACK_NUM:
                cv.put(SongEntry.COLUMN_NAME_TRACK_NUMBER, Integer.getInteger(newValue));
                break;
            case UPDATE_TRACK_COUNT:
                cv.put(SongEntry.COLUMN_NAME_TRACK_TOTAL, Integer.getInteger(newValue));
                break;
            case UPDATE_WRITER:
                cv.put(SongEntry.COLUMN_NAME_WRITER, newValue);
                break;
            case UPDATE_YEAR:
                cv.put(SongEntry.COLUMN_NAME_YEAR, Integer.getInteger(newValue));
                break;
            default:
        }

        //TODO Actually edit the song's metadata.
        db.update(SongEntry.TABLE_NAME, cv, where, whereArgs);
    }

    /**
     * querySongs() queries the Library database for all songs
     *
     * @return a SongCursor containing all of the songs in the database
     */
    public SongCursor querySongs() {
        if (allSongs != null && !allSongs.isClosed()) {
            return allSongs;
        }
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
        allSongs = new SongCursor(wrapped);
        return allSongs;
    }

    /**
     * querySongsForArtist(String) returns a SongCursor containing all songs by
     * a given artist
     */
    public SongCursor querySongsForArtist(String artist) {
        String selection = SongEntry.COLUMN_NAME_ARTIST + "=?";
        String selectionArgs[] = {artist};
        Cursor wrapped = getReadableDatabase()
                .query(SongEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null, null,
                        SongEntry.COLUMN_NAME_TITLE + " asc");
        return new SongCursor(wrapped);
    }

    /**
     * querySongsForAlbum(Album) returns a SongCursor containing the songs in
     * the given album
     *
     * @param album The album to get songs from
     *
     * @return A SongCursor with all the songs from the specified album
     */
    public SongCursor querySongsForAlbum(Album album) {
        String selection =
                SongEntry.COLUMN_NAME_ALBUM + " =? AND "
                        + SongEntry.COLUMN_NAME_ALBUM_ARTIST + " =?";
        String selectionArgs[] = {album.getAlbumName(), album.getAlbumArtist()};
        Cursor wrapped = getReadableDatabase()
                .query(
                        SongEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null, null,
                        SongEntry.COLUMN_NAME_ALBUM + " asc");

        return new SongCursor(wrapped);
    }

    /**
     * querySongsForGenre(String) returns a SongCursor containing the songs that
     * have the given genre
     *
     * @param genre the genre that all songs in the returned SongCursor will
     *              have
     *
     * @return A SongCursor with al the songs that the specified genre
     */
    public SongCursor querySongsForGenre(String genre) {
        String selection = SongEntry.COLUMN_NAME_GENRE + " =?";
        String selectionArgs[] = {genre};
        Cursor wrapped = getReadableDatabase()
                .query(
                        SongEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        SongEntry.COLUMN_NAME_TITLE + " asc");
        return new SongCursor(wrapped);
    }

    /**
     * queryAlbums() returns an AlbumCursor containing all albums in the
     * Library database
     *
     * @return an AlbumCursor containing all albums in the Library database
     */
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

    /**
     * queryAlbumsForArtist(String) returns an AlbumCursor containing all
     * albums by the specified artist
     */
    public AlbumCursor queryAlbumsForArtist(String artist) {
        String selection = AlbumEntry.COLUMN_NAME_ALBUM_ARTIST + " =?";
        String selectionArgs[] = {artist};
        Cursor wrapped = getReadableDatabase()
                .query(
                        AlbumEntry.TABLE_NAME,
                        null,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        AlbumEntry.COLUMN_NAME_ALBUM_NAME);
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

    public GenreCursor queryGenres() {
        Cursor wrapped = getReadableDatabase()
                .query(
                        GenreEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        GenreEntry.COLUMN_NAME_GENRE_NAME + " asc");
        return new GenreCursor(wrapped);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SONG_ENTRIES);
        db.execSQL(SQL_CREATE_ALBUM_ENTRIES);
        db.execSQL(SQL_CREATE_ARTIST_ENTRIES);
        db.execSQL(SQL_CREATE_GENRE_ENTRIES);

        // Populate
        long startTime = System.currentTimeMillis();
        int songCount = createSongsAndArtists(db);

        long endTime = System.currentTimeMillis();
        double duration1 = (endTime - startTime) / 1000.0;

        startTime = System.currentTimeMillis();
        int albumCount = createAlbums(db);
        endTime = System.currentTimeMillis();
        double duration2 = (endTime - startTime) / 1000.0;

        startTime = System.currentTimeMillis();
        int genreCount = createGenres(db);
        endTime = System.currentTimeMillis();
        double duration3 = (endTime - startTime) / 1000.0;

        Log.i(TAG, "Created database of " + songCount + " songs in " + duration1 + " seconds");
        Log.i(TAG, "Created database of " + albumCount + " albums in " + duration2 + " seconds");
        Log.i(TAG, "Created database of " + genreCount + " genres in " + duration3 + " seconds");
        Log.i(TAG, "Total time : " + (duration1 + duration2 + duration3) + " seconds");
//        db.close(); Not sure if i need this? Probably not because if you're
//                    creating a db, you're not going to be done with it here


    }

    private int createSongsAndArtists(SQLiteDatabase db) {
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
            try {
                retriever.setDataSource(mCursor.getString(mCursor.getColumnIndex(AudioColumns.DATA)));
            } catch (RuntimeException re) {
                Log.e(TAG, "Failed retriever.setDataSource(" + mCursor.getString(mCursor.getColumnIndex(AudioColumns.DATA)) + ")");
                // MetadataRetriever doesn't like midi files :(
                // TODO Use FFMPEG MetadataRetriever https://github.com/wseemann/FFmpegMediaMetadataRetriever
                mCursor.moveToNext();
                continue;
            }
            ContentValues cv = new ContentValues();

            String albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            if (albumName == null) {
                albumName = "<unknown>";
            }

            cv.put(SongEntry.COLUMN_NAME_ALBUM, albumName);

            String albumArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
            String artistName = mCursor.getString(mCursor.getColumnIndex(AudioColumns.ARTIST));

            if (albumArtist == null) {
                albumArtist = artistName;
            }

            cv.put(SongEntry.COLUMN_NAME_ALBUM_ARTIST, albumArtist);
            cv.put(SongEntry.COLUMN_NAME_ARTIST, artistName);

            try {
                cv.put(SongEntry.COLUMN_NAME_BIT_RATE, Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)));
            } catch (NumberFormatException nfe) {
                cv.put(SongEntry.COLUMN_NAME_BIT_RATE, -1);
            }

            cv.put(SongEntry.COLUMN_NAME_COMPOSER, mCursor.getString(mCursor.getColumnIndex(AudioColumns.COMPOSER)));
            int dateModified = mCursor.getColumnIndex(AudioColumns.DATE_MODIFIED);
//            if (dateModifiedString.equals("null")) {
//                dateModifiedString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
//            }
            cv.put(SongEntry.COLUMN_NAME_DATE_MODIFIED, dateModified/*mCursor.getLong(dateModifiedString)*/); // seconds since 1970 (type long)

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
                    cv.put(SongEntry.COLUMN_NAME_DISC_TOTAL, 1);
                }
            }

            cv.put(SongEntry.COLUMN_NAME_DURATION, mCursor.getLong(mCursor.getColumnIndex(AudioColumns.DURATION))); // length in ms (type long)
            cv.put(SongEntry.COLUMN_NAME_EQUALIZER_PRESET, "");
            cv.put(SongEntry.COLUMN_NAME_FILE_LOCATION, mCursor.getString(mCursor.getColumnIndex(AudioColumns.DATA)));
            cv.put(SongEntry.COLUMN_NAME_GENRE, retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));

            byte albumBytes[] = retriever.getEmbeddedPicture();

            int backGroundColor, primaryColor, secondaryColor, detailColor;

            if (albumBytes != null) {
                Bitmap albumCover = BitmapFactory.decodeByteArray(albumBytes, 0, albumBytes.length);
                ColorArt colorArt = new ColorArt(albumCover);
                backGroundColor = colorArt.getBackgroundColor();
                primaryColor = colorArt.getPrimaryColor();
                secondaryColor = colorArt.getSecondaryColor();
                detailColor = colorArt.getDetailColor();
            } else {
                backGroundColor = Song.DEFAULT_BACKGROUND_COLOR;
                primaryColor = Song.DEFAULT_PRIMARY_COLOR;
                secondaryColor = Song.DEFAULT_SECONDARY_COLOR;
                detailColor = Song.DEFAULT_DETAIL_COLOR;
            }

            cv.put(SongEntry.COLUMN_NAME_COLOR_BACKGROUND, backGroundColor);
            cv.put(SongEntry.COLUMN_NAME_COLOR_PRIMARY, primaryColor);
            cv.put(SongEntry.COLUMN_NAME_COLOR_SECONDARY, secondaryColor);
            cv.put(SongEntry.COLUMN_NAME_COLOR_DETAIL, detailColor);

            cv.put(SongEntry.COLUMN_NAME_HAS_ARTWORK, albumBytes == null ? 0 : 1);

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
                    cv.put(SongEntry.COLUMN_NAME_TRACK_TOTAL, 1);
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement schema changes and data massage here when upgrading
    }

    private int createGenres(SQLiteDatabase db) {
        Uri uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
        String projection[] = {MediaStore.Audio.Genres.NAME};
        String selection = MediaStore.Audio.Genres.NAME + " != ''";
        String sortOrder = MediaStore.Audio.Genres.NAME + " asc";

        Cursor cursor = sContext
                .getContentResolver()
                .query(uri, projection, selection, null, sortOrder);

        cursor.moveToNext();

        HashSet<String> genres = new HashSet<String>();

        while (!cursor.isAfterLast()) {
            String genre = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME));

            if (!genres.contains(genre) && !"".equals(genre)) {
                genres.add(genre);
                ContentValues values = new ContentValues();
                values.put(GenreEntry.COLUMN_NAME_GENRE_NAME, genre);
                db.insert(GenreEntry.TABLE_NAME, null, values);
                Log.i(TAG, "Created " + genres.size() + " genres");
            }

            cursor.moveToNext();
        }

        return genres.size();
    }

    private int createAlbums(SQLiteDatabase db) {
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
            try {
                retriever.setDataSource(mCursor.getString(mCursor.getColumnIndex(AudioColumns.DATA)));
            } catch (RuntimeException re) {
                Log.e(TAG, "Failed retriever.setDataSource(" + mCursor.getString(mCursor.getColumnIndex(AudioColumns.DATA)) + ")");
                // MetadataRetriever doesn't like midi files :(
                // TODO Use FFMPEG MetadataRetriever https://github.com/wseemann/FFmpegMediaMetadataRetriever
                mCursor.moveToNext();
                continue;
            }

            String albumName = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            if (albumName == null || "".equals(albumName)) {
                albumName = "<unknown>";
            }

            String albumArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST);
            if (albumArtist == null) {
                albumArtist = mCursor.getString(mCursor.getColumnIndex(AudioColumns.ARTIST));
            }

            Album album = new Album(albumName, albumArtist);

            if (!albumSet.contains(album) // Avoid duplicates
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

    /**
     * A convenience class to wrap a cursor that returns rows from the "song"
     * table. The {@link getSong()} method returns a Song instance representing
     * the current row.
     */
    public static class SongCursor extends CursorWrapper implements Serializable {
        public static final long serialVersionUID = 1L;

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

            int backgroundColor = getInt(getColumnIndex(SongEntry.COLUMN_NAME_COLOR_BACKGROUND));
            song.setBackgroundColor(backgroundColor);

            int primaryColor = getInt(getColumnIndex(SongEntry.COLUMN_NAME_COLOR_PRIMARY));
            song.setPrimaryColor(primaryColor);

            int secondaryColor = getInt(getColumnIndex(SongEntry.COLUMN_NAME_COLOR_SECONDARY));
            song.setSecondaryColor(secondaryColor);

            int detailColor = getInt(getColumnIndex(SongEntry.COLUMN_NAME_COLOR_DETAIL));
            song.setDetailColor(detailColor);

            String composer = getString(getColumnIndex(SongEntry.COLUMN_NAME_COMPOSER));
            song.setComposer(composer);

            long dateModified = getLong(getColumnIndex(SongEntry.COLUMN_NAME_DATE_MODIFIED));
            song.setDateModified(dateModified);

            int discNumber = getInt(getColumnIndex(SongEntry.COLUMN_NAME_DISC_NUMBER));
            song.setDiscNumber(discNumber);

            int discTotal = getInt(getColumnIndex(SongEntry.COLUMN_NAME_DISC_TOTAL));
            song.setDiscCount(discTotal);

            long duration = getLong(getColumnIndex(SongEntry.COLUMN_NAME_DURATION));
            song.setDuration(duration);

            String equalizerPreset = getString(getColumnIndex(SongEntry.COLUMN_NAME_EQUALIZER_PRESET));
            song.setEqualizerPreset(equalizerPreset);

            String fileLocation = getString(getColumnIndex(SongEntry.COLUMN_NAME_FILE_LOCATION));
            song.setFilePath(fileLocation);

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
            String albumName = getString(getColumnIndex(AlbumEntry.COLUMN_NAME_ALBUM_NAME));
            String albumArtist = getString(getColumnIndex(AlbumEntry.COLUMN_NAME_ALBUM_ARTIST));
            return new Album(albumName, albumArtist);
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

    public static class GenreCursor extends CursorWrapper {
        public GenreCursor(Cursor cursor) {
            super(cursor);
        }

        public String getGenre() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            return getString(getColumnIndex(GenreEntry.COLUMN_NAME_GENRE_NAME));
        }
    }


}
