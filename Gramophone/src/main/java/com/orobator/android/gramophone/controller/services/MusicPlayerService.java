package com.orobator.android.gramophone.controller.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;

import com.orobator.android.gramophone.model.Song;

import java.io.File;
import java.io.IOException;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {
    public static final String ACTION_PLAY = "com.orobator.android.gramophone.ACTION_PLAY";
    public static final String ACTION_TOGGLE_PLAYBACK = "com.orobator.android.gramophone.ACTION_TOGGLE_PLAYBACK";
    public static final String ACTION_SEEK_TO = "com.orobator.android.gramophone.ACTION_SEEK_TO";
    public static final String KEY_SEEK_TO = "com.orobator.android.gramophone.KEY_SEEK_TO";
    static MediaPlayer sMediaPlayer = null;
    private static long currentSongId = -1;
    private Song mSong;

    public static long getCurrentSongId() {
        return currentSongId;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            mSong = (Song) intent.getSerializableExtra(Song.KEY_SONG);
            currentSongId = mSong.getSongID();
            playSong(); // TODO: You're also calling playSong() in swipeViewListener. One of them has got to go.
        } else if (intent.getAction().equals(ACTION_TOGGLE_PLAYBACK)) {
            if (sMediaPlayer != null) {
                if (sMediaPlayer.isPlaying()) {
                    sMediaPlayer.pause();
                } else {
                    sMediaPlayer.start();
                }
            }
        } else if (intent.getAction().equals(ACTION_SEEK_TO)) {
            if (sMediaPlayer != null) {
                int seek = intent.getIntExtra(KEY_SEEK_TO, 0);
                sMediaPlayer.seekTo(seek);
            }
        }

        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void playSong() {
        Uri songUri = Uri.fromFile(new File(mSong.getFilePath()));
        sMediaPlayer = new MediaPlayer();
        sMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        sMediaPlayer.setOnPreparedListener(this);
        try {
            sMediaPlayer.setDataSource(getApplicationContext(), songUri);
            sMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * SeekBarUpdaterThread makes sure to update the SeekBar on the
     * NowPlayingFragment
     */
    public static class SeekBarUpdaterThread extends HandlerThread {
        private static final String TAG = "SeekBarUpdaterThread";
        private static final int UPDATE_SEEKBAR = 0;
        private SeekBar mSeekBar;
        private Listener mListener;
        /**
         * The handler for this SeekBarUpdaterThread
         */
        private Handler mThreadHandler;

        /**
         * UI can only be updated on main thread, so a Handler to the main
         * thread is needed
         */
        private Handler mMainThreadHandler;

        public SeekBarUpdaterThread(Handler handler) {
            super(TAG);
            mMainThreadHandler = handler;
        }

        @Override
        protected void onLooperPrepared() {
            mThreadHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == UPDATE_SEEKBAR) {
                        mMainThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (sMediaPlayer != null && sMediaPlayer.isPlaying()) {
                                    mListener.onUpdateReceived(mSeekBar, sMediaPlayer.getCurrentPosition());
                                }
                            }
                        });
                    }
                    Message message = mThreadHandler.obtainMessage(UPDATE_SEEKBAR);
                    sendMessageDelayed(message, 100);
                }
            };
        }

        public void setListener(Listener listener) {
            mListener = listener;
            if (mThreadHandler != null) {
                mThreadHandler.obtainMessage(UPDATE_SEEKBAR).sendToTarget();
            } else {
                Log.i(TAG, "mThreadHandler was null");
            }
        }

        public void setSeekBar(SeekBar seekBar) {
            mSeekBar = seekBar;
        }

        /**
         * Used as callback to the main thread so the SeekBar can be updated
         * with the passage of time
         */
        public interface Listener {
            void onUpdateReceived(SeekBar seekBar, int progress);
        }


    }
}
