package com.orobator.android.gramophone.model;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;

import com.orobator.android.gramophone.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Library {
    private static final String TAG = "Library";
    private static ArrayList<Song> mSongs;
    private static Library sLibrary;
    private static MediaMetadataRetriever sMetadataRetriever;
    private Context mAppContext;
    private SongDatabaseHelper mHelper;


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

    public ArrayList<Song> getSongs() {
        if (mSongs != null) {
            return mSongs;
        }

        mSongs = new ArrayList<Song>();

        //New Content Provider stuff

        final StringBuilder mSelection = new StringBuilder();
        mSelection.append(AudioColumns.IS_MUSIC + "=1");
        mSelection.append(" AND " + AudioColumns.TITLE + " != ''"); //$NON-NLS-2$
        Cursor mCursor = mAppContext
                .getContentResolver()
                .query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        new String[]{AudioColumns.ALBUM, AudioColumns.ARTIST, AudioColumns.COMPOSER,
                                AudioColumns.DISPLAY_NAME, AudioColumns.DATE_MODIFIED, AudioColumns.DURATION,
                                AudioColumns.TITLE, AudioColumns.TRACK, AudioColumns.SIZE, AudioColumns.YEAR}, mSelection.toString(), null, Media.DEFAULT_SORT_ORDER);

        mCursor.moveToNext();

        /* The columns to get */
//        String[] mProjection = {AudioColumns.ALBUM, AudioColumns.ARTIST, AudioColumns.COMPOSER,
//                AudioColumns.DISPLAY_NAME, AudioColumns.DATE_MODIFIED, AudioColumns.DURATION,
//                AudioColumns.TITLE, AudioColumns.TRACK, AudioColumns.SIZE, AudioColumns.YEAR};
//        //Display Name = file name
//
//        String mSelectionClause = AudioColumns.IS_MUSIC + "=1" + " AND " + AudioColumns.TITLE + " != ''";
//
//        /*
//         * This defines a one-element String array to contain the selection argument.
//         */
//        String[] mSelectionArgs = {""};
//
//
//        Cursor mCursor = mAppContext
//                .getApplicationContext()
//                .getContentResolver()
//                .query(
//                        Media.INTERNAL_CONTENT_URI,// The content uri of the audio media table
//                        mProjection, //The columns to get
//                        mSelectionClause,
//                        mSelectionArgs,
//                        Media.DEFAULT_SORT_ORDER
//                );

        //TODO fix up metadata + add your own, clean up code

        Log.i(TAG, "Found " + mCursor.getCount() + " songs");
        while (!mCursor.isAfterLast()) {
            Song song = new Song();
            song.setLocation(mCursor.getString(3));
            song.setAlbum(mCursor.getString(0));
            song.setArtist(mCursor.getString(1));
            song.setComposer(mCursor.getString(2));
//            Log.i(TAG, "Date Modified: " + mCursor.getString(4));
            song.setDateModified(new Date(Long.parseLong(mCursor.getString(4))));
            song.setDuration(Long.parseLong(mCursor.getString(5)));
            song.setTitle(mCursor.getString(6));
            Log.i(TAG, "Song " + song.getTitle() + " - " + song.getArtist() + " has track " + mCursor.getString(7));
            song.setSize(Long.parseLong(mCursor.getString(8)));
//            Log.i(TAG, "Year: " + mCursor.getString(9));
            String year = mCursor.getString(9);
            if (year != null) {
                song.setYear(Integer.parseInt(mCursor.getString(9)));
            } else {
                song.setYear(-1);
            }
            mSongs.add(song);
            mCursor.moveToNext();
        }
        //Content provider stuff end

        return mSongs;

    }


//    private void fileSnooper(ArrayList<File> files) {
//        if (files.isEmpty())
//            return;
//
//        ArrayList<File> newFiles = new ArrayList<File>();
//
//        for (File file : files) {
//            if (!file.isDirectory() && isSupportedType(file)) {
//                String filePath = file.getAbsolutePath();
//                if (sMetadataRetriever == null) {
//                    sMetadataRetriever = new MediaMetadataRetriever();
//                }
//                sMetadataRetriever.setDataSource(filePath);
//                Song song = new Song();
//                song.setLocation(filePath);
//
//                setSongMetadata(song, file);
//                mSongs.add(song);
//                if (mSongs.size() % 150 == 0) {
//                    Log.i(TAG, "Found " + mSongs.size() + " songs");
//                }
//            } else if (file.isDirectory()) {
//                File[] dirContents = file.listFiles();
//                if (dirContents == null) {
//                    continue;
//                }
//                for (File content : dirContents) {
//                    newFiles.add(content); //TODO slow, fix this
//                }
//            }
//        }
//
//        if (newFiles.isEmpty()) {
//            return;
//        }
//
//        fileSnooper(newFiles);
//    }

    private boolean isSupportedType(File file) {
        String filePath = file.getPath();
        String[] pathArray = filePath.split("\\.");
        String[] compat = mAppContext.getResources().getStringArray(R.array.supported_file_types);

        if (pathArray.length == 0) {
            return false;
        }

        String extension = pathArray[pathArray.length - 1];

        for (String type : compat) {
            if (type.equals(extension)) {
                return true;
            }
        }

        return false;

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
