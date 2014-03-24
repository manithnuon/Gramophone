package com.orobator.android.gramophone.controller.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.controller.broadcast.receivers.RemoteControlReceiver;
import com.orobator.android.gramophone.model.Song;
import com.orobator.android.gramophone.model.SongQueue;
import com.orobator.android.gramophone.view.NotificationBuilder;

import java.io.File;
import java.io.IOException;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener,
        AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, SongQueue.OnQueueChangeListener {
    public static final String ACTION_NEXT = "com.orobator.android.gramophone.ACTION_NEXT";
    public static final String ACTION_PAUSE = "com.orobator.android.gramophone.ACTION_PAUSE";
    public static final String ACTION_PLAY = "com.orobator.android.gramophone.ACTION_PLAY";
    public static final String ACTION_PREV = "com.orobator.android.gramophone.ACTION_PREV";
    public static final String ACTION_TOGGLE_PLAYBACK = "com.orobator.android.gramophone.ACTION_TOGGLE_PLAYBACK";
    public static final String ACTION_SEEK_TO = "com.orobator.android.gramophone.ACTION_SEEK_TO";
    public static final String ACTION_STOP = "com.orobator.android.gramophone.ACTION_STOP";
    public static final String KEY_SEEK_TO = "com.orobator.android.gramophone.KEY_SEEK_TO";
    private static MediaPlayer sMediaPlayer = null;
    private static MediaPlayer sNextMediaPlayer = null;
    private static String TAG = "MusicPlayerService";
    private static ComponentName sRemoteControllReciver;
    private static OnSongChangeListener sOnSongChangeListener;
    private static int currentAudioSessionID;
    private Song mSong;



    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            // Pause playback
            if (sMediaPlayer != null) {
                if (sMediaPlayer.isPlaying()) {
                    sMediaPlayer.pause();
                }
            }
        }
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            // Lower the volume
            // TODO: Implement this
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            // Resume playback
            // TODO: Raise audio level to normal
            if (sMediaPlayer != null) {
                sMediaPlayer.start();
            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            am.unregisterMediaButtonEventReceiver(sRemoteControllReciver);
            am.abandonAudioFocus(this);
            // Stop playback
            if (sMediaPlayer != null) {
                sMediaPlayer.stop();
                sMediaPlayer = null;
                // TODO: Make Now Playing fragment disappear somehow, call to finish()?
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(TAG, "onCompletion of session id " + mp.getAudioSessionId());
        mp.stop();
        mp.release();

        SongQueue.moveToNext();
        sMediaPlayer = sNextMediaPlayer;
        currentAudioSessionID = sMediaPlayer.getAudioSessionId();
        sNextMediaPlayer.setOnPreparedListener(this);
        Song nextSong = SongQueue.getNextSong();
        if (nextSong != null) {
            Uri nextSongUri = Uri.fromFile(new File(nextSong.getFilePath()));
            sNextMediaPlayer = new MediaPlayer();
            sNextMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            sNextMediaPlayer.setOnPreparedListener(this);
            sNextMediaPlayer.setOnCompletionListener(this);
            try {
                sNextMediaPlayer.setDataSource(this, nextSongUri);
                sNextMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (sOnSongChangeListener != null) {
            sOnSongChangeListener.onSongChanged();
        }
    }

    @Override
    public void onDestroy() {
        // TODO: This doesn't work. Find out what happens when a user clears your app from recent apps
        // Other apps remain playing when swiped out. Maybe I'll make it a setting when I do get it working
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationBuilder.NOW_PLAYING);

        // Make sure to clear up system resources
        if (sMediaPlayer != null) {
            sMediaPlayer.release();
            sMediaPlayer = null;
        }

        if (sNextMediaPlayer != null) {
            sNextMediaPlayer.release();
            sNextMediaPlayer = null;
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // ... react appropriately ...
        // The MediaPlayer has moved to the Error state, must be reset!
        // TODO: Figure out what should be done here
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp.getAudioSessionId() == currentAudioSessionID) {
            mp.start();
        } else {
            // This is the next MediaPlayer
            sMediaPlayer.setNextMediaPlayer(mp);
        }
        Log.d(TAG, "On prepared MediaPlayer audioSessionID: " + mp.getAudioSessionId());
        mp.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        // TODO: If streaming, use a WifiLock
    }

    @Override
    public void onQueueChange() {
        Song nextSong = SongQueue.getNextSong();

        if (nextSong == null) {
            sMediaPlayer.setNextMediaPlayer(null);
            return;
        }

        if (sNextMediaPlayer != null) {
            sNextMediaPlayer.release();
        }

        Uri nextSongUri = Uri.fromFile(new File(nextSong.getFilePath()));
        sNextMediaPlayer = new MediaPlayer();
        sNextMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        sNextMediaPlayer.setOnPreparedListener(this);
        sNextMediaPlayer.setOnCompletionListener(this);
        try {
            sNextMediaPlayer.setDataSource(this, nextSongUri);
            sNextMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: Update the notification
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        switch (intent.getAction()) {
            case ACTION_NEXT:
                if (sMediaPlayer != null) {
                    Song currentSong = SongQueue.getSong(SongQueue.getCurrentSongQueuePosition());
                    sMediaPlayer.seekTo((int) currentSong.getDuration());
                }
                Log.d(TAG, "received ACTION_NEXT");
                // TODO: Might have to update notification
                break;
            case ACTION_PAUSE:
                if (sMediaPlayer != null) {
                    if (sMediaPlayer.isPlaying()) {
                        sMediaPlayer.pause();
                        NotificationBuilder.createNotification(this, NotificationBuilder.NOW_PLAYING, false);
                    }
                }
                break;
            case ACTION_PLAY:
                mSong = (Song) intent.getSerializableExtra(Song.KEY_SONG);
                if (mSong == null) { // Received a headphone button press
                    if (sMediaPlayer != null) {
                        if (!sMediaPlayer.isPlaying()) {
                            sMediaPlayer.start();
                        }
                    }
                    break;
                }
                playSong();
                if (sMediaPlayer != null) {
                    sMediaPlayer.setOnErrorListener(this);
                }
                break;
            case ACTION_PREV:
                Log.d(TAG, "received ACTION_PREV");
                // TODO: Might have to update notification
                break;
            case ACTION_TOGGLE_PLAYBACK:
                if (sMediaPlayer != null) {
                    if (sMediaPlayer.isPlaying()) {
                        sMediaPlayer.pause();
                        NotificationBuilder.createNotification(this, NotificationBuilder.NOW_PLAYING, false);
                    } else {
                        sMediaPlayer.start();
                        NotificationBuilder.createNotification(this, NotificationBuilder.NOW_PLAYING, true);
                    }
                }
                break;
            case ACTION_SEEK_TO:
                if (sMediaPlayer != null) {
                    int seek = intent.getIntExtra(KEY_SEEK_TO, 0);
                    sMediaPlayer.seekTo(seek);
                }
                break;
            case ACTION_STOP:
                if (sMediaPlayer != null) {
                    sMediaPlayer.stop();

                    // Clean up time!
                    AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    am.unregisterMediaButtonEventReceiver(sRemoteControllReciver);

                    // Abandon audio focus when playback complete
                    am.abandonAudioFocus(this);
                    sMediaPlayer.release();
                    sMediaPlayer = null;
                    // TODO: Make sure Now Playing screen goes away somehow, call to finish() maybe?
                }
                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nm.cancel(NotificationBuilder.NOW_PLAYING);
            default:
        }

        return Service.START_NOT_STICKY;
    }

    private void playSong() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        // Request audio focus for playback
        int result = am.requestAudioFocus(this,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        // No tunes for you!
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // TODO: test this
            String focusFailed = getString(R.string.get_audio_focus_failed);
            Toast.makeText(this, focusFailed, Toast.LENGTH_LONG).show();
            return;
        }

        // Listen for headphone buttons
        sRemoteControllReciver = new ComponentName(getPackageName(), RemoteControlReceiver.class.getName());
        am.registerMediaButtonEventReceiver(sRemoteControllReciver);

        Uri songUri = Uri.fromFile(new File(mSong.getFilePath()));
        sMediaPlayer = new MediaPlayer();
        sMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        sMediaPlayer.setOnPreparedListener(this);
        sMediaPlayer.setOnCompletionListener(this);
        try {
            sMediaPlayer.setDataSource(getApplicationContext(), songUri);
            sMediaPlayer.prepareAsync();
            currentAudioSessionID = sMediaPlayer.getAudioSessionId();
            Log.d(TAG, "Current audio session id : " + currentAudioSessionID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Song nextSong = SongQueue.getSong(SongQueue.getCurrentSongQueuePosition() + 1);

        if (nextSong != null) {
            Uri nextSongUri = Uri.fromFile(new File(nextSong.getFilePath()));
            sNextMediaPlayer = new MediaPlayer();
            sNextMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            sNextMediaPlayer.setOnPreparedListener(this);
            sNextMediaPlayer.setOnCompletionListener(this);
            try {
                sNextMediaPlayer.setDataSource(this, nextSongUri);
                sNextMediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SongQueue.setOnQueueChangeListener(this);
    }

    public static void setOnSongChangeListener(OnSongChangeListener listener) {
        sOnSongChangeListener = listener;
    }

    /**
     * SeekBarUpdaterThread makes sure to update the SeekBar on the
     * NowPlayingFragment
     */
    public static class SeekBarUpdaterThread extends HandlerThread {
        private static final String TAG = "SeekBarUpdaterThread";
        private static final int UPDATE_SEEKBAR = 0;
        private SeekBar mSeekBar;
        protected Listener mListener;
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

    public interface OnSongChangeListener {
        void onSongChanged();
    }
}
