package com.orobator.android.gramophone.model;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.Log;

import com.orobator.android.gramophone.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Library {
    private static final String TAG = "Library";
    private static ArrayList<Song> mSongs;
    private static Library mLibrary;
    private static Context mContext;
    private static MediaMetadataRetriever sMetadataRetriever;

    private Library(Context context) {
        mContext = context;
        sMetadataRetriever = new MediaMetadataRetriever();
    }

    public static Library getLibrary(Context context) {
        if (mLibrary == null) {
            mLibrary = new Library(context);
        }
        return mLibrary;
    }

    public ArrayList<Song> getSongs() {
        if (mSongs != null) {
            return mSongs;
        }

        mSongs = new ArrayList<Song>();

        File musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        //Make the directories just in case they don't exist
        musicDirectory.mkdirs();

        downloadsDirectory.mkdirs();

        File[] musicFiles = musicDirectory.listFiles();
        File[] downloadsFiles = downloadsDirectory.listFiles();

        ArrayList<File> files = new ArrayList<File>();

        if (musicFiles != null) {
            for (File file : musicFiles) {
                files.add(file); //TODO slow, fix this
            }
        } else {
            Log.i(TAG, "No files in music");
        }

        if (downloadsFiles != null) {
            for (File file : downloadsFiles) {
                files.add(file); //TODO slow, fix this
            }
        } else {
            Log.i(TAG, "No files in downloads");
        }

        Log.i(TAG, "Starting fileSnooper on " + files.size() + " files/directories");

        fileSnooper(files);
        Log.i(TAG, "Found " + mSongs.size() + " songs");

        return mSongs;

    }

    //TODO find metadata editing java library

    private void fileSnooper(ArrayList<File> files) {
        if (files.isEmpty())
            return;

        ArrayList<File> newFiles = new ArrayList<File>();

        for (File file : files) {
            if (!file.isDirectory() && isSupportedType(file)) {
                String filePath = file.getAbsolutePath();
                if (sMetadataRetriever == null) {
                    sMetadataRetriever = new MediaMetadataRetriever();
                }
                sMetadataRetriever.setDataSource(filePath);
                Song song = new Song(filePath);

                setSongMetadata(song, file);
                mSongs.add(song);
                if (mSongs.size() % 150 == 0) {
                    Log.i(TAG, "Found " + mSongs.size() + " songs");
                }
            } else if (file.isDirectory()) {
                File[] dirContents = file.listFiles();
                if (dirContents == null) {
                    continue;
                }
                for (File content : dirContents) {
                    newFiles.add(content); //TODO slow, fix this
                }
            }
        }

        if (newFiles.isEmpty()) {
            return;
        }

        fileSnooper(newFiles);
    }

    private boolean isSupportedType(File file) {
        String filePath = file.getPath();
        String[] pathArray = filePath.split("\\.");
        String[] compat = mContext.getResources().getStringArray(R.array.supported_file_types);

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

        song.setDuration(sMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
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
