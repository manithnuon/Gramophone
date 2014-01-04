package com.orobator.android.gramophone.controller.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import com.orobator.android.gramophone.model.Song;

import java.io.File;
import java.io.IOException;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String TAG = "MusicPlayerService";
    public static final String ACTION_PLAY = "com.orobator.android.gramophone.ACTION_PLAY";
    public static final String ACTION_TOGGLE_PLAYBACK = "com.orobator.android.gramophone.ACTION_TOGGLE_PLAYBACK";
    public static final String ACTION_SEEK_TO = "com.orobator.android.gramophone.ACTION_SEEK_TO";
    public static final String KEY_SEEK_TO = "com.orobator.android.gramophone.KEY_SEEK_TO";
    MediaPlayer mMediaPlayer = null;

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Log.d(TAG, "MediaPlayer started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        Log.d(TAG, "onStartCommand");
        if (intent.getAction().equals(ACTION_PLAY)) {
            Log.d(TAG, "Received ACTION_PLAY");
            Song song = (Song) intent.getSerializableExtra(Song.KEY_SONG);
            Uri songUri = Uri.fromFile(new File(song.getFilePath()));
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setOnPreparedListener(this);
            try {
                mMediaPlayer.setDataSource(getApplicationContext(), songUri);
                mMediaPlayer.prepareAsync();
                Log.d(TAG, "Prepared the MediaPlayer asynchronously");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (intent.getAction().equals(ACTION_TOGGLE_PLAYBACK)) {
            if (mMediaPlayer != null) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                } else {
                    mMediaPlayer.start();
                }
            }
        } else if (intent.getAction().equals(ACTION_SEEK_TO)) {
            if (mMediaPlayer != null) {
                int seek = intent.getIntExtra(KEY_SEEK_TO, 0);
                mMediaPlayer.seekTo(seek);
            }
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
